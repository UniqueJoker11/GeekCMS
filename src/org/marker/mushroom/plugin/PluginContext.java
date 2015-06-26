package org.marker.mushroom.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.freemarker.LoadDirective;
import org.marker.mushroom.freemarker.UpperDirective;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.plugin.annotation.Plugin;
import org.marker.mushroom.plugin.freemarker.EmbedDirectiveInvokeTag;
import org.marker.urlrewrite.freemarker.FrontURLRewriteMethodModel;
import org.osgi.framework.Bundle;

import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 * 插件容器，主要存放插件
 * @author marker
 * @version 1.0
 */
public class PluginContext {

	// 存放HTTP插件
	// 插件路径/代理 ，主要是因为这块的热部署功能，因此使用并发库中线程安全HanMap。
	private static final Map<String, ProxyPluginlet> pluginLets = new ConcurrentHashMap<String, ProxyPluginlet>();
	
	
	
	
	private PluginContext(){
		try {
			cfg.setTemplateUpdateDelay(0);
			cfg.setDirectoryForTemplateLoading(new File(WebRealPathHolder.REAL_PATH+"plugins"+File.separator));
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static class SingletonHolder {
		public final static PluginContext instance = new PluginContext();     
	}
	
	
	/**
	 * 获取数据库配置实例
	 * */
	public static PluginContext getInstance(){
		return SingletonHolder.instance;
	}
	

	/** freeMarker模版引擎配置 */
	private Configuration cfg = new Configuration();
	
	
	 
	
	/**
	 * 添加Pluginlet
	 * @param url
	 * @param action
	 * @throws Exception 
	 */
	public void addPluginlet(Bundle bundle, Pluginlet pluginlet) throws Exception{
		Plugin plugin = pluginlet.getClass().getAnnotation(Plugin.class);
		if(plugin == null)
			throw new Exception(pluginlet.getClass()+" plugin class invalid");
		pluginLets.put(plugin.value(), new ProxyPluginlet(pluginlet, new PluginInfo(bundle)));
	}
	
	
	/**
	 * 移除Pluginlet
	 * @param clzz
	 * @throws Exception 
	 */
	public void remove(Class<?> clzz) throws Exception{
		Plugin plugin = clzz.getAnnotation(Plugin.class);
		if(plugin == null) 
			throw new Exception(clzz + " plugin class invalid"); 
		pluginLets.remove(plugin.value());
	}


	/**
	 * 调用
	 * @param pluginName
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public ViewObject invoke(FormMethod method, String uri) throws Exception { 
		if(method == null)
			throw new Exception("request method invalid");
		int index = uri.indexOf("/");
		if(index != -1){// 解析成功
			String pluginName = uri.substring(0, index);// 插件名称
			String pluginCurl = uri.substring(index, uri.length());// 插件功能URL 
			ProxyPluginlet pluginProxy = pluginLets.get(pluginName);
			if(pluginProxy != null){
				ViewObject view = pluginProxy.invoke(method, pluginCurl); 
					if(view != null){// 如果返回值为空，代表是自己手动处理
					Writer out = ActionContext.getResp().getWriter();
					switch(view.getType()){
					case JSON : 
					    ObjectMapper mapper = new ObjectMapper() ;    
				        JsonGenerator gen = new JsonFactory().createJsonGenerator(out);   
				        mapper.writeValue(gen, view.getResult()); 
						;break;
					case HTML:
						String path = pluginName + File.separator +"templates"+File.separator+ view.getResult();
						
						
						Template template = cfg.getTemplate(path);
						
						ServletContext application = ActionContext.getApplication();
						HttpServletRequest request = ActionContext.getReq();
						
						Map<String,Object> root = new HashMap<String,Object>(); 
						
						
						root.put("encoder", new FrontURLRewriteMethodModel());//URL重写  
						root.put("list",  new UpperDirective());// 调用
						root.put("load", new LoadDirective());//
						root.put("plugin", new EmbedDirectiveInvokeTag());// 嵌入式指令插件
						@SuppressWarnings("unchecked")
						Enumeration<String> attrs3 = application.getAttributeNames(); 
						while (attrs3.hasMoreElements()) {
							String attrName = attrs3.nextElement();
							root.put(attrName, application.getAttribute(attrName));
						}
						//转移Session数据
						HttpSession session = request.getSession();
						@SuppressWarnings("unchecked")
						Enumeration<String> attrs2 = session.getAttributeNames();
						while (attrs2.hasMoreElements()) {
							String attrName = attrs2.nextElement();
							root.put(attrName, session.getAttribute(attrName));
						}
						//这里是进行数据转移
						@SuppressWarnings("unchecked")
						Enumeration<String> attrs = request.getAttributeNames(); 
						while (attrs.hasMoreElements()) {
							String attrName = attrs.nextElement();
							root.put(attrName, request.getAttribute(attrName));
						}

						
						 
						template.process(root, out);
					
						;break;
					default:
						break;
					} 
					out.flush();
					out.close();
				} 
			}
		}
		return null;
	}
	
	
	
	
	/**
	 * 调用嵌入式指令 
	 * @param pluginName
	 * @param directive
	 * @return String 
	 * @throws Exception
	 */
	public String invoke(String pluginName, String directive) throws Exception {
		ProxyPluginlet pluginProxy = pluginLets.get(pluginName);
		if(pluginProxy != null)
			return pluginProxy.invokeDrictive(directive);
		return "";
	}
	
	
	/**
	 * 获取当前维护的Pluginlet代理
	 * @return
	 */
	public Map<String, ProxyPluginlet> getPluginLet(){ 
		return pluginLets;
	}


	/**
	 * 获取当前插件集合
	 * @return
	 */
	public List<PluginInfo> getCurrentList() {
		List<PluginInfo> list = new ArrayList<PluginInfo>();
		Iterator<String> it = pluginLets.keySet().iterator();
		while(it.hasNext()){
			list.add(pluginLets.get(it.next()).getInfo());
		}
		return list;
	}
}
