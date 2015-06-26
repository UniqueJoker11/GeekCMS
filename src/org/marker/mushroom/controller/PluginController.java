package org.marker.mushroom.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.ext.ModuleFile;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.listener.FelixStart;
import org.marker.mushroom.plugin.PluginContext;
import org.marker.mushroom.support.SupportController;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;




/**
 * 插件控制器
 * @author marker
 * */
@Controller
@RequestMapping("/admin/plugin")
public class PluginController extends SupportController {

	
	
	public PluginController() {
		this.viewPath = "/admin/plugin/";
	}
	
	
	 
	// 显示插件接口列表和嵌入式指令列表
	@RequestMapping("/list")
	public ModelAndView list(){
		ModelAndView view = new ModelAndView(this.viewPath+"list");
		PluginContext pluginContext = PluginContext.getInstance();
		view.addObject("data",  pluginContext.getCurrentList());
		return view;
	}

	
	
	
	

	/**
	 * 安装插件
	 * @param path
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/install")
	public Object install(@RequestParam("path") String path, @RequestParam("name") String name){
		try {
			name = new String(name.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace();
		}
		File file = new File(WebRealPathHolder.REAL_PATH + path + File.separator + name);
		try {
			ModuleFile pfile = new ModuleFile(file); 
			String pluginsPath = WebRealPathHolder.REAL_PATH + "plugins" + File.separator;
			pfile.export(pluginsPath); // 导出
		 
			
			// 安装bundle
			
			
			BundleContext context = (BundleContext) application.getAttribute(FelixStart.OSGI_FELIX);
			if(context != null){
				String moduleName = pfile.getModuleName();
				
				String a = pluginsPath + moduleName+File.separator+"bundle";
				File jar = new File(a).listFiles()[0]; // 只读取一个jar
				if(jar.isFile()){
					// jar包过滤,占未实现 
					FileInputStream is = null;
					try{
						is = new FileInputStream(jar);
						Bundle bundle = context.installBundle(jar.getAbsolutePath(),is);
						bundle.start();
					}catch(Exception e){
						e.printStackTrace();
					}finally{ 
						is.close();
					} 
				} 
			
			}else{
				return new ResultMessage(false, "Felix not start"); 
			}
		} catch (IOException e) { 
			log.error("安装插件失败",e);
			return new ResultMessage(false, "插件安装失败!"); 
		} 
		return new ResultMessage(false, "插件安装成功!"); 
	} 
	 
}
