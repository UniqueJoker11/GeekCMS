package org.marker.mushroom.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.plugin.annotation.Directive;
import org.marker.mushroom.plugin.annotation.Mapping;
import org.marker.mushroom.plugin.annotation.RestBody;


/**
 * Pluginlet代理，主要是为了节约每次获取Method的时间，这样处理，更加高效。
 * @author marker
 * @version 1.0
 */
public class ProxyPluginlet {

	// 实例
	private Pluginlet object;
	
	// URI-Method
	private Map<String, Method> urlMapping = new HashMap<String, Method>();
	// 嵌入式指令
	private Map<String, Method> directiveMapping  = new HashMap<String, Method>();
	
	// 插件信息
	private PluginInfo info;
	
	
	/**
	 * 初始化
	 * @param pluginlet
	 * @throws Exception 
	 */
	public ProxyPluginlet(Pluginlet pluginlet, PluginInfo info) throws Exception {
		this.object = pluginlet;
		this.info = info;
		this.init();
	}

	






	/**
	 * 初始化方法
	 * @throws Exception 
	 */
	private void init() throws Exception{
		Method[] methods = this.object.getClass().getDeclaredMethods();
		for(Method me : methods){
			Mapping mapping     = me.getAnnotation(Mapping.class);// 获取映射
			Directive directive = me.getAnnotation(Directive.class);// 获取嵌入式指令
			
			if (mapping != null && directive != null) {// bug
				throw new Exception(this.object.getClass() + " is error plugin");
			} else if (mapping != null) {// 如果没有注解，不进行绑定方法
				String url = mapping.method() + "|" + mapping.value();
				urlMapping.put(url, me);
				continue;
			} else if (directive != null) {// 嵌入式指令
				directiveMapping.put(directive.value(), me);
				continue;
			}
		} 
	}
	
	
	
	/**
	 * 调用指令 
	 * @param directive
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @return 返回""只要目的是为了避免有的新手使用模板引擎的一些疏忽
	 */
	public String invokeDrictive(String directive) throws Exception{ 
		Method me = directiveMapping.get(directive);
		if (me != null) {// 指令存在
			Object obj = me.invoke(object);
			if (obj != null) {
				return obj.toString();
			}
		}
		return "";
	}

	/**
	 * 根据URL调用对应的Pluginlet方法
	 * @param FormMethod method 请求方法
	 * @param url
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public ViewObject invoke(FormMethod method, String url) throws Exception{
		String new_url = method + "|" + url;
		Method me = urlMapping.get(new_url);
		if(me != null){
			ViewObject view = new ViewObject();
			if(me.getAnnotation(RestBody.class) != null){// JSON
				view.setType(ViewType.JSON);
			}else{// HTML
				view.setType(ViewType.HTML);
			} 
			view.setResult(me.invoke(object));
			return view;
		}
		return null;
	}
	
	public PluginInfo getInfo() {
		return info;
	}

	
}
