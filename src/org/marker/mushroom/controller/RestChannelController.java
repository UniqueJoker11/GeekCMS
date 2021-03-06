package org.marker.mushroom.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.model.ContentModelContext;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
 

/**
 * 栏目管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/api/channel")
public class RestChannelController extends SupportController {
	
	@Autowired protected IChannelDao channelDao;
	
	
	
	/**
	 * 获取所有栏目信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Object list(HttpServletRequest request){
	  return dao.queryForList("select * from  "+getPrefix()+"channel");
	} 
	
}
