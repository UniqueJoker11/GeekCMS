package org.marker.mushroom.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.utils.WebUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 判断是否登录，如果没有登录就重定向到/admin/login.do
 * 
 * @author marker
 * 
 * */
public class LoginInterceptor implements HandlerInterceptor  {


	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
//		HttpSession session = request.getSession();
//		String username = (String)session.getAttribute(AppStatic.WEB_APP_SESSSION_LOGINNAME);
//		if(username != null){
//			return true;
//		}else{
//			if("/admin/childmenus.do".equals(request.getRequestURI())){// 访问菜单，不进行权限验证！
//				WebUtils.sendRedirect(response, "login.do"); 
//				return false;
//			}
//			
//			
//			// 判断页面类型，有的请求 
//			System.out.println(request.getRequestURI());
//			String accept = request.getHeader("accept"); 
//			if(accept.matches(".*application/json.*")){// json数据请求
//				PrintWriter out = response.getWriter(); 
//				out.write("{\"status\":false,\"code\":\"101\",\"message\":\"当前会话失效，请重新登录系统!\"}");
//				out.flush();
//				out.close();
//				return false;
//			}
//			
//			
//			
//			response.sendRedirect("/admin/login.do");
//			return false;
//		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
