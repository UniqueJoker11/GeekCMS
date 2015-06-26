package org.marker.mushroom.filter;

import java.io.IOException;
import java.util.regex.Pattern;

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
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.urlrewrite.URLRewriteEngine;

/**
 * 1. 反向代理真实IP获取，主要是请求头中的X-Real-IP参数
 * 2. URL重写功能 (主要是重写前端访问地址)
 * @author marker
 * */
public class SystemCoreFilter implements Filter {

	// 日志记录器
	private static final Log log = LogFactory.getLog(SystemCoreFilter.class);

	// URL重写引擎
	private static final URLRewriteEngine rewrite = SingletonProxyFrontURLRewrite.getInstance();
	
	// 全局作用域
	private ServletContext application;

	// 排除过滤格式
	private Pattern suffixPattern;
	
	private String Encoding = "utf-8";
	

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		// 记录开始执行时间
		req.setAttribute(AppStatic.WEB_APP_STARTTIME, System.currentTimeMillis());
		HttpServletRequest  request  = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) { 
//			e.printStackTrace();
//		}
		
		request.setCharacterEncoding(Encoding);
		response.setContentType("text/html;charset=utf-8");
		String uri  = request.getRequestURI();  
		
		/* ============================================
		 *              排除过滤格式
		 * ============================================
		 */
		int dotIndex= uri.lastIndexOf(".") + 1;// 请求文件后缀
		if (dotIndex != -1) {// 有后缀
			String suffix = uri.substring(dotIndex, uri.length());
			if (suffixPattern.matcher(suffix).matches()) {
				chain.doFilter(req, response);
				return; // 因为这里是静态文件，所以直接返回了
			}
		}
		if(uri.startsWith("/public")){
			chain.doFilter(req, response);
			return; // 因为这里是公共文件，所以直接返回了
		}
		
		
	
		// IP地址获取
		String ip = HttpUtils.getRemoteHost(request);
		req.setAttribute(AppStatic.REAL_IP, ip);// 将用户真实IP写入请求属性
		
		/* ============================================
		 *     uri中移除项目名称
		 * ============================================
		 */
		uri = uri.replace(request.getContextPath(), "");
		// uri: /index.html
		// uri: /plugin/dsdsd.html
		int gradient = uri.indexOf("plugin");
		if(gradient != -1){
			String pluginPath = uri.substring(1,gradient+6);
//			System.out.println(pluginPath);
			if("plugin".equals(pluginPath)){// 判断是否为插件路径
				chain.doFilter(request, response);
				return;
			}
		}
		
		// 防止项目文件夹错误指向
		log.info("req url="+uri);
		String url = rewrite.decoder(uri);
		log.info("decoder url="+url);
		
		
		// 网址路径
		application.setAttribute(AppStatic.WEB_APP_URL,
				HttpUtils.getRequestURL(request));
		
		
		req.getRequestDispatcher(url).forward(request, response);// 请求转发
		  
	}



	@Override
	public void init(FilterConfig config) throws ServletException {
		this.application = config.getServletContext();
		String excludeFormat = config.getInitParameter("exclude_format");
		this.suffixPattern = Pattern.compile("("+excludeFormat+")");
		log.info("mrcms system filter initing...");
	}

	
	@Override
	public void destroy() {
		log.info("mrcms system filter destroying..."); 
	}
}
