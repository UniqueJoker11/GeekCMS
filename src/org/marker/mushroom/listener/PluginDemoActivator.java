package org.marker.mushroom.listener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PluginDemoActivator implements BundleActivator{
 
	
	@Override
	public void start(BundleContext context) throws Exception { 
		System.out.println("注册了插件服务...");
	 
	}

	@Override
	public void stop(BundleContext arg0) throws Exception { 
		System.out.println("卸载了插件服务...");
	 
	}

}
