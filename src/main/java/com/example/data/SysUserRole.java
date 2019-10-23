package com.example.data;

import java.io.Serializable;

/**
 * 用户-角色实体类
 */
public class SysUserRole implements Serializable {
    static final long serialVersionID = 1L;
    private Integer userId;
    private Integer roleId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
