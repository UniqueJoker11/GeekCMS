package org.marker.mushroom.listener;

import java.io.File;

public class TestStar {

	public static void main(String[] args) {
		
		File file = new File("D:\\works\\myeclipse10\\mushroom\\WebRoot\\plugins");
		for(File f : file.listFiles()){
			String moduleName = f.getName();// 模块内容
			File bundleFolderFile = new File(f.getAbsolutePath()+File.separator+"bundle/");
			if(bundleFolderFile != null){ 
				
				for(File jar : bundleFolderFile.listFiles()){
					if(jar.isFile()){
						// jar包过滤
						String bundleFile = jar.getAbsolutePath();
						
						
					}
				}
			}
			
    		
    	}
		
	}
}
