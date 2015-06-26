package org.marker.mushroom.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




/**
 * 内容模型注解
 * @author marker
 * */
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target({ElementType.TYPE})//类注解
public @interface Model {
	String name();// 姓名
	String type();// URL 
	String author();// URL 
	String version();// URL 
	String description();// URL 
	String template() default "template.html";
}
