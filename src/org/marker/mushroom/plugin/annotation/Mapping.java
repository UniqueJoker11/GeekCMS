package org.marker.mushroom.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.marker.mushroom.plugin.FormMethod;




/**
 *  URL路径映射
 *  value 默认 "/"
 *  method 默认 GET
 * @author marker
 * */
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target({ElementType.METHOD})//方法注解
public @interface Mapping {
	String value() default "/";// URL
	FormMethod method() default FormMethod.GET;// 请求方法
}
