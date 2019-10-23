package com.example.data;

import java.io.Serializable;

/**
 * 用户信息实体类
 */
public class SysUser implements Serializable {
    static final long serialVersionID = 1L;//版本序列号
    private Integer id;
    private String name;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
