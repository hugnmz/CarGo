package util.di;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import util.di.annotation.Autowired;
import util.di.annotation.Component;
import util.di.annotation.Repository;
import util.di.annotation.Service;

// class container quan ly dependency injection
public final class DIContainer {

    // khong cho tao instance cua container
    private DIContainer() {}

    // co the set lai base package de quet theo package tuy chon
    // neu rong thi dung danh sach mac dinh ben duoi
    private static final String BASE_PACKAGE = "";

    // cache singletons: key = implementation class, value = instance
    private static final Map<Class<?>, Object> SINGLETONS = new ConcurrentHashMap<>();

    // map interface -> implementation class
    private static final Map<Class<?>, Class<?>> IMPLEMENTATIONS = new ConcurrentHashMap<>();

    // thread-local set de theo doi stack dang build, phat hien vong phu thuoc
    private static final ThreadLocal<Set<Class<?>>> BUILD_STACK = ThreadLocal.withInitial(HashSet::new);

    // static block chay khi class duoc load lan dau -> tu dong scan + register
    static {
        autoScan();
    }

    // --------------------- Auto-scan & register ---------------------

    // quet cac package duoc dinh nghia de dang ky cac implementation
    private static void autoScan() {
        try {
            Set<Class<?>> classes = getAllClassesInPackage(BASE_PACKAGE);
            for (Class<?> clazz : classes) {
                // neu class co annotation danh dau la bean thi register
                if (clazz.isAnnotationPresent(Repository.class)
                        || clazz.isAnnotationPresent(Service.class)
                        || clazz.isAnnotationPresent(Component.class)) {
                    registerClass(clazz);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("[DI] Lỗi autoScan: " + e.getMessage(), e);
        }
    }

    // dang ky implementation: anh xa interface -> implementation class
    private static void registerClass(Class<?> impl) {
        try {
            Class<?>[] itfs = impl.getInterfaces();
            if (itfs != null && itfs.length > 0) {
                for (Class<?> itf : itfs) {
                    // neu interface da co implementation khac thi canh bao, giu lai ban dau tien
                    if (IMPLEMENTATIONS.containsKey(itf)) {
                        continue;
                    }
                    IMPLEMENTATIONS.put(itf, impl);
                }
            }
            // dong thoi luu chinh impl de co the get(ImplClass.class)
            IMPLEMENTATIONS.putIfAbsent(impl, impl);
        } catch (Exception e) {
            throw new RuntimeException("[DI] Lỗi registerClass " + impl.getName() + ": " + e.getMessage(), e);
        }
    }

    // --------------------- Public API ---------------------

    // lay bean tu container theo interface hoac implementation class
    // tra ve singleton instance (tao moi neu chua co)
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> type) {
        try {
            // neu khong co mapping cho interface va interface do chua co impl thi tra ve null (cho phep @Autowired(required=false))
            if (!IMPLEMENTATIONS.containsKey(type) && type.isInterface()) {
                return null;
            }

            // resolve implementation: neu type la interface thi tim implementation tuong ung
            final Class<?> impl = IMPLEMENTATIONS.getOrDefault(type, type);

            // neu instance da tao san thi tra ve luon
            if (SINGLETONS.containsKey(impl)) {
                return (T) SINGLETONS.get(impl);
            }

            // tao va inject dependencies (co kiem soat vong lap)
            Object instance = createAndInjectSafely(impl);

            // luu vao cache dam bao singleton
            SINGLETONS.put(impl, instance);

            return (T) instance;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException("[DI] Lỗi tạo bean cho " + type.getName() + ": " + e.getMessage(), e);
        }
    }

    // --------------------- Create & inject helpers ---------------------

    // Tạo instance + inject có kiểm tra vòng phụ thuộc
    private static Object createAndInjectSafely(Class<?> impl) {
        Set<Class<?>> stack = BUILD_STACK.get();

        // Nếu impl đang được tạo ở cấp cao hơn trong thread -> vòng phụ thuộc
        if (stack.contains(impl)) {
            StringBuilder pathBuilder = new StringBuilder();
            int index = 0;
            for (Class<?> clazz : stack) {
                if (index > 0) pathBuilder.append(" -> ");
                pathBuilder.append(clazz.getSimpleName());
                index++;
            }
            String path = pathBuilder.toString();
            throw new RuntimeException("[DI] Circular dependency phát hiện: " + path + " -> " + impl.getSimpleName());
        }

        // Đánh dấu đang khởi tạo
        stack.add(impl);
        try {
            Object instance = createInstance(impl); // constructor injection
            injectDependencies(instance);           // field injection
            return instance;
        } finally {
            // Dọn flag dù succeed hay fail
            stack.remove(impl);
            if (stack.isEmpty()) {
                BUILD_STACK.remove();
            }
        }
    }

    /**
     * Tạo instance bằng cách:
     *  1) Thử constructor không tham số.
     *  2) Nếu không có, thử constructor khác và resolve param bằng get(paramType).
     *  3) Nếu không thể tạo => ném RuntimeException.
     */
    private static Object createInstance(Class<?> impl) {
        try {
            Constructor<?> noArgs = impl.getDeclaredConstructor();
            noArgs.setAccessible(true);
            return noArgs.newInstance();
        } catch (NoSuchMethodException ignore) {
            // Không có ctor không tham số, tiếp tục thử ctor khác
        } catch (Exception e) {
            throw new RuntimeException("[DI] Lỗi gọi ctor mặc định của " + impl.getName() + ": " + e.getMessage(), e);
        }

        // Thử các constructor có tham số (constructor injection)
        Constructor<?>[] ctors = impl.getDeclaredConstructors();
        for (Constructor<?> ctor : ctors) {
            try {
                Class<?>[] paramTypes = ctor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = get(paramTypes[i]); // resolve param
                }

                ctor.setAccessible(true);
                return ctor.newInstance(params);
            } catch (Exception ignored) {
                // Nếu ctor không resolve được thì thử ctor khác
            }
        }

        throw new RuntimeException("[DI] Không thể tạo instance cho: " + impl.getName());
    }

