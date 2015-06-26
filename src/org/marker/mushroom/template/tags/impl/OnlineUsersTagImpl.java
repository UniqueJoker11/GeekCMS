package org.marker.mushroom.template.tags.impl;

import org.marker.mushroom.template.tags.AbstractTag;
import org.marker.mushroom.template.tags.ITag;

/**
 * 显示统计在线人数滴 调用代码：<!--{online}-->
 * 
 * @author marker
 * */
public class OnlineUsersTagImpl extends AbstractTag implements ITag {

	/** 默认构造 */
	public OnlineUsersTagImpl() {
		this.put("<!--\\s*\\{online}\\s*-->",
				"<#if sessions?exists>\\${sessions}<#else>1</#if>", 0);

	}

}
