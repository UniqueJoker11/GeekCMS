package org.marker.mushroom.template.tags.impl;

import org.marker.mushroom.template.tags.AbstractTag;
import org.marker.mushroom.template.tags.ITag;

/**
 * 插件调用标签 <!--{pulgin: name=(comment) invoke=(adsads)}-->
 * */
public class PluginTagImpl extends AbstractTag implements ITag {

	/** 默认构造 */
	public PluginTagImpl() {
		this.put(
				"<!--\\s*\\{\\s*pulgin:\\s*name=\\((\\w+)\\)\\s*invoke=\\((\\w+)\\)\\s*\\}\\s*-->",
				"<%=YLPluginFactory.getInstance().get(\"$1\").initialize(request,response,application).$2()%>",
				0);

	}

}
