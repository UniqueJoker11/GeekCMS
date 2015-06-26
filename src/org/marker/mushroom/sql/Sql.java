package org.marker.mushroom.sql;


public interface Sql {
	
	/** 查询 */
	String QUERY_FOR_FORM = "select * from ";
	
	/** 主表别名 */
	String QUERY_FOR_ALIAS = " A ";
	/** 主表别名点 */
	String QUERY_FOR_ALIAS_DOT = " A.";
	
	String QUERY_FOR_WHERE = " where ";
	
	String QUERY_FOR_AND = " and ";
	
	String QUERY_FOR_ORDERBY = " order by ";
}
