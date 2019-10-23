package com.example.dao;

import com.example.data.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ProjectName: SecurityDemo
 * @Package: com.example.dao
 * @ClassName: SysPermissionMapper
 * @Author: shengshuli
 * @Description: 权限DAO
 * @Date: 2019/10/22 19:00
 * @Version: 1.0
 */
@Mapper
public interface SysPermissionMapper {

    //根据角色id，查找权限集合
    //指定角色的所有权限
    @Select("select * from sys_permission where role_id = #{roleId}")
    List<SysPermission> listByRoleId(Integer roleId);
}
