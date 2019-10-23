package com.example.service;

import com.example.dao.SysUserMapper;
import com.example.data.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.IntegerSyntax;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    public SysUser selectById(Integer id){
        return sysUserMapper.selectById(id);
    }
    public SysUser selectByName(String name){
        return sysUserMapper.selectByName(name);
    }
}
