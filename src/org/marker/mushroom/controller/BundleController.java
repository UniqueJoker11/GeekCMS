package org.marker.mushroom.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import javax.servlet.ServletContext;

import org.marker.mushroom.beans.BundleInfo;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.config.impl.PluginConfig;
import org.marker.mushroom.ext.ModuleFile;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.listener.FelixStart;
import org.marker.mushroom.support.SupportController;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Bundles管理器
 * @author marker
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/bundle")
public class BundleController extends SupportController {

	@Autowired private ServletContext application;
	
	public BundleController(){ 
		this.viewPath = "/admin/bundle/";
	}
	
	
	
	@RequestMapping("/list")
	public ModelAndView list(){
		BundleContext context = (BundleContext) application.getAttribute(FelixStart.OSGI_FELIX);
		
		ModelAndView view = new ModelAndView(this.viewPath+"list");
		List<BundleInfo> list = new ArrayList<BundleInfo>();
		for(Bundle bundle : context.getBundles()){
			Dictionary<String, String> binfo = bundle.getHeaders();
			 
			BundleInfo info = new BundleInfo();
			info.setId(bundle.getBundleId());
			info.setName(binfo.get("Bundle-Name"));
			info.setVersion(binfo.get("Bundle-Version"));
			info.setVendor(binfo.get("Bundle-Vendor"));
			info.setDescription(binfo.get("Bundle-Description")); 
			info.setState(bundle.getState());//状态
			 
			list.add(info);
		}
		
		view.addObject("bundles", list);
		return view;
		
	}
	
	
//
//	/**
//	 * 安装bundle
//	 * @param path
//	 * @param name
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/install")
//	public Object install(@RequestParam("path") String path, @RequestParam("name") String name){
//		try {
//			name = new String(name.getBytes("iso-8859-1"),"utf-8");
//		} catch (UnsupportedEncodingException e) { 
//			e.printStackTrace();
//		}
//		File file = new File(WebRealPathHolder.REAL_PATH + path + File.separator + name);
//		try {
//			ModuleFile pfile = new ModuleFile(file);
//			if(!pfile.isInvalid()){// 有效
//				String pluginsPath = WebRealPathHolder.REAL_PATH + "plugins"+File.separator;
//				pfile.export(pluginsPath); // 导出
//				PluginConfig config = PluginConfig.getInstance();
//				config.set(pfile.getLabel(), pfile.getBundle()); 
//				config.store();
//				
//				// 安装bundle
//				String bundleJar = pluginsPath +pfile.getLabel()+File.separator+ pfile.getBundle();
//				
//				BundleContext context = (BundleContext) application.getAttribute(FelixStart.OSGI_FELIX);
//				
//				FileInputStream is = new FileInputStream(new File(bundleJar));
//			 
//				try {
//					Bundle bundle = context.installBundle("",is);
//					bundle.start(); 
//				} catch (BundleException e) { 
//					e.printStackTrace();
//				} 
//			} 
//		} catch (IOException e) { 
//			log.error("", e);
//			return new ResultMessage(false, "插件安装失败!"); 
//		}
//		return new ResultMessage(false, "插件安装成功!"); 
//	} 
	
	
	
	/**
	 *  停止某个bundle
	 * @param id
	 * @return
	 */
	@RequestMapping("/stop") 
	public Object stop(@RequestParam("id") long id){
		BundleContext context = (BundleContext) application.getAttribute(FelixStart.OSGI_FELIX);
		
		Bundle bundle = context.getBundle(id);// 停止
		try {
			bundle.stop();
		} catch (BundleException e) {
			log.error("stop bundleId={}",bundle.getBundleId(), e); 
		} 
		return list(); 
	} 
	
	
	/**
	 *  停止某个bundle
	 * @param id
	 * @return
	 */
	@RequestMapping("/start") 
	public Object start(@RequestParam("id") long id){
		BundleContext context = (BundleContext) application.getAttribute(FelixStart.OSGI_FELIX);
		Bundle bundle = context.getBundle(id);
		try {
			bundle.start();
		} catch (BundleException e) { 
			log.error("start bundleId={}",bundle.getBundleId(), e); 
		}
		return list();
	}
	
	
	/**
	 * 卸载某个bundle
	 * @param id
	 * @return
	 */
	@RequestMapping("/uninstall")
	@ResponseBody
	public Object uninstall(@RequestParam("id") long id){
		BundleContext context = (BundleContext) application.getAttribute(FelixStart.OSGI_FELIX);
		Bundle bundle = context.getBundle(id); 
		try {
			bundle.uninstall();
		} catch (BundleException e) { 
			log.error("start bundleId={}",bundle.getBundleId(), e); 
		}
		return list(); 
	}
	
	
}


