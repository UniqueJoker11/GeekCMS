package org.marker.mushroom.template.tags.impl;

import org.marker.mushroom.template.tags.AbstractTag;
import org.marker.mushroom.template.tags.ITag;

/**
 * if判断标签 格式: <!--{if:${channel.id != current.id}}-->
 * 
 * <!--{else}-->
 * 
 * <!--{/if}-->
 * 
 * */
public class IfTagImpl extends AbstractTag implements ITag {

	/** 默认构造 */
	public IfTagImpl() {
		this.put(
				"\\s*<!--\\s*\\{if:[\\x20]*\\$\\{(.*)\\}[\\x20]*\\}\\s*-->[\\x20]*\\n?",
				"<#if ($1) >", 0);
		this.put("\\s*<!--\\s*\\{else\\}\\s*-->[\\x20]*\\n?", "<#else>", 0);
		this.put("\\s*<!--\\s*\\{/if\\}\\s*-->[\\x20]*\\n?", "</#if>\n", 0);
	}

}
