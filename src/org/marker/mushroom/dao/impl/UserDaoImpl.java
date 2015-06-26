package org.marker.mushroom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.marker.mushroom.beans.User;
import org.marker.mushroom.beans.UserGroup;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IUserDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperUser;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperUserGroup;
import org.springframework.stereotype.Repository;


@Repository("userDao")
public class UserDaoImpl extends DaoEngine implements IUserDao{

	
	public UserDaoImpl() {
		super(User.class);
	}
	
	
	/**
	 * 通过用户名和密码查询用户对象
	 * */
	@Override
	public User queryByNameAndPass(String name, String pass) {
		String prefix = dbConfig.getPrefix();
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(prefix).append(tableName).append(" where name=? and pass=?");
		System.out.println(sql);
		User user = null; 
		try{
			user = this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{name, pass}, new RowMapperUser());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}


	@Override
	public boolean updateLoginTime(Serializable id) {
		String prefix = dbConfig.getPrefix();
		StringBuilder sql = new StringBuilder("update ");
		sql.append(prefix).append("user ").append("set logintime=sysdate() where id=?");
		
		int status = jdbcTemplate.update(sql.toString(), id);
		return status>0?true:false;
	}


	/* (non-Javadoc)
	 * @see org.marker.mushroom.dao.IUserDao#findUserByName(java.lang.String)
	 */
	@Override
	public User findUserByName(String userName) {
		String prefix = dbConfig.getPrefix();
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(prefix).append(tableName).append(" where name=?");
		User user = null; 
		try{
			user = this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{userName}, new RowMapperUser());
		}catch (Exception e) {
			logger.error("通过name查询用户失败!", e);
		}
		
		return user; 
	}


	@Override
	public List<UserGroup> findGroup() {
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(getPreFix()).append("user_group"); 
		return this.jdbcTemplate.query(sql.toString(), new RowMapperUserGroup());
	}

}
