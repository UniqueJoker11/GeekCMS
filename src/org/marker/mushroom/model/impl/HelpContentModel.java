package org.marker.mushroom.model.impl;

import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.model.ContentModel;
import org.marker.mushroom.model.annotation.Model;
import org.marker.mushroom.sql.Sql;
import org.marker.mushroom.template.tags.res.SqlDataSource;
import org.marker.urlrewrite.URLRewriteEngine;

/**
 * 文章模型用来处理文章信息的
 * 
 * @author marker
 * */
@Model(
	    name = "帮助模型",
		author="marker",
		version = "0.2",
		type = "help",// 模型标识
		template = "log.html",
		description = "栏目模型主要是针对栏目信息"
	)
public class HelpContentModel extends ContentModel {

 
	// URL 重写引擎
	private URLRewriteEngine urlrewrite = SingletonProxyFrontURLRewrite.getInstance();
	
	
	
	/**
	 * 前台标签生成SQL遇到该模型则调用模型内算法
	 * @param tableName 表名称
	 * */
	public StringBuilder doWebFront(String tableName, SqlDataSource sqlDataSource) {
//		ObjectDataSourceImpl obj = (ObjectDataSourceImpl) sqlDataSource;
		String prefix = DataBaseConfig.getInstance().getPrefix();// 表前缀，如："yl_"
		StringBuilder sql = new StringBuilder("select ");
		sql.append("A.id,A.title,C.name as 'cname' ,A.time ,concat('/cms?p=',C.url,'&type=',C.module,'&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as 'url' from ");
		sql.append(prefix).append("article").append(Sql.QUERY_FOR_ALIAS)
				.append(" join ").append(prefix).append("channel").append(" C ")
				.append("on A.pid=C.id");
		return sql;
	}



 


	@Override
	public void doContent(Channel current, WebParam param) {
		String prefix = dbconfig.getPrefix();//表前缀，如："yl_"
		HttpServletRequest request = ActionContext.getReq();
		
		int cid = Integer.parseInt(param.contentId);// 获取指定ID的文章信息
		Object article = commonDao.findById(Article.class, cid);
		commonDao.update("update " + prefix
				+ "article set views = views+1 where id=?", cid);// 更新浏览量
		request.setAttribute("article", article);
		param.template = this.template;// 模型的模板
	}



	@Override
	public void doPage(Channel current, WebParam param) {
		String prefix = dbconfig.getPrefix();//表前缀，如："yl_"
		HttpServletRequest request = ActionContext.getReq();
		
		
		long pid  = current.getId();//当前栏目ID
		int limit = current.getRows();//每页内容条数
 
		
		int pageNo = 1;
		if(param.page != null && !"".equals(param.page)){
			try{
				pageNo = Integer.parseInt(param.page);
			}catch (Exception e) {e.printStackTrace(); }
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select A.id,A.title,C.name as cname ,A.time ,concat('/cms?p=',C.url,'&type=article&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as url from ");
		sql.append(prefix).append("article").append(Sql.QUERY_FOR_ALIAS)
		 .append(" join ").append(prefix).append("channel").append(" C on A.pid=C.id");
		sql.append(" where A.pid=").append(pid);
		
	 
		Page currentPage = commonDao.findByPage(pageNo, limit, sql.toString());
		
		request.setAttribute(AppStatic.WEB_APP_PAGE, currentPage);
		
		//传递分页信息
		String nextPage = "/cms?p="+param.pageName+"&page="+currentPage.getNextPageNo();
		String prevPage = "/cms?p="+param.pageName+"&page="+currentPage.getPrevPageNo();
		 
		request.setAttribute("nextpage", urlrewrite.encoder(nextPage));
		request.setAttribute("prevpage", urlrewrite.encoder(prevPage));
	}



	

}
