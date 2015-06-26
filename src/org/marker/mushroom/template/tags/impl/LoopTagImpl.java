package org.marker.mushroom.template.tags.impl;

import org.marker.mushroom.template.tags.AbstractTag;


/**
 * 遍历当前栏目下的文章集合标签
 * 说明：采用解析结构生成SQL语句进行进一步的查询，最后使用MVC模式传递数据
 * 格式：
 * <code>
 *  <!-- {list:var=(channel) items=(channels) table=(channel) pid=(0) page=(1) limit=(10)} --> 
 *		<a href="${channel.url}">${channel.name}</a>
 *	<!-- {/list} -->
 * </code>
 * 2013-01-18: 调整了输出HTML代码的格式，增加可读性
 * 2013-01-20 根据item参数来存取SQLDataEngqin
 * 
 * @author marker
 * @date 2013-01-17
 */
public class LoopTagImpl extends AbstractTag{

	/** 默认构造 */
	public LoopTagImpl(){
		this.put("\\s*<!--\\s*?\\{([a-zA-Z_]+):loop\\}\\s*?-->[\\x20]*\\n?",
				"<#list page.data as $1>\n",0);
		this.put("\\s*<!--\\s*\\{/loop\\}\\s*-->[\\x20]*\\n?", 
				"</#list>\n",0);
	}
	
}
