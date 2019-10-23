package com.example.service;

import com.example.dao.SysRoleMapper;
import com.example.data.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    public SysRole selectById(Integer id){
        return sysRoleMapper.selectById(id);
    }
    public SysRole selectByName(String roleName){
        return sysRoleMapper.selectByName(roleName);
    }

}
