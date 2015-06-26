package org.marker.mushroom.model;

import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.model.annotation.Model;
import org.marker.mushroom.template.tags.res.SqlDataSource;



/**
 * 抽象模型
 * 模型是拿来匹配和我发布文章或者是产品信息的时候用的，
 * 如果我发布一个商品，那么就会调用商品模型，这样区分出来
 *  
 * 
 * @author marker
 * 
 * */
public abstract class ContentModel{
	
	/** 数据库配置信息 */
	public static final DataBaseConfig dbconfig = DataBaseConfig.getInstance();

	/** 数据库模型引擎 */
	public ISupportDao commonDao;
	
	
	protected String type;//类型
	protected String name;//名称
	protected String template;//模板
	
	
	/**
	 * 初始化一些必要工具
	 */
	public ContentModel() {
		this.commonDao = SpringContextHolder.getBean(SystemStatic.DAO_COMMON);
	}
	
	
	/**
	 * 获取数据库前缀
	 * @return
	 */
	public String getPrefix(){
		return dbconfig.getPrefix();
	}

	
	private ModelInfo info;
	
	
	/**
	 * 初始化基本信息
	 * @param info
	 */
	public void init(Model model) { 
		info = new ModelInfo();
		info.setAuthor(model.author());
		info.setDescription(model.description());
		info.setName(model.name());
		info.setTemplate(model.template());
		info.setType(model.type());
		info.setVersion(model.version());// 版本
	}

	
	
	
	public String getType(){
		if(info != null)
			return info.getType();
		return null; 
	}
	
	public ModelInfo getInfo() {
		return info;
	}
	
	
	

	/**
	 * 处理分页查询信息
	 * */
	public abstract void doPage(Channel currentChannel, WebParam param);
	
	
	
	/**
	 * Web前端生成SQL语句(模板引擎会调用来生成sql语句)
	 * @see SQLDataEngine
	 * */
	public abstract StringBuilder doWebFront(String tableName, SqlDataSource sqlDataSource);



	/**
	 * 内容处理
	 * @param currentChannel
	 * @param param
	 */
	public abstract void doContent(Channel currentChannel,  WebParam param);



	
}
