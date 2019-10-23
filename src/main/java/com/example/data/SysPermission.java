package com.example.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @ProjectName: SecurityDemo
 * @Package: com.example.data
 * @ClassName: SysPermission
 * @Author: shengshuli
 * @Description: 权限实体类
 * @Date: 2019/10/22 18:56
 * @Version: 1.0
 */
public class SysPermission implements Serializable {
    static final long serialVersionID = 1L;
    private Integer id;
    private String url;
    private Integer roleId;
    private String permission;
    private List permissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    //特殊处理权限集合：c,r,u,d
    public List getPermissions() {
        return Arrays.asList(this.permission.trim().split(","));
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }
}
