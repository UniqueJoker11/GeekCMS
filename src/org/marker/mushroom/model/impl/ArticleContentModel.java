package org.marker.mushroom.model.impl;

import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.model.ContentModel;
import org.marker.mushroom.model.annotation.Model;
import org.marker.mushroom.sql.Sql;
import org.marker.mushroom.template.tags.res.SqlDataSource;
import org.marker.urlrewrite.URLRewriteEngine;

/**
 * 文章模型用来处理文章信息的
 * 每个模型对应一个表
 * 
 * @author marker
 * */
@Model(
    name = "文章模型",
	author="marker",
	version = "0.2",
	type = "article",// 模型标识
	template = "article.html",
	description = "系统默认提供的文章模型"
)
public class ArticleContentModel extends ContentModel {
	
	
	
	//处理栏目下内容分页
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
		sql.append("select A.id,A.title,C.name as cname ,A.time ,concat('p=',C.url,'&type=article&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as url from ");
		sql.append(prefix).append("article").append(Sql.QUERY_FOR_ALIAS)
		 .append(" join ").append(prefix).append("channel").append(" C on A.pid=C.id");
		sql.append(" where A.pid=").append(pid);
		
	 
		Page currentPage = commonDao.findByPage(pageNo, limit, sql.toString());
		
		request.setAttribute(AppStatic.WEB_APP_PAGE, currentPage);
		
		URLRewriteEngine urlRewrite = SingletonProxyFrontURLRewrite.getInstance();
		
		//传递分页信息
		String nextPage = "p="+param.pageName+"&page="+currentPage.getNextPageNo();
		String prevPage = "p="+param.pageName+"&page="+currentPage.getPrevPageNo();
		request.setAttribute("nextpage", urlRewrite.encoder(nextPage));
		request.setAttribute("prevpage", urlRewrite.encoder(prevPage));
		
	}
	
	
	
	/**
	 * 前台标签生成SQL遇到该模型则调用模型内算法
	 * @param tableName 表名称
	 * */
	public StringBuilder doWebFront(String tableName, SqlDataSource sqlDataSource) {
		String prefix = dbconfig.getPrefix();// 表前缀，如："yl_"
		StringBuilder sql = new StringBuilder("select ");
		sql.append("A.id,A.title,A.content,C.name as 'cname' ,A.time ,concat('/cms?p=',C.url,'&type=',C.module,'&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as 'url' from ");
		sql.append(prefix).append("article").append(Sql.QUERY_FOR_ALIAS)
				.append(" join ").append(prefix).append("channel").append(" C ")
				.append("on A.pid=C.id");
		return sql;
	}


	
	public void doContent(Channel current, WebParam param) {
		String prefix = dbconfig.getPrefix();//表前缀，如："yl_"
		HttpServletRequest request = ActionContext.getReq();
		
		
		long cid = Long.parseLong(param.contentId);// 获取指定ID的文章信息
		Object article = commonDao.findById(Article.class, cid);
		commonDao.update("update " + prefix
				+ "article set views = views+1 where id=?", cid);// 更新浏览量
		request.setAttribute("article", article);
		param.template = this.template;// 模型的模板
	}



	

}
