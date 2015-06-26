package org.marker.mushroom.dao;

 
import java.io.Serializable;
import java.util.List;

import org.marker.mushroom.beans.Menu;

/**
 * 菜单管理Dao
 * @author marker
 */
public interface IMenuDao  extends ISupportDao{

	boolean hasChildMenu(int menuId);

	/**
	 * 查询最高级菜单
	 */
	List<Menu> findTopMenu();
	
	/**
	 * 通过分组ID查询最顶级菜单
	 */
	List<Menu> findTopMenuByGroupId(Serializable groupId);
	
	/**
	 * 通过分组ID和父级ID查询子菜单
	 */
	List<Menu> findChildMenuByGroupAndParentId(Serializable groupId, Serializable parentId);
	
	
	
	/**
	 * 通过ID查询子菜单
	 * @param id
	 * @return
	 */
	List<Menu> findChildMenuById(int id);
	
	
	
	
	
	Menu findMenuById(int id);
	
	/**
	 * 根据类型查询菜单ID
	 * @param type
	 * @return
	 */
	int findMenuIdByType(String type);
}
