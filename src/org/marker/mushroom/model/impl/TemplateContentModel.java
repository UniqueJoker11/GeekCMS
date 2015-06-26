package org.marker.mushroom.model.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.Template;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.channel.ChannelItem;
import org.marker.mushroom.core.channel.ChannelTree;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.model.ContentModel;
import org.marker.mushroom.model.annotation.Model;
import org.marker.mushroom.template.tags.res.ObjectDataSourceImpl;
import org.marker.mushroom.template.tags.res.SqlDataSource;


/**
 * 模板模型
 * 
 * @author marker
 * @version 1.0
 */
@Model(
	    name = "模板文档模型",
		author="marker",
		version = "0.2",
		type = "template",// 模型标识
		template = "template.html",
		description = "模板文档"
	)
public class TemplateContentModel extends ContentModel {
	
	
	
	public StringBuilder doWebFront(String tableName,
			SqlDataSource sqlDataSource) {
		ObjectDataSourceImpl obj = (ObjectDataSourceImpl) sqlDataSource;
		
		String prefix = DataBaseConfig.getInstance().getPrefix();//表前缀，如："yl_"
		
		StringBuilder queryString = new StringBuilder(
				"select a.id,a.name,a.icon,a.durl,a.grade,a.dloaded,a.author,c.name as cname ,a.time ,concat('cms?p=',c.url,'&type=template&id=',CAST(a.id as char),'&time=',DATE_FORMAT(a.time,'%Y%m%d')) as url " +
				"from "+prefix+"template a,channel c ");
		if(obj.getWhere() != null && !"".endsWith(obj.getWhere())){
			queryString.append("where ");
			String[] ws = obj.getWhere().split(",");
			int count = 0;
			for(String a : ws){
				count++;
				queryString.append("a."+a);
				if(count != ws.length){
					queryString.append(" and ");
				}
			}
			queryString.append(" and a.pid=c.id ");
		}else{
			queryString.append(" where a.pid=c.id "); 
		} 
		return queryString;
	}

	
	/**
	 * 根据参数ID获取内容信息，并更新views值 
	 * */
	@Override
	public void doContent(Channel current, WebParam param) {
		String prefix = dbconfig.getPrefix();//表前缀，如："yl_"
		HttpServletRequest request = ActionContext.getReq();
		
		long cid = Long.parseLong(param.contentId);// 内容ID 
		Object tpl = commonDao.findById(Template.class, cid); 
		commonDao.update("update "+prefix+"template set views = views+1 where id=?", cid);// 更新浏览量
		request.setAttribute("template", tpl);
		param.template = this.template;// 模型的模板
	}




	@Override
	public void doPage(Channel current, WebParam param) {
		String prefix = dbconfig.getPrefix();//表前缀，如："yl_"
		HttpServletRequest request = ActionContext.getReq();
		
		//分页查询模板 
		long pid  = current.getId();//当前栏目ID
		int limit = current.getRows();//每页内容条数
		
		int pageNo = 1;
		if(param.page != null && !"".equals(param.page)){
			try{
				pageNo = Integer.parseInt(param.page);
			}catch (Exception e) {e.printStackTrace(); }
		} 
		
		IChannelDao channelDao = SpringContextHolder.getBean("channelDao");
		
		List<Channel> list = channelDao.findAll();
		
		ChannelItem currentItem = ChannelTree.foreach(pid, current, list);
		
		
		String str = currentItem.getChildIdToString();
 
		String queryString = "select A.id,A.name,A.name as cname ,A.time ,A.durl,A.dloaded,a.grade,A.author,A.icon,A.description,A.views,concat('/cms?p=',c.url,'&type=template&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as url " +
				"from "+prefix+"template A" +
				" join "+prefix+"channel c on a.pid=c.id " +
				" where A.pid in("+str+")";
	 
		Page currentPage = commonDao.findByPage(pageNo, limit, queryString);
		request.setAttribute(AppStatic.WEB_APP_PAGE, currentPage);
		
		//传递分页信息
		String nextPage = "/cms?p="+param.pageName+"&page="+currentPage.getNextPageNo();
		String prevPage = "/cms?p="+param.pageName+"&page="+currentPage.getPrevPageNo();
//		request.setAttribute("nextpage", URLRewrite.me().encoder(nextPage));
//		request.setAttribute("prevpage", URLRewrite.me().encoder(prevPage));
			
  
	}

}
