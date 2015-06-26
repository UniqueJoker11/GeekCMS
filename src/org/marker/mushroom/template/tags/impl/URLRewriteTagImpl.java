package org.marker.mushroom.template.tags.impl;

import org.marker.mushroom.template.tags.AbstractTag;
import org.marker.mushroom.template.tags.ITag;

/**
 * URL重写标签 格式:
 * @author marker
 * @date 2013-8-24 下午12:47:32
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class URLRewriteTagImpl extends AbstractTag implements ITag {
	/** 默认构造 */
	public URLRewriteTagImpl() {
		// \\\\{?\\w+\\}?\\.?\\w*\\??[\\w+\\-?\\=?\\$?\\{?\\w+\\.\\}?&?]*
		this.put("href\\=[\"\']\\$\\{(\\w+.\\w+)\\}[\'\"]",
				"href=\"\\${url}\\${encoder($1)}\"", 0);

	}

}
