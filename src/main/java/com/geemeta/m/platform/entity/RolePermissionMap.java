package com.geemeta.m.platform.entity;

import com.geemeta.core.entity.IdEntity;
import com.geemeta.core.gql.meta.Entity;
/**
 * Created by hongxq on 2015/6/17.
 */

@Entity(name = "sys_role_r_permission")
public class RolePermissionMap extends IdEntity {
    private Long roleId;

    private Long permissionId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
}
