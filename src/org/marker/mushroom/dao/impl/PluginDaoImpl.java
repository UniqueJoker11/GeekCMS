package org.marker.mushroom.dao.impl;

import java.util.List;

import org.marker.mushroom.beans.Plugin;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IPluginDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperPlugin;
import org.springframework.stereotype.Repository;


/**
 * 插件数据库操作对象
 * @author marker
 * */
@Repository(SystemStatic.DAO_PLUGIN)
public class PluginDaoImpl extends DaoEngine implements IPluginDao{

	
	public PluginDaoImpl() { 
		super(Plugin.class);
	}

	
	
	/**
	 * 查询所有插件
	 */
	@Override
	public List<Plugin> queryAll() {
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(getPreFix()).append(tableName).append(" where status=1"); 
		return jdbcTemplate.query(sql.toString(), new RowMapperPlugin()); 
	}


	
	@Override
	public Plugin findByMark(String mark) {
		String prefix = dbConfig.getPrefix();
		StringBuilder sql = new StringBuilder("select id,name,uri,mark,status from ");
		sql.append(prefix).append(tableName).append(" where mark=?");
		return jdbcTemplate.queryForObject(sql.toString(), new Object[]{mark}, new RowMapperPlugin()); 
	}
	
	
	
	 
}
