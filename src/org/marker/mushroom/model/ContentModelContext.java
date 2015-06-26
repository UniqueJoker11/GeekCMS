package org.marker.mushroom.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.ModuleNotFoundException;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.model.annotation.Model;
import org.marker.mushroom.model.impl.ArticleContentModel;
import org.marker.mushroom.model.impl.ChannelContentModel;
import org.marker.mushroom.model.impl.HelpContentModel;
import org.marker.mushroom.template.tags.res.SqlDataSource;


/**
 * 内容模型作用域
 * 
 * @author marker
 * @version 1.0
 */
public class ContentModelContext implements IContentModelParse {

	
	 
	/** 系统配置信息 */
	public static final SystemConfig sysConfig = SystemConfig.getInstance();
	 
	
	/** 存放模型的集合(key:类型 value:模型对象) */
	private final Map<String,ContentModel> contentModels = new ConcurrentHashMap<String,ContentModel>();
	
	private IChannelDao channelDao ;
	
	
	
	private ContentModelContext(){ 
		channelDao = SpringContextHolder.getBean(SystemStatic.DAO_CHANNEL); 
		 put(new ChannelContentModel());// 栏目模型
		 put(new ArticleContentModel());// 文章模型
		 put(new HelpContentModel());// 帮助模型
	}
	
	
	
	/**
	 * 初始化
	 */
	public void put(ContentModel model){
		model.init( model.getClass().getAnnotation(Model.class));
		contentModels.put(model.getType(), model); 
	}
	
	
	
	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static class SingletonHolder {
		public final static ContentModelContext instance = new ContentModelContext();     
	}
	
	
	/**
	 * 获取数据库配置实例
	 * */
	public static ContentModelContext getInstance(){
		return SingletonHolder.instance;
	}



	// 解析内容模型的类型 
	public int parse(WebParam param) throws SystemException {
		HttpServletRequest request = ActionContext.getReq();//获取请求对象
		
		//查询当前访问的栏目信息，栏目信息里面包含模型调用对应的模型库
		Channel currentChannel = channelDao.queryByUrl(param.pageName);
		if(currentChannel != null){ 
			String keywords    = currentChannel.getKeywords();
			String description = currentChannel.getDescription(); 
			if("".equals(description)){
				description = sysConfig.get(SystemConfig.Names.DESCRIPTION);
				currentChannel.setDescription(description);
			}
			if("".equals(keywords)){
				keywords = sysConfig.get(SystemConfig.Names.KEYWORDS); 
				currentChannel.setKeywords(keywords);
			}
			
			
			
			request.setAttribute(AppStatic.WEB_CURRENT_CHANNEL, currentChannel);
			param.template   = currentChannel.getTemplate();//模板
			param.moduleType = currentChannel.getModule();//内容模型
			param.redirect   = currentChannel.getRedirect();//重定向地址
			if(param.redirect != null && !"".equals(param.redirect)){
				return 1;//如果重定向地址不为null
			}
			
			//查到栏目对应的模型，然后进行相应操作
			ContentModel mod = contentModels.get(param.moduleType);//获取模型
			if(mod != null){
				//获取应用作用域
				if(param.contentId != null && !"".equals(param.contentId)){//内容
					mod.doContent(currentChannel, param);
				}else{//
					mod.doPage(currentChannel,param);
				}
				return 2;
			}else{
				//没有找到内容模型
				throw new ModuleNotFoundException(param.moduleType);
			}
			
		}
		return 0;
	}


	
	/**
	 * 这里有点问题，不能是tableName
	 */
	@Override
	public StringBuilder parse(String tableName, SqlDataSource sqldatasource) throws SystemException {
	 
		//当type=null的时候应该获取栏目的模型，然后进行处理
		ContentModel mod = contentModels.get(tableName);//获取模型
		if(mod != null){
			return mod.doWebFront(tableName, sqldatasource);
		}
		return null; 
	}


	
	public Object getCurrentList() {
		List<ModelInfo> list = new ArrayList<ModelInfo>(5);
		Set<String> keys = contentModels.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			ContentModel cm = contentModels.get(it.next());
			list.add(cm.getInfo());
		}
		return list;
	}
	
	
 
	
	
}
