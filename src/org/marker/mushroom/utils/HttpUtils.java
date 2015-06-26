package org.marker.mushroom.utils;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * 工具类
 * @author marker
 *
 */
public class HttpUtils {

	
	
	/**
	 * 获取请求对象的URL地址
	 * @param request 请求对象
	 * @return String URL地址
	 */
	public static String getRequestURL(HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		String scheme = request.getScheme();
		String contextPath = request.getContextPath();
		int port = request.getServerPort();
		url.append(scheme); // http, https
		url.append("://");
		url.append(request.getServerName());
		if (("http".equals(scheme) && port != 80)
				|| ("https".equals(scheme) && port != 443)) {
			url.append(':');
			url.append(request.getServerPort());
		}
		url.append(contextPath);
		return url.toString();
	}
	
	
	/**
	 * for nigx返乡代理构造 获取客户端IP地址
	 * @param request
	 * @return
	 */
	public static String getRemoteHost(HttpServletRequest request){
		String ip = request.getHeader("X-Real-IP");//获取代理IP地址
		if(ip == null)
			ip = request.getRemoteHost();// 获取IP地址;
		return ip;
	}
	
	
}
