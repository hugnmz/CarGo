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
 * Phiên bản này implement các chức năng:
 *  - Scan một số package cụ thể để tìm các class được đánh dấu
 *    (@Repository, @Service, @Component).
 *  - Map interface -> implementation (IMPLEMENTATIONS)
 *  - Tạo singleton instance cho mỗi implementation (SINGLETONS)
 *  - Hỗ trợ constructor injection (ưu tiên ctor không tham số, sau
 *    đó thử các ctor có tham số và resolve param bằng get(...))
 *  - Hỗ trợ field injection với @Autowired (inject cả superclass fields)
 *  - Phát hiện vòng phụ thuộc (circular dependency) trên từng thread
 *
 * Lưu ý thiết kế / usage notes:
 *  - Đặt đúng package list trong getAllClassesInPackage (mặc định: "service.impl", "dao.impl", "mapper")
 *  - Container tạo các instance dạng singleton theo implementation class.
 *  - Nếu một interface có nhiều implementation, registerClass sẽ ghi đè theo thứ tự scan.
 *  - Có thể gọi DIContainer.register(interface, impl) để ghim thủ công ánh xạ.
 *  - Tránh scan toàn bộ classpath trong runtime production — tốt hơn là cấu hình cụ thể hoặc build-time generation.
 */
public final class DIContainer {

    // Không cho tạo instance của container
    private DIContainer() {}

    // Nếu muốn scan toàn bộ project thì có thể set lại BASE_PACKAGE;
    // Trong implementation hiện tại getAllClassesInPackage sử dụng 1 mảng packages cứng (xem bên dưới)
    private static final String BASE_PACKAGE = "";

    // Cache singletons: key = implementation Class, value = instance
    // Dùng ConcurrentHashMap để an toàn với multi-thread (web container nhiều thread)
    private static final Map<Class<?>, Object> SINGLETONS = new ConcurrentHashMap<>();

    // Map interface -> implementation class
    // Khi gọi get(SomeInterface.class) container sẽ lookup IMPLEMENTATIONS.get(SomeInterface.class)
    private static final Map<Class<?>, Class<?>> IMPLEMENTATIONS = new ConcurrentHashMap<>();

    // Thread-local set để theo dõi stack đang build, dùng để phát hiện vòng phụ thuộc
    // Sử dụng Set (không cho phép duplicate) để kiểm tra nhanh contains()
    private static final ThreadLocal<Set<Class<?>>> BUILD_STACK = ThreadLocal.withInitial(HashSet::new);

    // Static block sẽ chạy 1 lần khi class DIContainer được load lần đầu
    // Mục đích: tự động scan + register các class được đánh dấu
    static {
        autoScan();
    }

    // --------------------- Auto-scan & register ---------------------

