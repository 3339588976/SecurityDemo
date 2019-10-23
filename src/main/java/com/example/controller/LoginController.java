package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    //登录页面
    @RequestMapping("/login")
    public String showLogin(){
        return "login.html";
    }
    //session过时跳转
    @RequestMapping("/login/invalid")
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String invalid(){
        return "Session 已超时，请重新登录！";
    }

    //认证成功-但不一定授权成功
    @RequestMapping("/success")
    public String showHome(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("当前登录用户：" + name);
        return "home.html";
    }

    //认证失败
    @RequestMapping("/login/error")
    public void showError(HttpServletRequest request, HttpServletResponse response){
        //设置文本类型
        response.setContentType("text/html;charset=utf-8");
        //权限异常信息
        AuthenticationException exception = (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        //打印出异常信息
        try {
            response.getWriter().write("print error info is :" + exception.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@PreAuthorize判断用户是否有指定权限，没有就不能访问
    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin(){
        return "ROLE_ADMIN角色登录";
    }

    @RequestMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public String printUser(){
        return "ROLE_USER角色登录";
    }

    //指定角色的所有权限问题
    //hasPermission(访问url,权限)
    @RequestMapping("/admin1")
    @ResponseBody
    @PreAuthorize("hasPermission('/admin','r')")
    public String printAdmin1(){
        return "ROLE_ADMIN访问/admin路径的权限为r";
    }

    @RequestMapping("/user1")
    @ResponseBody
    @PreAuthorize("hasPermission('/user','c')")
    public String printUser1(){
        return "ROLE_USER访问/user路径的权限为c";
    }

}
