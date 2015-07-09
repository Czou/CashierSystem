package com.shengxun.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Savion on 2015/7/2.
 */
@DatabaseTable(tableName = "entity2_table")
public class Entity2 {

    @DatabaseField
    public String name;
    @DatabaseField(canBeNull = false,generatedId = true)
    public int id;
    public Entity2(){}
    public Entity2(String name){
        this.name = name;
    }
}
