package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: SecurityDemo
 * @Package: com.example.config
 * @ClassName: CustomAuthenticationFailureHandler
 * @Author: shengshuli
 * @Description: 自定义登录失败处理类
 * @Date: 2019/10/23 11:13
 * @Version: 1.0
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        logger.info("登录失败");
        //设置失败状态
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        //设置文本类型
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        //打印失败信息
        String errorInfo = objectMapper.writeValueAsString(e.getMessage());
        httpServletResponse.getWriter().write(errorInfo);
    }
}
