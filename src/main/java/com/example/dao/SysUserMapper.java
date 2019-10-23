package com.example.dao;

import com.example.data.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * DAO层接口
 */
@Mapper
public interface SysUserMapper {

    //根据id查询
    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    SysUser selectById(Integer id);
    //根据name查询
    @Select("SELECT * FROM sys_user WHERE name = #{name}")
    SysUser selectByName(String name);
}