    /**
     * Tiêm các field có annotation @Autowired. 
     * Duyệt cả superclass để inject field protected/private kế thừa.
     * Nếu annotation có required=true mà dependency không resolve được thì ném exception.
     */
    private static void injectDependencies(Object bean) {
        Class<?> c = bean.getClass();
        while (c != null && c != Object.class) {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(Autowired.class)) {
                    f.setAccessible(true);
                    Class<?> depType = f.getType();

                    // Gọi get() để resolve dependency
                    Object dependency = get(depType);

                    Autowired ann = f.getAnnotation(Autowired.class);
                    if (dependency == null && ann.required()) {
                        throw new RuntimeException("[DI] Không thể inject dependency cho field "
                                + f.getName() + " của " + bean.getClass().getName());
                    }

                    try {
                        f.set(bean, dependency);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("[DI] Không set được field " + f.getName()
                                + " của " + bean.getClass().getName() + ": " + e.getMessage(), e);
                    }
                }
            }
            c = c.getSuperclass();
        }
    }

    // --------------------- Classpath scanning (simple impl) ---------------------

    /**
     * Lấy toàn bộ Class<?> nằm trong các package được liệt kê.
     * Nếu BASE_PACKAGE rỗng -> dùng danh sách mặc định: {"service.impl", "dao.impl", "mapper"}.
     * Hoạt động tốt khi project chạy trong IDE hoặc exploded WAR (WEB-INF/classes).
     */
    private static Set<Class<?>> getAllClassesInPackage(String packageName) {
        Set<Class<?>> result = new HashSet<>();
        try {
            // Danh sách package cần scan — có thể thay đổi theo project
            String[] packages = packageName.isEmpty()
                    ? new String[]{"service.impl", "dao.impl", "mapper"}
                    : new String[]{packageName};

            for (String pkg : packages) {
                String path = pkg.replace('.', '/');
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                URL root = cl.getResource(path);
                if (root != null) {
                    File dir = new File(root.getFile());
                    if (dir.exists()) {
                        findClassesInDirectory(result, dir, pkg, cl);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("[DI] Lỗi scan packages: " + e.getMessage(), e);
        }
        return result;
    }

    // Đệ quy duyệt thư mục, load .class (không khởi tạo static block)
    private static void findClassesInDirectory(Set<Class<?>> out, File dir, String pkg, ClassLoader cl) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                findClassesInDirectory(out, f, pkg + "." + f.getName(), cl);
            } else if (f.getName().endsWith(".class")) {
                String simple = f.getName().substring(0, f.getName().length() - 6);
                // Bỏ qua inner/anonymous classes
                if (simple.matches(".*\\$\\d+")) continue;

                String fqcn = pkg + "." + simple;
                try {
                    Class<?> clazz = Class.forName(fqcn, false, cl);
                    out.add(clazz);
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
    }

    /** Cho phép đăng ký thủ công: interface -> implementation */
    public static void register(Class<?> interfaceClass, Class<?> implementationClass) {
        IMPLEMENTATIONS.put(interfaceClass, implementationClass);
    }
}
