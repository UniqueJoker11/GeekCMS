package org.marker.mushroom.plugin;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.marker.mushroom.context.ActionContext;


/**
 * Web插件
 * 插件let主要提供类似Servlet的功能
 * 
 * @author marker
 * @version 1.0
 */
public abstract class Pluginlet {

	
	
	/**
	 * 获取HttpServletRequest 请求对象
	 * @return
	 */
	public HttpServletRequest getServletRequest(){
		return ActionContext.getReq();
	}
	
	
	/**
	 * 获取HttpServletResponse 响应对象
	 * @return
	 */
	public HttpServletResponse getServletResponse(){
		return ActionContext.getResp();
	}
	
	
	/**
	 * 获取当前会话(Session)对象
	 * @return
	 */
	public HttpSession getSession(){
		return ActionContext.getReq().getSession();
	}
	
	
	/**
	 * 获取当前会话(Session)对象
	 * @param boolean 是否创建新的Session
	 * @return
	 */
	public HttpSession getSession(boolean is){
		return ActionContext.getReq().getSession(is);
	}
	
	
	/**
	 * 获取ServletContext
	 * @return
	 */
	public ServletContext getServletContext(){
		return ActionContext.getApplication(); 
	}
	
}
