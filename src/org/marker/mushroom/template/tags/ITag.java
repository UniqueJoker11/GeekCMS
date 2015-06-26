package org.marker.mushroom.template.tags;

import org.marker.mushroom.core.exception.SystemException;

/**
 * 自定义标签接口
 * 
 * @author marker
 * */
public interface ITag {

	// 初始化内容
	void iniContent(String content);

	/**
	 * 执行标签替换
	 * @throws SystemException 在处理标签的时候可能抛出此异常
	 */
	void doTag() throws SystemException;
 
	
    void doDataTag(MatchRule mr) throws SystemException;

	
	/**
	 * 获取编译结果 
	 * @return String 字符串
	 * */
	String getContent();
}
