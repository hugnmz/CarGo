/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package util.di.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//đánh dấu class là 1 component dc quản lí bởi DI
@Target(ElementType.TYPE) // dc gắn vào class, interface hay enum
@Retention(RetentionPolicy.RUNTIME) //tồn tại khi chương trình chạy runtime
public @interface Component {
    String value() default "";
}