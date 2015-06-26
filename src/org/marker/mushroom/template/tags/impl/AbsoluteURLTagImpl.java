package org.marker.mushroom.template.tags.impl;

import org.marker.mushroom.template.tags.AbstractTag;

/**
 * URL绝对路径重写标签 作用于主题目录：images、css、js、static文件夹 格式:
 * 
 * 
 * @author marker
 * @date 2013-8-24 下午12:38:13
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class AbsoluteURLTagImpl extends AbstractTag {

	/** 默认构造 */
	public AbsoluteURLTagImpl() {
		this.put(
				"(src=|href=|background=)[\"\']((?!http://)(?!/)(?!\\$)(?!\\#).+)[\"\']",
				"$1\"\\${url}/\\${config.themes_path}$2\"", 0);
	}

}
