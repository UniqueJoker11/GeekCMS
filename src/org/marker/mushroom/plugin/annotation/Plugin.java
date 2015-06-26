package org.marker.mushroom.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




/**
 * 插件名称配置，与URL路径相关,插件必须配置
 * @author marker
 * */
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target({ElementType.TYPE})//类注解
public @interface Plugin {
	String value();// URL 
}
