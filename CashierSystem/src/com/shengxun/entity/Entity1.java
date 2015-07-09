package com.shengxun.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Savion on 2015/7/2.
 */
@DatabaseTable(tableName = "entity1_table")
public class Entity1 {
    @DatabaseField(canBeNull = false)
    public String name;
    @DatabaseField(canBeNull = false,generatedId = true)
    public int id;
    public Entity1(){

    }
    public Entity1(String name){
        this.name = name;
    }
    public Entity1(String name,int id){
    	this.name = name;
    	this.id = id;
    }
}
