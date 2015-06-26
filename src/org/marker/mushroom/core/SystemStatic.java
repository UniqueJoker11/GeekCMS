package org.marker.mushroom.core;


/**
 * 系统相关的常量
 * */
public interface SystemStatic {
	
	
	/** 模型工厂Bean名称 */
	String FACTORY_MODULE = "moduleFactory";
	
	/** 插件工厂Bean名称 */
	String SYSTEM_PLUGIN_FACTORY = "pluginFactory";
	
	/** CMS模板引擎Bean名称 */
	String SYSTEM_CMS_TEMPLATE = "cmstemplate";
	
	/** CMS碎片组件Bean名称 */
	String SYSTEM_CMS_CHIP = "chipContext";
	
	
	/* -------------------------------------------------
	 *                服务层Bean变量名称
	 * ------------------------------------------------- */
	 
	/** 栏目查询服务层 */
	String SYSTEM_CHANNEL_SERVICE = "channelService";
	
 
	/** 内容模型服务层 */
	String SERVICE_MODULE = "moduleManagement";
	
	
	
	/** 通用DaoBean */
	String DAO_COMMON = "commonDao";
	
	/** 内容模型DaoBean */
	String DAO_MODULE = "moduleDao";
	
	/** 内容模型日志DaoBean */
	String DAO_MODULE_LOG = "moduleLogDao";
	
	/** 栏目DaoBean */
	String DAO_CHANNEL = "channelDao";

	/** 插件DaoBean */
	String DAO_PLUGIN = "pluginDao";

	/** 用户登录日志DaoBean */
	String DAO_USER_LOGIN_DAO = "userLoginLogDao";
	
	/** 权限 DaoBean */
	String DAO_PERMISSION = "permissionDao";
	
	
}
