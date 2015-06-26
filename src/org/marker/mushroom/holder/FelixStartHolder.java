package org.marker.mushroom.holder;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.framework.Felix;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.dao.ICommonDao;
import org.marker.mushroom.listener.FelixStart;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.beans.BeansException;
import org.springframework.web.context.ServletContextAware;




public class FelixStartHolder implements ServletContextAware {

	private static final Log log = LogFactory.getLog(FelixStart.class);

	public static final String OSGI_FELIX = "osgi_felix";
	
    // Felix实例
    private Felix felix;
    
    
    
	public void setServletContext(ServletContext application) { 
		
		
		
		
		ICommonDao dao = SpringContextHolder.getBean(SystemStatic.DAO_COMMON);
		
		
		
		dao.update("DROP TABLE IF EXISTS `guestbook`");
		
		
		
		String RootPath = application.getRealPath(File.separator);// 项目跟路径
		String SystemBundlesDir = RootPath+"WEB-INF"+File.separator+"osgi"+File.separator+"bundles";// 系统Bundles
		String cache = RootPath+"WEB-INF"+File.separator+"osgi"+File.separator+"cache";
		
		log.info("mrcms start felix osgi framework...");
	   	Map<String,Object> configMap = new HashMap<String,Object>();
	   	// 自动部署目录
        configMap.put("felix.auto.deploy.dir", SystemBundlesDir);
        // 缓存目录
        configMap.put("felix.cache.rootdir",cache);
       //  configMap.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, list);
        
        // 插件依赖包
        String commonPackage = 
        		"org.marker.mushroom.plugin;"
        		+ "org.slf4j;"
        		+ "javax.servlet.http;"
        		+ "org.marker.mushroom.beans;"
        		+ "org.marker.mushroom.holder;"
        		+ "org.marker.mushroom.dao;"
        		+ "org.marker.qqwryip;"
        		+ "org.marker.mushroom.core";
        
        
        configMap.put("org.osgi.framework.system.packages.extra",commonPackage);
        configMap.put("felix.log.level", "1");
        configMap.put("felix.auto.deploy.action", "install,start");
        
	        try {
	            // Now create an instance of the framework with
	            // our configuration properties.
	        	felix = new Felix(configMap);
	        	felix.init();
	        	 
	           
	        	felix.start();  // 启动felix实例

    			BundleContext context = felix.getBundleContext();
	        	application.setAttribute(OSGI_FELIX, context);
	        	

	        	String pluginPath = RootPath+"plugins/";
	        	
	        	
	        	
	        	// 自动扫描插件，并安装或者更新
	        	File file = new File(pluginPath);
	        	for(File f : file.listFiles()){
	    			String moduleName = f.getName();// 模块内容
	    			File bundleFolderFile = new File(f.getAbsolutePath()+File.separator+"bundle/");
	    			if(bundleFolderFile != null){ 
	    				if(bundleFolderFile.listFiles() != null &&  bundleFolderFile.listFiles().length > 0 ){
	    					File jar = bundleFolderFile.listFiles()[0]; // 只读取一个jar
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
	    				}
	    			}
	    			
	        		
	        	}
	        	
//	        	PluginConfig config = PluginConfig.getInstance();
//	        	
//	        	Set<Object>  sets = config.getProperties().keySet(); 
//	        	for(Object key : sets){
//					String value =  (String) config.getProperties().get(key);
//					String bundlePath = pluginPath+key+File.separator+"bundle"+File.separator+value; 
//					FileInputStream is = new FileInputStream(new File(bundlePath));
//					Bundle bundle = context.installBundle("",is);
//					bundle.start(); 
//	        	}
	        	
	        
//	        	 
//	        	
	        	
	        	Bundle[] bs = context.getBundles();
	        	for(Bundle b : bs){
	        		System.out.println(b.getBundleId()+" "+b.getSymbolicName()+"  "+b.getLocation()+"  "+b.getState());
	        	}
	        	
	        } catch (Exception e)  {
	        	log.error("Could not create osgi framework: ", e);
	        } 
		
	}
 
}
