package org.marker.mushroom.template.tags.impl;

import org.marker.mushroom.template.tags.AbstractTag;
import org.marker.mushroom.template.tags.ITag;

/**
 * 字符串截取标签 格式:
 * 
 * */
public class StringSubTagImpl extends AbstractTag implements ITag {

	/** 默认构造 */
	public StringSubTagImpl() {
		this.put("\\$\\{\\s*(\\w+\\.?\\w+)\\s+length\\=\\((\\d+)\\)\\s*\\}",
				"<#if ($1?length>$2)>" + "\\${$1[0..$2]}..." + "<#else>"
						+ "\\${$1!}" + "</#if>", 0);
	}

}
