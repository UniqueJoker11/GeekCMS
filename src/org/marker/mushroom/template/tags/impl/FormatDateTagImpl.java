package org.marker.mushroom.template.tags.impl;

import org.marker.mushroom.template.tags.AbstractTag;
import org.marker.mushroom.template.tags.ITag;

/**
 * 时间格式标签 调用格式: ${dsad format=(yy-DD-MM)}
 * 
 * 
 * @author marker
 * */
public class FormatDateTagImpl extends AbstractTag implements ITag {
	/** 默认构造 */
	public FormatDateTagImpl() {
		this.put("\\$\\{\\s*([a-z.]+)\\s+format\\=\\(([a-z A-Z:-]+)\\)\\s*\\}",
				"<\\#if $1?exists>\\${$1?string(\"$2\")}</\\#if>", 0);

	}

}
