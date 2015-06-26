package org.marker.mushroom.template.tags.impl;

import org.marker.mushroom.template.tags.AbstractTag;
import org.marker.mushroom.template.tags.ITag;

/**
 * 执行时间标签 调用格式: <!--{executetime}--> 单位为秒
 * 
 * 已更新为Freemarker
 * 
 * @author marker
 * */
public class ExecuteTimeTagImpl extends AbstractTag implements ITag {

	/** 默认构造 */
	public ExecuteTimeTagImpl() {
		this.put("\\s*<!--\\s*\\{executetime\\}\\s*-->",
				"\\${(.now?long-_starttime)/1000} ", 0);
	}

}
