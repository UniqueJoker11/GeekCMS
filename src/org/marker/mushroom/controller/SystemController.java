package org.marker.mushroom.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.support.SupportController;
import org.marker.security.Base64;
import org.marker.security.DES;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * RushRoom系统控制器
 * @author marker
 * */
@Controller
@RequestMapping("/admin/system")
public class SystemController extends SupportController {

	private final static Log log = LogFactory.getLog(SystemController.class);
	
	
	
	public SystemController() {
		this.viewPath = "/admin/system/";
	}
	 
	
	//网站基本信息
	@RequestMapping("/siteinfo")
	public String siteinfo(HttpServletRequest request){
		SystemConfig config = SystemConfig.getInstance();
		request.setAttribute("config", config.getProperties());
		return this.viewPath + "siteinfo";
	}
	
	
	//保存网站配置信息
	@ResponseBody
	@RequestMapping("/saveinfo")
	public Object saveinfo(HttpServletRequest request){
		SystemConfig config = SystemConfig.getInstance();
		try{
			/* 系统基本信息配置 */
			config.set("title", request.getParameter("config.title"));//网站标题
			config.set("url", request.getParameter("config.url"));//网站地址
			config.set("keywords", request.getParameter("config.keywords"));//网站关键字
			config.set("description", request.getParameter("config.description"));//网站描述
			config.set("mastermail", request.getParameter("config.mastermail"));//管理员邮箱
			config.set("copyright", request.getParameter("config.copyright"));//版权信息
			config.set("icp", request.getParameter("config.icp"));//ICP备案
			
			/* 主题配置 */
			config.set("index_page", request.getParameter("config.index_page"));//网站首页
			config.set("error_page", request.getParameter("config.error_page"));//错误模版
			config.set("themes_path", request.getParameter("config.themes_path"));//主题路径
			config.set("themes_cache", request.getParameter("config.themes_cache"));//主题缓存目录
			config.set("dev_mode", request.getParameter("config.dev_mode"));//是否开发模式
			config.set("gzip", request.getParameter("config.gzip"));//GZIP
			
			config.store();//修改配置信息状态
			return new ResultMessage(true, "更新成功!");
		}catch (Exception e) {
			return new ResultMessage(false, "更新失败!");
		} 
	}
	
	

	
	
	/**
	 * SEO设置
	 * */
	@RequestMapping("/seoinfo")
	public String seoinfo(HttpServletRequest request){
		URLRewriteConfig urlRewriteConfig =  URLRewriteConfig.getInstance();
		request.setAttribute("urlConfig", urlRewriteConfig.getProperties());
		return this.viewPath + "seoinfo";
	}
	
	@ResponseBody
	@RequestMapping("/saveseoinfo")
	public Object saveseoinfo(HttpServletRequest request){
		URLRewriteConfig urlRewriteConfig =  URLRewriteConfig.getInstance();
		try{
			String channelRule = request.getParameter("url.channel");
			String contentRule = request.getParameter("url.content");
			String pageRule    = request.getParameter("url.page");
			
			urlRewriteConfig.set("channel", channelRule);
			urlRewriteConfig.set("content", contentRule);
			urlRewriteConfig.set("page", pageRule);
			urlRewriteConfig.store();
			return new ResultMessage(true, "更新成功!");
		}catch (Exception e) {
			log.error("更新url重写失败了!", e);
			return new ResultMessage(true, "更新失败!");
		}
	}
	
	
	/**
	 * Mail配置
	 * */
	@RequestMapping("/mailinfo")
	public String mailinfo(HttpServletRequest request){ 
		return this.viewPath + "mailinfo";
	} 
	
	
	
	/**
	 * 进入数据库配置
	 * @param request
	 * @return
	 */
	@RequestMapping("/dbinfo")
	public ModelAndView dbinfo(HttpServletRequest request){
		ModelAndView view = new ModelAndView(this.viewPath + "dbinfo");
		DataBaseConfig dbconfig = DataBaseConfig.getInstance();
		Properties config = (Properties) dbconfig.getProperties().clone();
 
		String pass = config.getProperty("mushroom.db.pass");
		
		String desPass = getDesCode(pass);
		config.setProperty("mushroom.db.pass", desPass);
		
		view.addObject("sql", config);
		return view;
	}
	/**
	 * 获取Des加密结果
	 * */
	private String getDesCode(String pass){
		String key = SystemConfig.getInstance().get("secret_key");//网站秘钥，这是在安装的时候获取的
		try {
			return Base64.encode(DES.encrypt(pass.getBytes(), key));
		} catch (Exception e) { e.printStackTrace();}
		return pass;
	}
	
	
	
	/** 保存数据库配置  */
	@ResponseBody
	@RequestMapping("/savedbinfo")
	public Object savedbinfo(HttpServletRequest request){
		DataBaseConfig config = DataBaseConfig.getInstance();
		String oldPass = config.get("mushroom.db.pass");
		String newpass = request.getParameter("sql.pass");
		
		if(!getDesCode(oldPass).equals(newpass)){//修改密码了
			oldPass = newpass;
		}
		
		try{
			//数据库连接配置信息
			config.set("mushroom.db.host", request.getParameter("sql.host"));
			config.set("mushroom.db.port", request.getParameter("sql.port"));
			config.set("mushroom.db.demo", request.getParameter("sql.demo"));
			config.set("mushroom.db.char", request.getParameter("sql.char"));
			config.set("mushroom.db.debug", request.getParameter("sql.debug"));
//			config.set("mushroom.db.prefix", request.getParameter("sql.prefix"));
			config.set("mushroom.db.driver", request.getParameter("sql.driver"));
			config.set("mushroom.db.user", request.getParameter("sql.user"));
			config.set("mushroom.db.pass", oldPass);
			
			//数据库连接池配置信息
			config.set("c3p0.initialPoolSize", request.getParameter("sql.initialPoolSize"));
			config.set("c3p0.minPoolSize", request.getParameter("sql.minPoolSize"));
			config.set("c3p0.maxPoolSize", request.getParameter("sql.maxPoolSize"));
			config.set("c3p0.acquireIncrement", request.getParameter("sql.acquireIncrement"));
			config.set("c3p0.maxIdleTime", request.getParameter("sql.maxIdleTime"));
			config.set("c3p0.maxStatements", request.getParameter("sql.maxStatements"));
			
			config.store();//持久化配置信息
			return new ResultMessage(true, "修改成功! 重启服务器生效!");
		}catch (Exception e) {
			log.error("save db config faild!", e);
		}
		return new ResultMessage(false, "更新失败!");
	}
	
	
	

}
