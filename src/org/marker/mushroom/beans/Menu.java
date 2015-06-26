package org.marker.mushroom.beans;

import java.io.Serializable;

import org.marker.mushroom.dao.annotation.Entity;


@Entity("user_menu")
public class Menu implements Serializable {

	private static final long serialVersionUID = 8538161497615816793L;

	/** ID */
	private int id;
	/** 上级ID */
	private int pid;
	private String icon;
	private String name;
	private String url;
	private int sort;
	private String description;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
}
