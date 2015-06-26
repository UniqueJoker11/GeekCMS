/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.urlrewrite.URLRewriteEngine;

/**
 * @author marker
 * @date 2013-12-2 下午8:02:53
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class TestServlet  extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2990324920926049103L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("testing.....");
		URLRewriteConfig.getInstance();
		URLRewriteEngine ur = SingletonProxyFrontURLRewrite.getInstance();
	  
		String url ="/cms?p=index";
		String a = ur.encoder(url);
		System.out.println("re: "+a);
		
	}
	
	
	
}
