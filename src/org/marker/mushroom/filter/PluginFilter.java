package org.marker.mushroom.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.plugin.FormMethod;
import org.marker.mushroom.plugin.PluginContext;

/**
 *  插件过滤器
 * @author marker
 * */
public class PluginFilter implements Filter {

	// 日志记录器
	private static final Log log = LogFactory.getLog(PluginFilter.class);
	
	
	// 全局作用域
	private ServletContext application;
 
	 
	
	
	/** 插件文件名目录(如果要读取模版，必须配置) */
	protected String pluginFolder;

	/** 插件根路径 */
	protected String pluginsPath;
	

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest  request  = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		// 线程绑定请求对象和响应对象
		ActionContext.currentThreadBindRequestAndResponse(request, response);
		
		application.getContextPath();
		  
		
		FormMethod method = FormMethod.valueOf(request.getMethod());
		
		
		PluginContext context =	PluginContext.getInstance();
 
		 
		String uri = request.getRequestURI(); 
		int gradient = uri.indexOf("/",7);  
		if(gradient != -1){
			String pluginUrl = uri.substring(gradient+1,uri.length());
			try {
				 context.invoke(method, pluginUrl); 
			} catch (Exception e) { 
				e.printStackTrace();
			} 
		}
		 
	//	chain.doFilter(request, response);
		
	}



	@Override
	public void init(FilterConfig config) throws ServletException {
		this.application = config.getServletContext(); 
		log.debug("mrcms system filter initing...");
		
	}

	
	@Override
	public void destroy() {
		log.debug("mrcms system filter destroying...");
		
	}
}
