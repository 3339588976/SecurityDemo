package com.example.config;

import com.example.data.SysPermission;
import com.example.data.SysRole;
import com.example.service.SysPermissionService;
import com.example.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @ProjectName: SecurityDemo
 * @Package: com.example.config
 * @ClassName: CustomPermissionEvaluator
 * @Author: shengshuli
 * @Description: 自定义PermissionEvaluator,重写hasPermission
    用户Authorities-用户角色-用户权限集合
 * @Date: 2019/10/22 19:20
 * @Version: 1.0
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysPermissionService sysPermissionService;

    //重写该方法
    //authentication:用户的权限身份（ROLE_ADMIN/ROLE_USER）
    //targetUrl:访问url 如：/admin
    //targetPermission:访问权限 如:r
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object targetPermission) {
        //获取loaUserByUsername方法的结果-当前用户
        User user = (User) authentication.getPrincipal();
        //获取loadUserByUsername中注入的角色
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        //遍历用户所有角色(当局)
        for(GrantedAuthority authority:authorities){
            //当局的角色名称(ROLE_ADMIN/ROLE_USER)
            String roleName = authority.getAuthority();
            //角色id
            Integer roleId = sysRoleService.selectByName(roleName).getId();
            //角色的权限集合
            List<SysPermission> permissionList = sysPermissionService.listByRoleId(roleId);
            //遍历权限集合 ，进行比较
            for(SysPermission sysPermission : permissionList){
                //权限集(c,r,u,d)逗号分隔
                List permissions = sysPermission.getPermissions();
                //如果访问的url和用户对应的url符合并且权限集包含目标权限，则返回true
                if (targetUrl.equals(sysPermission.getUrl()) && permissions.contains(targetPermission)) {
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
