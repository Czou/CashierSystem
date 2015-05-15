package com.shengxun.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 地区信息实体
 * @author sw
 * @date 2015-4-23
 */
@DatabaseTable(tableName = "areaInfoTable")
public class AreaInfo {

	/**
	 * 地区ID
	 */
	@DatabaseField(id=true)
	public String aid;
	/**
	 * 上级地区ID
	 */
	@DatabaseField
	public String pid;
	/**
	 * 地区名称
	 */
	@DatabaseField
	public String name;
	/**
	 * 地区等级
	 */
	@DatabaseField
	public String level;
	
	@Override
	public String toString() {
		return "AreaInfo [aid=" + aid + ", pid=" + pid + ", name=" + name
				+ ", level=" + level + "]";
	}
	
	
}
