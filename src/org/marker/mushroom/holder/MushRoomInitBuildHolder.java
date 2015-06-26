package org.marker.mushroom.holder;

import java.io.File;

import javax.servlet.ServletContext;

import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebAPP;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.core.proxy.SingletonProxyKeyWordComputer;
import org.springframework.web.context.ServletContextAware;



/**
 * 环境初始化构建
 * 
 * 
 * @author marker
 * */
public class MushRoomInitBuildHolder implements ServletContextAware{

	
	@Override
	public void setServletContext(ServletContext application) {
    	String webRootPath = application.getRealPath(File.separator);//网站根目录路径
    	
    	WebAPP.install = isInstall(webRootPath);// 设置系统是否被安装
    	 
    	
    	
		/* 
		 * ============================================================
		 *          ActionContext bind (application)应用作用域
		 * ============================================================
		 */ 	
    	ActionContext.currentThreadBindServletContext(application);
    	
    	
    	
    	
		/* 
		 * ============================================================
		 *                初始化系统配置信息路径
		 * ============================================================
		 */
    	SystemConfig systemConfig = SystemConfig.getInstance();
    	application.setAttribute(AppStatic.WEB_APP_CONFIG, systemConfig.getProperties());
    	
    	
		/* 
		 * ============================================================
		 *               URLRewrite 初始化URL规则
		 * ============================================================
		 */
    	URLRewriteConfig.getInstance();
    	
    	
    	/* 
		 * ============================================================
		 *               关键字提取代理，初始化(避免懒加载带来的等待)
		 * ============================================================
		 */
    	SingletonProxyKeyWordComputer.init(webRootPath);// 初始化dic
    	SingletonProxyKeyWordComputer.getInstance();
    	
    	/* 
		 * ============================================================
		 *               PluginFactory 初始化
		 * ============================================================
		 */
	 
    	
    	
    	
    	
//		if(isInstall){
//			DataBaseConfig dbc = DataBaseConfig.getInstance();
//			C3p0DataSourceProvide c3p0 =  new C3p0DataSourceProvide(dbc.getDatabase());
//			EngineKit.setDataSource(c3p0.getDataSource());
//	    	//初始化插件工厂
//	    	YLPluginFactory.ini(application);
//	    	
//	    	//模型初始化
//	    	ModuleFactory.init();
//		}
    	
    	
    	
    	
		/* 
		 * ============================================================
		 *               内容模型类加载器  Testing...
		 * ============================================================
		 */ 
    	//test
//    	try {
//    		Class<?> clzz = loader.loadClass("org.marker.mushroom.module.impl.ArticleModule");
//			System.out.println(clzz); 
//		} catch (ClassNotFoundException e) { 
//			e.printStackTrace();
//		}
    	
    	
    	
		 
	}
	
	
	/**
	 * 判断是否是否已安装(true:安装 false:未安装)
	 * @return boolean 状态
	 * */
	private boolean isInstall(String webRootPath){
		return new File(webRootPath + "data" + File.separator + "install.lock").exists();
	}

}
