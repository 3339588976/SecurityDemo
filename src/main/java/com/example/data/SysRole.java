package com.example.data;

import java.io.Serializable;

/**
 * 角色实体类
 */
public class SysRole implements Serializable {
    static final long serialVersionID = 1L;
    private Integer id;
    private String name;

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
}
