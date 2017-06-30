package com.geemeta.m.platform.entity;

import com.geemeta.core.entity.BaseEntity;
import com.geemeta.core.gql.meta.Col;
import com.geemeta.core.gql.meta.Entity;
import com.geemeta.core.gql.meta.Title;
import com.geemeta.core.gql.meta.Transient;

/**
 * Created by hongxueqian on 14-4-12.
 */

@Entity(name = "sys_user")
@Title(title = "用户")
public class User extends BaseEntity {
    private String name;
    private String loginName;
    private String password;
    private String salt;
    private String avatar;
    private String description;
    private String plainPassword;

    public User() {
    }

    public User(Long id) {
        this.setId(id);
    }

    @Title(title = "名称")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Title(title = "登录名")
    @Col(name = "login_name")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Title(title = "密码")
//    @JsonDeserialize(using = JsonIgnoreDeserialize.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Title(title = "Salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Title(title = "头像")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Title(title = "描述")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Title(title = "明文密码")
    // 不持久化到数据库，也不显示在Restful接口的属性.
    @Transient
    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

}
