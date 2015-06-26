/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.core.loader;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.mushroom.core.exception.ModuleNotFoundException;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.springframework.stereotype.Service;

/**
 * 内容模型类加载器
 * @author marker
 * @date 2013-9-21 下午4:51:33
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
// 使用Servie注解将是单利模式，而组件注解是多实例
@Service("moduleClassLoader")
public class ModuleClassLoader {

	private static final Log log = LogFactory.getLog(ModuleClassLoader.class);
	
	//内容模型根路径
	private static String module_root;
	
	
	/** 内部加载器(必须唯一)  */
	private volatile static URLClassLoader urlClassLoader = null;     
	
	
	public ModuleClassLoader() {
		if(urlClassLoader == null){ 
			module_root = WebRealPathHolder.REAL_PATH + File.separator + "module";
	        URL[] urls = null;   
	        try {   
	            urls = getJarURLs(module_root);   
	        } catch (MalformedURLException e) {   
	            System.out.println(e.getMessage());   
	        }
	        //初始化 
	        urlClassLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());   
		}
    }   
	
	
	
	/**
	 * 获取jar包的URL对象(内部调用)
	 * @param rootDirStr
	 * @return
	 * @throws MalformedURLException
	 */
	private URL[] getJarURLs(String rootDirStr) throws MalformedURLException {   
        if (!rootDirStr.endsWith(File.separator)) {   
            rootDirStr += File.separator;
        }   
        
        List<URL> list = new ArrayList<URL>();
        
        // classesDir就是web应用中的"\module\"目录   
        File moduleDir = new File(rootDirStr);   
        
        File[] modules = moduleDir.listFiles();
        for(File module : modules){
        	if(module.isDirectory()){
	        	File[] module_lib_jars = getModuleJarFiles(module);
	        	for(File jar : module_lib_jars){
	        		URL url = toURL(jar);
	        		list.add(url);	 
	        	}
        	}
        }
        return list.toArray(new URL[0]);
    }
 
	
	private File[] getModuleJarFiles(File module){
		File module_lib_dir = new File(module + File.separator + "lib");
        if (module_lib_dir.isDirectory()) {   
        	File[] jarFiles = module_lib_dir.listFiles(new JarFileNameFilter());
        	return jarFiles; 
        }
		return null;
	}
	
	/**
	 * 转换File为URL对象
	 * @param file
	 * @return
	 * @throws MalformedURLException
	 */
	private URL toURL(File file) throws MalformedURLException{
		log.info("load module jar: "+ file);
		return file.toURI().toURL();
	}
	
	
	/*  
     *加载class,直接调用 myClassLoader的loadClass(className)方法  
     */  
    public Class<?> loadClass(String className) throws ModuleNotFoundException{
    	try{
            return urlClassLoader.loadClass(className);   
    	}catch (ClassNotFoundException e) {
    		throw new ModuleNotFoundException(className);
		}
    }



	/**
	 * 
	 */ 
	public void loadModuleJar(String moduleType) {
		String jars = module_root + File.separator + moduleType + File.separator + "lib" + File.separator;
		List<URL> list = new ArrayList<URL>(); 
        URL[] urls = new URL[0];   
        try {   
        	File module_lib_dir = new File(jars);
        	File[] module_lib_jars = module_lib_dir.listFiles(new JarFileNameFilter());
        	for(File jar : module_lib_jars){
        		URL url = toURL(jar);
        		list.add(url);	 
        	}
        	urls = list.toArray(urls);
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        }
        urlClassLoader = URLClassLoader.newInstance(urls, urlClassLoader); 
	}   
	
	
	
}

/**
 * jar包文件名过滤器
 * @author marker
 * @date 2013-9-21 下午5:10:05
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
class JarFileNameFilter implements FilenameFilter {
	
	@Override
	public boolean accept(File dir, String name) {
        // 注意"dir"参数指的是jar文件的父目录,"name"才是jar文件的   
        if (dir.isDirectory() // jar文件的父目录必须是一个文件夹   
                && (name.endsWith(".jar") || name.endsWith(".zip"))) // 注意zip文件也是可以的哦   
            return true;   
        return false;   
	}
	
}


