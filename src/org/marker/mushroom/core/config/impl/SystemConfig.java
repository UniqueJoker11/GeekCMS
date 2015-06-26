package org.marker.mushroom.core.config.impl;

import java.io.IOException;

import org.marker.mushroom.core.config.ConfigEngine;
 

/**
 * 系统配置类（对Properties进行了简单封装）
 * 用于配置系统配置文件，提供读取和保存两种持久化操作
 * 在系统StartListener监听器中进行配置文件地址的初始化
 * @author marker
 * */
public final class SystemConfig extends ConfigEngine{

 
	// 配置文件路径
	public static final String CONFIG_FILE_PATH = "/config/site/system.config";
	
	
	
	/**
	 * 默认构造方法
	 * @throws IOException 
	 * */
	private SystemConfig() {
		super(CONFIG_FILE_PATH); 
	}
	
	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static class SingletonHolder {
		public final static SystemConfig instance = new SystemConfig();     
	}
	
	
	/**
	 * 获取系统配置实例
	 * */
	public static SystemConfig getInstance(){
		return SingletonHolder.instance;
	}
	
	
	/**
	 * 配置文件中属性名称配置
	 * */
	public interface Names{
		/** 关键字 */
		String KEYWORDS = "keywords";
		/** 描述信息 */
		String DESCRIPTION = "description";
		
		
		
	}


	/**
	 * 开发模式
	 * @return
	 */
	public boolean isdevMode() {
		String devmode = (String) this.properties.get("dev_mode");
		if(devmode != null){
			return Boolean.valueOf(devmode);
		}
		return false;
	}
}