    // Tự quét các package được định nghĩa để đăng ký các implementation
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
            // Không bung ứng dụng chỉ vì lỗi scan — log và tiếp tục
            System.err.println("[DI] Lỗi autoScan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Đăng ký implementation: ánh xạ tất cả interface mà impl implements
    private static void registerClass(Class<?> impl) {
        try {
            Class<?>[] itfs = impl.getInterfaces();
            if (itfs != null && itfs.length > 0) {
                for (Class<?> itf : itfs) {
                    // Ghi đè mapping interface->impl nếu đã tồn tại
                    IMPLEMENTATIONS.put(itf, impl);
                }
            }
            // Đồng thời lưu chính impl để có thể get(ImplClass.class) trả về chính nó
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
            // Resolve implementation: nếu type là interface, tìm implementation tương ứng
            final Class<?> impl = IMPLEMENTATIONS.getOrDefault(type, type);

            // Nếu instance đã tạo sẵn thì trả về luôn
            if (SINGLETONS.containsKey(impl)) {
                return (T) SINGLETONS.get(impl);
            }

            // Tạo và inject dependencies (có kiểm soát vòng lặp)
            Object instance = createAndInjectSafely(impl);

            // Lưu vào cache để đảm bảo singleton
            SINGLETONS.put(impl, instance);

            return (T) instance;
        } catch (RuntimeException ex) {
            throw ex; // ném nguyên vẹn runtime exception để caller thấy thông điệp
        } catch (Exception e) {
            throw new RuntimeException("[DI] Lỗi tạo bean cho " + type.getName() + ": " + e.getMessage(), e);
        }
    }

    // --------------------- Create & inject helpers ---------------------

    // Tạo instance + inject có kiểm tra vòng phụ thuộc
    private static Object createAndInjectSafely(Class<?> impl) {
        Set<Class<?>> stack = BUILD_STACK.get();

        // Nếu impl đang được tạo ở một cấp cao hơn trong cùng thread => circular dependency
        if (stack.contains(impl)) {
            throw new RuntimeException("[DI] Circular dependency phát hiện: " + impl.getSimpleName());
        }

        // Đánh dấu đang khởi tạo
        stack.add(impl);
        try {
            Object instance = createInstance(impl); // constructor injection
            injectDependencies(instance);           // field injection (@Autowired)
            return instance;
        } finally {
            // Dọn flag ở cuối dù succeed hay fail
            stack.remove(impl);
            if (stack.isEmpty()) {
                BUILD_STACK.remove(); // tránh memory leak của ThreadLocal
            }
        }
    }

    /**
     * Tạo instance bằng cách:
     *  1) Thử constructor không tham số (no-args ctor)
     *  2) Nếu không có, thử mọi constructor khác, resolve tham số bằng get(paramType)
     *  3) Nếu không thể tạo => ném RuntimeException
     */
    private static Object createInstance(Class<?> impl) {
        try {
            Constructor<?> noArgs = impl.getDeclaredConstructor();
            noArgs.setAccessible(true);
            return noArgs.newInstance();
        } catch (NoSuchMethodException ignore) {
            // Không có constructor không tham số, tiếp tục thử các ctor khác
        } catch (Exception e) {
            throw new RuntimeException("[DI] Lỗi gọi ctor mặc định của " + impl.getName() + ": " + e.getMessage(), e);
        }

        // Thử constructor có tham số (constructor injection)
        Constructor<?>[] ctors = impl.getDeclaredConstructors();
        for (Constructor<?> ctor : ctors) {
            try {
                Class<?>[] paramTypes = ctor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];

                // Resolve param bằng container (đệ quy)
                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = get(paramTypes[i]);
                }

                ctor.setAccessible(true);
                return ctor.newInstance(params);
            } catch (Exception ignored) {
                // Nếu ctor hiện tại không thể resolve (thiếu dependency) => thử ctor khác
            }
        }

        throw new RuntimeException("[DI] Không thể tạo instance cho: " + impl.getName());
    }

    /**
     * Tiêm các field có annotation @Autowired. 
     * Duyệt cả superclass để inject các field protected/private kế thừa.
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

                    // Gọi get() để resolve dependency (có thể tạo mới)
                    Object dependency = get(depType);

                    // Nếu annotation required và dependency trả về null (hiếm) => lỗi
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
            c = c.getSuperclass(); // lên lớp cha để xử lý field kế thừa
        }
    }

    // --------------------- Classpath scanning (simple impl) ---------------------

    /**
     * Lấy toàn bộ Class<?> nằm trong các package được liệt kê.
     * Lưu ý: cách này chỉ hoạt động khi class files có trên file system (IDE / exploded WAR),
     * không đảm bảo hoạt động khi package được đóng gói trong jar nếu đường dẫn classloader khác.
     * Nếu deploy trong WAR, đảm bảo các .class ở bên trong WEB-INF/classes để cl.getResource trả về file URL.
     */
    private static Set<Class<?>> getAllClassesInPackage(String packageName) {
        Set<Class<?>> result = new HashSet<>();
        try {
            // Danh sách package cần scan — thay đổi theo project
            String[] packages = {"service.impl", "dao.impl", "mapper"};

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

    // Đệ quy duyệt thư mục, load .class (không khởi tạo static block vì Class.forName(..., false, cl))
    private static void findClassesInDirectory(Set<Class<?>> out, File dir, String pkg, ClassLoader cl) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                findClassesInDirectory(out, f, pkg + "." + f.getName(), cl);
            } else if (f.getName().endsWith(".class")) {
                String simple = f.getName().substring(0, f.getName().length() - 6);
                // Bỏ qua inner/anonymous classes dạng Outer$1.class
                if (simple.matches(".*\\$\\d+")) continue;

                String fqcn = pkg + "." + simple;
                try {
                    // load class nhưng không khởi tạo static block
                    Class<?> clazz = Class.forName(fqcn, false, cl);
                    out.add(clazz);
                } catch (ClassNotFoundException ignored) {
                    // Nếu không load được thì bỏ qua
                }
            }
        }
    }

    /** Allow manual registration: interface -> implementation */
    public static void register(Class<?> interfaceClass, Class<?> implementationClass) {
        IMPLEMENTATIONS.put(interfaceClass, implementationClass);
    }
}
