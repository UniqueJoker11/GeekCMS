package org.marker.mushroom.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebAPP;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.statistic.StatisticUtil;

/**
 * 前台界面的Servlet 这个Servlet主要用来将请求转到WebAPP对象中，有核心处理类来处理前台的请求信息
 * 
 * @author marker
 * */
public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 6700091564520406775L;

	/** 请求字符编码 */
	private static final String ENCODING = "utf-8";
	
	/** 响应内容类型 */
	private static final String CONTENT_TYPE = "text/html;charset=utf-8";
	
	
	/**
	 * 处理请求/cms?参数=值& 
	 * @throws IOException
	 * */
	public void progress(HttpServletRequest request,
			HttpServletResponse response) throws IOException { 
		request.setCharacterEncoding(ENCODING);// 设置请求字符编码
		response.setContentType(CONTENT_TYPE);// 设置响应字符编码
		
		// 线程绑定请求对象和响应对象
		ActionContext.currentThreadBindRequestAndResponse(request, response);
 
	
		StatisticUtil s = SpringContextHolder.getBean("statisticUtil");
		
		String ip =  (String) request.getAttribute(AppStatic.REAL_IP); // 搭建了niginx request.getRemoteHost();
		
		s.visited(ip); 
	
		// 创建应用实例(多线程模式)
		WebAPP app = WebAPP.newInstance();
		app.start();// 启动实例 
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.progress(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.progress(request, response);
	}

	/**
	 * 销毁
	 */
	@Override
	public void destroy() {
		super.destroy();
	}
}
