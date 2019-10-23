package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: SecurityDemo
 * @Package: com.example.config
 * @ClassName: CustomAuthenticationSuccessHandler
 * @Author: shengshuli
 * @Description: 自定义登录成功的处理类
 * @Date: 2019/10/23 11:04
 * @Version: 1.0
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //打印用户登录后的认证信息
        logger.info("登录成功-{}",authentication);
        //重定向到登录成功的页面
        httpServletResponse.sendRedirect("/success");
    }
}
