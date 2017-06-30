package com.geemeta.m.platform.entity;


import com.geemeta.core.entity.BaseEntity;
import com.geemeta.core.gql.meta.Entity;
import com.geemeta.core.gql.meta.Title;

/**
 * Created by hongxueqian on 14-4-12.
 */

@Entity(name = "sys_role")
@Title(title = "角色")
public class Role extends BaseEntity {
    private String name;
    private String code;
    private String description;

    @Title(title = "编码")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Title(title = "名称")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Title(title = "描述")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
