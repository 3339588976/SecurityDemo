package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private SessionRegistry sessionRegistry;

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
    
    //主动踢出用户
    //当admin访问http://localhost:8080/kick?username=jitwxs时，踢出用户jitwxs
    @GetMapping("/kick")
    @ResponseBody
    public String removeSessionByUsername(@RequestParam String username) {
    	int count = 0;
    	//获取当前session所有的主体principles(用户信息)
    	List<Object> users = sessionRegistry.getAllPrincipals();
    	//获取指定用户的principle
    	for(Object principal : users) {
    		if(principal instanceof User) {
    			String principleName = ((User) principal).getUsername();
    			//判断输入的username和是否和当前主体的username相同
    			if(principleName.equals(username)) {
    				//获取该principle上的所有session,是否包含过期session
    				List<SessionInformation> sessionInfo = sessionRegistry.getAllSessions(principal, false);
    				if(null != sessionInfo && sessionInfo.size() > 0) {
    					for(SessionInformation session : sessionInfo) {
    						//使得session过期
    						session.expireNow();
    						//清理个数加一
    						count++;
    					}
    				}
    				
    			}
    		}
    	}
    	return "操作成功，清理session共 " + count + " 个";
    }
    
    //认证信息
    @RequestMapping("/me")
    @ResponseBody
    public Object me() {
    	return SecurityContextHolder.getContext().getAuthentication();
    }
    
    @RequestMapping("/me1")
    @ResponseBody
    public Object me1(Authentication auth) {
    	return auth;
    }
    
    @RequestMapping("/me2")
    @ResponseBody
    public Object me2(@AuthenticationPrincipal UserDetails user) {
    	return user;
    }
    
    //获取短信验证码
    @RequestMapping("/sms/code")
    @ResponseBody
    public void sms(String mobile,HttpSession session) {
    	//随机生成验证码
    	int code = (int) Math.ceil(Math.random() * 9000 + 100);
    	//创建验证信息对象
    	Map<String,Object> smsCode = new HashMap<String, Object>();
    	smsCode.put("code", code);
    	smsCode.put("mobile", mobile);
    	//放入session中
    	session.setAttribute("smsCode", smsCode);
    	logger.info("sessionID = {},mobile = {}的验证码为:{}",session.getId(),mobile,code);
    }
    
    
    
    
    

}
