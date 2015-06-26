package org.marker.mushroom.menu;

import org.marker.mushroom.beans.Menu;
import org.marker.mushroom.dao.IMenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 菜单工具类
 * 负责三方框架操作添加对应的菜单
 * @author marker
 * @version 1.0
 */
@Component("menutools")
public class MenuTool {

	// 菜单操作Dao
	@Autowired private IMenuDao menuDao;
	
	
	public void builder(String type , Menu menu){
		
		int pid = menuDao.findMenuIdByType(type);
		menu.setPid(pid);// 设置父级ID
		
		// 保存操作
		menuDao.save(menu);
		
		// 将菜单添加给内置管理员
		System.out.println(menu.getId());
		
	}
}
