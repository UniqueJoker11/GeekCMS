package org.marker.mushroom.beans;

import java.io.Serializable;
import java.util.Date;

import org.marker.mushroom.dao.annotation.Entity;

 



/**
 * 文章对象
 * @author marker
 * */
@Entity("article")
public class Article implements Serializable {
	private static final long serialVersionUID = -2456959238880328330L;
	
	/** 文章ID */
	private Integer id;
	/** 所属栏目ID */
	private Integer pid;
	/** 标题 */
	private String title;
	/** 关键字 */
	private String keywords;
	/** 描述 */
	private String description;
	/** 作者 */
	private String author;
	/** 浏览量 */
	private int views;
	/** 内容 */
	private String content;
	/** 发布时间 */
	private Date time;
	
	/* 发布状态：0草稿 1发布*/
	private int status;
	
	
	
 
 
 
 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param pid the pid to set
	 */
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	 
	/**
	 * @return the pid
	 */
	public Integer getPid() {
		return pid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public long getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
 
}
