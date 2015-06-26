package org.marker.mushroom.plugin;

import java.io.Serializable;
import java.util.Dictionary;

import org.osgi.framework.Bundle;

/**
 * 插件Bean
 * @author marker
 * */ 
public class PluginInfo implements Serializable{
	private static final long serialVersionUID = -4808038290887993182L;
	
	private long bundleId;
	/** 插件名称 */
	private String name;
	/** 作者 */
	private String author;
	/** 版本 */
	private String version;
	/** 描述 */
	private String description; 
	
	/** bundle */
	private Bundle bundle;
	
	
	
	public PluginInfo(Bundle bundle) {
		this.bundle = bundle;
		// 获取bundle的描述文件信息
		Dictionary<String, String> binfo = bundle.getHeaders();
				 
		setBundleId(bundle.getBundleId());
		setName(binfo.get("Bundle-Name"));
		setVersion(binfo.get("Bundle-Version"));
		setAuthor(binfo.get("Bundle-Vendor"));
		setDescription(binfo.get("Bundle-Description")); 
		 
	}
	
	
	
	
	
	public long getBundleId() {
		return bundleId;
	}
	public void setBundleId(long bundleId) {
		this.bundleId = bundleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * 获取状态
	 * @return
	 */
	public int getState() {
		return bundle.getState();
	} 
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	 
	
}
