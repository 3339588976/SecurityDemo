package com.example.smscode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.example.config.CustomAuthenticationFailureHandler;
import com.example.config.CustomAuthenticationSuccessHandler;

/**
* @author : ShengShuli
* @Date: 2019年10月25日
* @Description:将自定义的配置类添加到security框架中，类似于WebSecurityConfig的功能
*       为每种登录方式建立一个配置文件，将配置文件添加到WebSecurity中，实现解耦
*/
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity>{

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;
	@Autowired
	private CustomAuthenticationFailureHandler failureHandler;
	
	//实现配置的方法，将自定义的一些配置放进去
	@Override
	public void configure(HttpSecurity hs) throws Exception {
		//创建短信验证过滤器
		SmsCodeAuthenticationFilter smsFilter = new SmsCodeAuthenticationFilter();
		//指定鉴权管理
		smsFilter.setAuthenticationManager(hs.getSharedObject(AuthenticationManager.class));
		//设置处理类
		smsFilter.setAuthenticationSuccessHandler(successHandler);
		smsFilter.setAuthenticationFailureHandler(failureHandler);
		//Manager选中合适的Provider
		SmsCodeAuthenticationProvider smsProvider = new SmsCodeAuthenticationProvider();
		//provider鉴权（指定uds,通过loadUserByUsername获取用户信息）
		smsProvider.setUserDetailsService(userDetailsService);
		//将filter和provider都加入到HttpSecurity配置中
		hs.authenticationProvider(smsProvider).addFilterAfter(smsFilter, UsernamePasswordAuthenticationFilter.class);
		super.configure(hs);
	}
	
	
}
