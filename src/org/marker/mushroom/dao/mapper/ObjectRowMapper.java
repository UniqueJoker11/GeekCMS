package org.marker.mushroom.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Menu;
import org.marker.mushroom.beans.Module;
import org.marker.mushroom.beans.Permission;
import org.marker.mushroom.beans.Plugin;
import org.marker.mushroom.beans.User;
import org.marker.mushroom.beans.UserGroup;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.urlrewrite.URLRewriteEngine;
import org.springframework.jdbc.core.RowMapper;

/**
 *  数据库查询结果集映射对象处理
 * 
 * 注意：这样做的目的是为了简化Dao实现的代码。而static final，这个是Spring3.2官方文档推荐使用的。
 * 
 * @author marker
 */
public final class ObjectRowMapper {

	// 栏目RowMapper
	public static final class RowMapperChannel implements RowMapper<Channel> { 
		public Channel mapRow(ResultSet rs, int arg1) throws SQLException {
			Channel channel = new Channel();
			channel.setId(rs.getInt("id"));
			channel.setName(rs.getString("name"));
			channel.setTemplate(rs.getString("template"));
			channel.setModule(rs.getString("module")); 
			channel.setUrl( rs.getString("url"));// URL地址
			channel.setPid(rs.getLong("pid"));
			channel.setRows(rs.getInt("rows"));// 分页条数目
			channel.setIcon(rs.getString("icon"));// 图标
			channel.setKeywords(rs.getString("keywords"));
			channel.setDescription(rs.getString("description"));
			channel.setRedirect(rs.getString("redirect"));// 重定向地址
			return channel;
		}
	}
	
	
	// 菜单RowMapper
	public static final class RowMapperMenu implements RowMapper<Menu> {
		public Menu mapRow(ResultSet rs, int arg1) throws SQLException {
			Menu menu = new Menu();
			menu.setId(rs.getInt("id"));
			menu.setPid(rs.getInt("pid"));
			menu.setName(rs.getString("name"));
			menu.setIcon(rs.getString("icon"));
			menu.setSort(rs.getInt("sort"));
			menu.setUrl(rs.getString("url"));
			return menu;
		}
	}
	
	
	// 用户 RowMapper
	public static final class RowMapperUser implements RowMapper<User> {
		public User mapRow(ResultSet rs, int num) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setNickname(rs.getString("nickname"));
			user.setLogintime(rs.getDate("logintime"));
			user.setGroup(rs.getInt("group"));// 分组ID
			user.setStatus(rs.getShort("status"));
			return user;
		}
	}
	
	
	// 插件 RowMapper
	public static final class RowMapperPlugin implements RowMapper<Plugin> {
		public Plugin mapRow(ResultSet rs, int num) throws SQLException {
			Plugin plugin = new Plugin();
			plugin.setId(rs.getInt("id"));
			plugin.setName(rs.getString("name"));
			plugin.setUri(rs.getString("uri"));
			plugin.setMark(rs.getString("mark"));
			plugin.setStatus(rs.getInt("status"));
			plugin.setDescription(rs.getString("description"));
			return plugin;
		}
	}
	
	
	// 内容模型 RowMapper
	public static final class RowMapperModule implements RowMapper<Module> {
		public Module mapRow(ResultSet rs, int num) throws SQLException {
			Module module = new Module();
			module.setId(rs.getLong("id"));
			module.setName(rs.getString("name"));
			module.setUri(rs.getString("uri"));
			module.setType(rs.getString("type"));
			module.setTemplate(rs.getString("template"));
			module.setVersion(rs.getInt("version"));
			return module;
		}
	}
	
	// 权限 RowMapper
	public static final class RowMapperPermission implements RowMapper<Permission>{
		public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
			Permission permission = new Permission();
			permission.setGid(rs.getInt("gid"));
			permission.setMid(rs.getInt("mid"));
			return permission;
		}
		
	}
	
	// 用户分组 RowMapper
	public static final class RowMapperUserGroup implements RowMapper<UserGroup>{
		public UserGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserGroup group = new UserGroup();
			group.setId(rs.getInt("id"));
			group.setName(rs.getString("name"));
			group.setScope(rs.getInt("scope"));
			group.setDescription(rs.getString("description"));
			return group;
		}
		
	}
}
