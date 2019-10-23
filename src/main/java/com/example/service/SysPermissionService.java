package com.example.service;

import com.example.dao.SysPermissionMapper;
import com.example.data.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: SecurityDemo
 * @Package: com.example.service
 * @ClassName: SysPermissionService
 * @Author: shengshuli
 * @Description: 权限服务层
 * @Date: 2019/10/22 19:04
 * @Version: 1.0
 */
@Service
public class SysPermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    //获取指定角色的所有权限
    public List<SysPermission> listByRoleId(Integer roleId){
        return sysPermissionMapper.listByRoleId(roleId);
    }
}
