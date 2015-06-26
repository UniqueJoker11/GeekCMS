package org.marker.mushroom.controller;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.beans.User;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebAPP;
import org.marker.mushroom.dao.IMenuDao;
import org.marker.mushroom.dao.IUserDao;
import org.marker.mushroom.dao.IUserLoginLogDao;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.GeneratePass;
import org.marker.mushroom.utils.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理主界面控制器
 * 
 * @author marker
 * 
 * */
@Controller
@RequestMapping("/admin")
public class AdminController extends SupportController {

	private final Log log = LogFactory.getLog(AdminController.class);

	@Resource
	IUserDao userDao;
	@Resource
	IUserLoginLogDao userLoginLogDao;
	@Resource
	IMenuDao menuDao;
	@Resource
	ServletContext application;

	/** 构造方法初始化一些成员变量 */
	public AdminController() {
		this.viewPath = "/admin/";
	}

	/** 后台主界面 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		// 如果没有安装系统
		if (!WebAPP.install)
			return "redirect:../install/index.jsp";

		request.setAttribute("url", HttpUtils.getRequestURL(request));
		HttpSession session = request.getSession();
		if (session != null) {
			try {
				int groupId = (Integer) session
						.getAttribute(AppStatic.WEB_APP_SESSSION_USER_GROUP_ID);
				request.setAttribute("topmenus",
						menuDao.findTopMenuByGroupId(groupId));
			} catch (Exception e) {
				log.error("因为没有登录，在主页就不能查询到分组ID", e);
				return "redirect:login.do";
			}
		}

		return this.viewPath + "index";
	}

	/**
	 * 子菜单接口
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/childmenus")
	public Object menu(HttpServletRequest request, @RequestParam("id") int id) {
		ModelAndView view = new ModelAndView(this.viewPath + "childmenus");
		view.addObject("menu", menuDao.findMenuById(id));
		HttpSession session = request.getSession();
		if (session != null) {
			int groupId = (Integer) session
					.getAttribute(AppStatic.WEB_APP_SESSSION_USER_GROUP_ID);
			view.addObject("childmenus",
					menuDao.findChildMenuByGroupAndParentId(groupId, id));
		}
		return view;
	}

	/**
	 * 登录操作
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		// 如果没有安装系统
		if (!WebAPP.install)
			return "redirect:../install/index.jsp";
		request.setAttribute("url", HttpUtils.getRequestURL(request));
		return this.viewPath + "login";
	}

	/**
	 * 登录系统 验证码不区分大小写
	 * 
	 * @return json
	 * */
	@ResponseBody
	@RequestMapping(value = "/loginSystem", method = RequestMethod.POST)
	public Object loginSystem(HttpServletRequest request) {
		String randcode = request.getParameter("randcode").toLowerCase();// 验证码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();// 如果会话不存在也就不创建
		Object authCode = session.getAttribute(AppStatic.WEB_APP_AUTH_CODE);

		int loginLogType = 0;// 登录日志类型
		String scode = "";
		if (authCode != null) {
			scode = ((String) authCode).toLowerCase();
		}

		ResultMessage msg = null;
		if (scode != null && !scode.equals(randcode)) {// 验证码不匹配
			msg = new ResultMessage(false, "验证码错误!");
			loginLogType = 1;// 错误
		} else {
			String password2 = null;
			try {
				password2 = GeneratePass.encode(password);
				User user = userDao.queryByNameAndPass(username, password2);
				if (user != null) {
					if (user.getStatus() == 1) {// 启用
						userDao.updateLoginTime(user.getId());// 更新登录时间
						session.setAttribute(AppStatic.WEB_APP_SESSION_ADMIN,
								user);
						session.setAttribute(
								AppStatic.WEB_APP_SESSSION_LOGINNAME,
								user.getName());
						session.setAttribute(
								AppStatic.WEB_APP_SESSSION_USER_GROUP_ID,
								user.getGroup());// 设置分组
						session.setAttribute(
								AppStatic.WEB_APP_SESSION_NICKNAME,
								user.getNickname());
						session.removeAttribute(AppStatic.WEB_APP_AUTH_CODE);// 移除验证码
						msg = new ResultMessage(true, "登录成功!");
					} else {
						loginLogType = 1;
						msg = new ResultMessage(false, "用户已禁止登录!");
					}
				} else {
					loginLogType = 1;
					msg = new ResultMessage(false, "用户名或者密码错误!p:" + password);
				}
			} catch (Exception e) {
				loginLogType = 1;
				msg = new ResultMessage(false, "系统加密算法异常!");
				log.error("系统加密算法异常!", e);
			}

		}
		// 获取真实IP地址
		String ip = HttpUtils.getRemoteHost(request);

		// 记录日志信息
		userLoginLogDao.intsert(ip, username, loginLogType, msg.getMessage());

		return msg;
	}

	/**
	 * 注销
	 * */
	@RequestMapping("/logout")
	public void logout(HttpServletResponse response, HttpSession session) {
		if (session != null)
			session.invalidate();
		try {
			response.sendRedirect("login.do");
		} catch (IOException e) {
			log.error("注销登录重定向异常", e);
		}
	}

	/**
	 * 系统信息
	 * */
	@RequestMapping("/systeminfo")
	public ModelAndView systeminfo() {
		ModelAndView view = new ModelAndView(this.viewPath + "systeminfo");
		String os = System.getProperty("os.name");// 操作系统名称
		String osVer = System.getProperty("os.version"); // 操作系统版本
		String javaVer = System.getProperty("java.version"); // 操作系统版本
		String javaVendor = System.getProperty("java.vendor"); // 操作系统版本

		Runtime runTime = Runtime.getRuntime();

		long freeM = runTime.freeMemory() / 1024 / 1024;
		long maxM = runTime.maxMemory() / 1024 / 1024;
		long tM = runTime.totalMemory() / 1024 / 1024;
		view.addObject("freememory", freeM);
		view.addObject("maxmemory", maxM);
		view.addObject("totalmemory", tM);
		view.addObject("os", os);
		view.addObject("osver", osVer);
		view.addObject("javaver", javaVer);
		view.addObject("javavendor", javaVendor);
		view.addObject("currenttime", new Date());

		view.addObject("serverinfo", application.getServerInfo());
		view.addObject("dauthor", "marker");
		view.addObject("email", "wuweiit@gmail.com");
		view.addObject("version", "20140105");
		view.addObject("qqqun", "331925386");
		view.addObject("uxqqqun", "181150189");

		return view;
	}

}
