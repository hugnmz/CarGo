package util.di.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// annotation danh dau class la 1 component duoc quan li boi di
@Target(ElementType.TYPE) // duoc gan vao class, interface hay enum
@Retention(RetentionPolicy.RUNTIME) // ton tai khi chuong trinh chay runtime
public @interface Component {
    String value() default "";
}