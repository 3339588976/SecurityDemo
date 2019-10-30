package com.example.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
    *  成功退出登录的处理类
 * @author shengshuli
 *
 */
@Component
public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler{
	private Logger logger = LoggerFactory.getLogger(DefaultLogoutSuccessHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		String username = ((User)authentication.getPrincipal()).getUsername();
		logger.info("成功退出登录，用户:{}",username);
		//重定向到登录页
		response.sendRedirect("login.html");
	}

}
