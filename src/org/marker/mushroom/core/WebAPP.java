package org.marker.mushroom.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.model.ContentModelContext;
import org.marker.mushroom.model.IContentModelParse;
import org.marker.mushroom.template.MyCMSTemplate;

/**
 * 前台处理核心对象
 * 
 * 应用控制类，完成网址解析，单一入口控制，静态页面缓存功能
 * 
 * @author marker
 * */

public final class WebAPP {

	// 日志记录器
	private static final Log log = LogFactory.getLog(WebAPP.class);

	/** 请求响应相关的对象 */
	private HttpServletRequest request;
	private HttpServletResponse response; 


	/** 系统配置信息 */
	private static final SystemConfig syscfg = SystemConfig.getInstance();
	/** 模版引擎 */
	private static MyCMSTemplate cmstemplate;
	/** 内容模型工厂 */
	private static ContentModelContext cmc; 
	
	
	
	/**
	 * 安装状态（注意：容器初始化赋值）
	 * @see org.marker.mushroom.holder.MushRoomInitBuildHolder
	 */
	public static boolean install = false;
	
	
	
	/**
	 * 创建APP实例 
	 * @return WebAPP
	 */
	public static WebAPP newInstance() {
		return new WebAPP();
	} 
	
	
	
	/**
	 * 私有构造方法 ，避免外部被实例化
	 */
	private WebAPP() {
		this.request     = ActionContext.getReq();
		this.response    = ActionContext.getResp();
//		this.application = ActionContext.getApplication();
		 
		
		//初始化模板引擎
		if(WebAPP.cmstemplate == null)
			WebAPP.cmstemplate = SpringContextHolder.getBean(SystemStatic.SYSTEM_CMS_TEMPLATE); 
		//初始化模型工厂
	    if(cmc == null){
			cmc =  ContentModelContext.getInstance(); 
		}
	}
	
	


	
	/**
	 * 启动App
	 * */
	public void start() {
		
		
		/* ====================================================
		 *                 检查系统是否安装
		 * ====================================================
		 */
		if ( !install ) { 
			try {
				log.info("mrcms not install!");
				response.sendRedirect("install/index.jsp");// 没有安装则进入安装页面
				return; // 处理完毕直接返回。
			} catch (IOException e) {
				log.error(e);
			}
		}
	
		
		/* ====================================================
		 *                        採集碎片
		 * ====================================================
		 */
		IChip chipData = SpringContextHolder.getBean(SystemStatic.SYSTEM_CMS_CHIP);
		request.setAttribute(AppStatic.WEB_APP_CHIP, chipData.getVector());
		
		
		WebParam param = WebParam.get();// 解析地址 
		try { 
			// 解析内容模型
			// 这里将模式分为：重定向模型|内容模型
			int statusCode = cmc.parse(param);
			switch (statusCode) {
			case IContentModelParse.STATUS_REDIRECT: //如果是重定向，不是依赖内容模型
				response.sendRedirect(param.redirect);
				return;
			case IContentModelParse.STATUS_MODULE: //内容模型
				cmstemplate.proxyCompile(param.template);// 代理编译
				cmstemplate.sendModeltoView(param.template);
				break;
			default:
				response.sendError(404); return;//页面肯定不存在
			}
		}catch(SystemException syse){ 
			handleErrorMessage(syse); 
		}catch (FileNotFoundException e) {
			log.error("", e); 
		} catch (IOException e) {
			log.error("", e); 
		}
		this.destory();
	}

	
	/**
	 * 发送错误消息
	 * @param errorCode
	 * @throws org.marker.mushroom.core.exception.SystemException  
	 */
	public void handleErrorMessage(SystemException e) {
		String errorTemplate = syscfg.get("error_page"); 
//		String errorMessage = ErrorConfig.getInstance().get(errorCode);
		 
		
		
		this.request.setAttribute("err_code","MR-".concat("0000"));
		this.request.setAttribute("error",e.getErrMessage());
		
		File errorFile = new File(WebRealPathHolder.REAL_PATH + syscfg.get("themes_path") + errorTemplate);
		if(!errorFile.exists()){ 
			try {
				Writer out = response.getWriter();
				out.write(e.renderHTML());
				out.flush(); out.close();
			} catch (IOException e2) { 
				log.error(e2); 
			}
		} else {
			try {
				cmstemplate.proxyCompile(errorTemplate);
				cmstemplate.sendModeltoView(errorTemplate);
			} catch (Exception e3) { 
				log.error(e3); 
			}
		}
		
		this.destory();
	}
	
	
	
	
	/**
	 * 销毁App
	 * */
	public void destory() {
		//清除当前请求对象和响应对象
		ActionContext.remove();
	}

}
