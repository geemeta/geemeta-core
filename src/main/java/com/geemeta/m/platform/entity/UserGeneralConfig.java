package com.geemeta.m.platform.entity;

import com.geemeta.core.entity.BaseEntity;
import com.geemeta.core.gql.meta.Entity;
import com.geemeta.core.gql.meta.Title;

/**
 * Created by hongxueqian on 14-5-2.
 */
@Entity(name = "sys_user_general_config")
@Title(title = "用户配置")
public class UserGeneralConfig extends BaseEntity {
    private String ownerId;
    private String theme;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
