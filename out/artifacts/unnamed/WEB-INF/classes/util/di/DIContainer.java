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

/**
 * DIContainer - lightweight Dependency Injection container
 * ------------------------------------------------------
 * Phiên bản nâng cấp (2025) gồm các cải tiến:
 *  - Quét package linh hoạt hơn (dùng BASE_PACKAGE nếu được set).
 *  - Cảnh báo khi có nhiều implementation cho cùng interface.
 *  - Báo lỗi rõ ràng nếu inject interface chưa đăng ký implementation.
 *  - In rõ chuỗi dependency khi phát hiện vòng phụ thuộc.
 *  - Hỗ trợ @Autowired(required = false) hoạt động đúng.
 *  - Giữ nguyên toàn bộ logic cũ để đảm bảo tương thích.
 */
public final class DIContainer {

    // Không cho tạo instance của container
    private DIContainer() {}

    // Có thể set lại BASE_PACKAGE để quét theo package tùy chọn.
    // Nếu rỗng -> dùng danh sách mặc định bên dưới.
    private static final String BASE_PACKAGE = "";

    // Cache singletons: key = implementation Class, value = instance
    private static final Map<Class<?>, Object> SINGLETONS = new ConcurrentHashMap<>();

    // Map interface -> implementation class
    private static final Map<Class<?>, Class<?>> IMPLEMENTATIONS = new ConcurrentHashMap<>();

    // Thread-local set để theo dõi stack đang build, phát hiện vòng phụ thuộc
    private static final ThreadLocal<Set<Class<?>>> BUILD_STACK = ThreadLocal.withInitial(HashSet::new);

    // Static block chạy khi class được load lần đầu -> tự động scan + register
    static {
        autoScan();
    }

    // --------------------- Auto-scan & register ---------------------

    // Quét các package được định nghĩa để đăng ký các implementation
    private static void autoScan() {
        try {
            Set<Class<?>> classes = getAllClassesInPackage(BASE_PACKAGE);
            for (Class<?> clazz : classes) {
                // Nếu class có annotation đánh dấu là bean thì register
                if (clazz.isAnnotationPresent(Repository.class)
                        || clazz.isAnnotationPresent(Service.class)
                        || clazz.isAnnotationPresent(Component.class)) {
                    registerClass(clazz);
                }
            }
        } catch (Exception e) {
            System.err.println("[DI] Lỗi autoScan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Đăng ký implementation: ánh xạ interface -> implementation class
    private static void registerClass(Class<?> impl) {
        try {
            Class<?>[] itfs = impl.getInterfaces();
            if (itfs != null && itfs.length > 0) {
                for (Class<?> itf : itfs) {
                    // Nếu interface đã có implementation khác -> cảnh báo, giữ lại bản đầu tiên
                    if (IMPLEMENTATIONS.containsKey(itf)) {
                        System.err.println("[DI] Cảnh báo: interface " + itf.getName()
                                + " có nhiều implementation (" + IMPLEMENTATIONS.get(itf).getName()
                                + " và " + impl.getName() + "). Giữ lại implementation đầu tiên.");
                        continue;
                    }
                    IMPLEMENTATIONS.put(itf, impl);
                }
            }
            // Đồng thời lưu chính impl để có thể get(ImplClass.class)
            IMPLEMENTATIONS.putIfAbsent(impl, impl);
        } catch (Exception e) {
            System.err.println("[DI] Lỗi registerClass " + impl.getName() + ": " + e.getMessage());
        }
    }

    // --------------------- Public API ---------------------

    /**
     * Lấy bean từ container theo interface hoặc implementation class.
     * Trả về singleton instance (tạo mới nếu chưa có).
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> type) {
        try {
            // Nếu không có mapping cho interface và interface đó chưa có impl => trả về null (cho phép @Autowired(required=false))
            if (!IMPLEMENTATIONS.containsKey(type) && type.isInterface()) {
                return null;
            }

            // Resolve implementation: nếu type là interface -> tìm implementation tương ứng
            final Class<?> impl = IMPLEMENTATIONS.getOrDefault(type, type);

            // Nếu instance đã tạo sẵn -> trả về luôn
            if (SINGLETONS.containsKey(impl)) {
                return (T) SINGLETONS.get(impl);
            }

            // Tạo và inject dependencies (có kiểm soát vòng lặp)
            Object instance = createAndInjectSafely(impl);

            // Lưu vào cache đảm bảo singleton
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
            String path = String.join(" -> ", stack.stream().map(Class::getSimpleName).toList());
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
            System.err.println("[DI] Lỗi scan packages: " + e.getMessage());
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
