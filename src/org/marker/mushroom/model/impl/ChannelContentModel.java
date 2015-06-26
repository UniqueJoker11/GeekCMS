package org.marker.mushroom.model.impl;

import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.model.ContentModel;
import org.marker.mushroom.model.annotation.Model;
import org.marker.mushroom.sql.Sql;
import org.marker.mushroom.template.tags.res.SqlDataSource;




/**
 * 栏目的模型处理
 * 
 * @author marker
 * */
@Model(
    name = "栏目模型",
	author="marker",
	version = "0.2",
	type = "channel",// 模型标识
	template = "",
	description = "栏目模型主要是针对栏目信息"
)
public class ChannelContentModel extends ContentModel {
	
	
	/**
	 * 前台标签生成SQL遇到该模型则调用模型内算法
	 * @param tableName 表名称
	 * */
	public StringBuilder doWebFront(String tableName, SqlDataSource sqlDataSource) {
		String prefix = dbconfig.getPrefix();// 表前缀，如："yl_"
		StringBuilder sql = new StringBuilder();
		sql.append("select A.pid,A.id,A.name,A.template,A.hide,A.keywords,A.description,A.icon,A.rows,A.sort,A.module, concat('p=',A.url) 'url' from ");
		sql.append(prefix).append("channel ").append(Sql.QUERY_FOR_ALIAS);
		return sql;
	}

	
	
	 
	public void doContent(Channel arg0, WebParam arg1) {
		// TODO Auto-generated method stub
		
	}
 
	public void doPage(Channel arg0, WebParam arg1) {
		// TODO Auto-generated method stub
		
	}

}
