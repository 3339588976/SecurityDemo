package com.example.smscode;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
* @author : ShengShuli
* @Date: 2019年10月25日
* @Description: 由AuthenticationManager选中，处理验证逻辑
*/
public class SmsCodeAuthenticationProvider implements AuthenticationProvider{
	private UserDetailsService userDetailsService;
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	/**
	 * 判断authentication是不是SmsCodeAuthenticationToken的子类或子接口
	 * 处理该Provider怎么被AuthenticationManager选中
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}
	/**处理验证逻辑
	 * 1.将authentication强转为SmsAuthenticationToken
	 * 2.取出其中登录的principal，即手机号
	 * 3.进行验证码校验
	 * 4.数据库读取用户信息
	 * 5.验证完成，重新构造鉴权后的SmsAuthenticationToken,并返回给SmsAuthenticationFilter
	 * 6.doFilter处理登录成功或失败的handler
	 * 
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		//1
		SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
		//2
		String mobile = (String) authenticationToken.getPrincipal();
		//3.
		
		//4.
		UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);
		//5.鉴权成功后，重新new一个拥有鉴权的Token
		SmsCodeAuthenticationToken newAuthenticationToken = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
		newAuthenticationToken.setDetails(authenticationToken.getDetails());
		
		return newAuthenticationToken;
	}
	
	/**
	   *     通过手机号校验验证码
	   *     用户输入的手机号和验证码
	 * session存储的手机号和验证码
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void checkSmsCode(String mobile) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		//获取用户输入的CODE
		String inputCode = request.getParameter("smsCode");
		//获取session中的验证对象(包括code,mobile)
		Map<String,Object> smsCodeMap = (Map<String, Object>) request.getSession().getAttribute("smsCode");
		if(smsCodeMap == null) {
			throw new BadCredentialsException("没有申请到验证码");
		}
		//申请的手机号
		String applyMobile = (String) smsCodeMap.get("mobile");
		//申请的验证码
		int applyCode = (int) smsCodeMap.get("code");
		if(!mobile.equals(applyMobile)) {
			throw new BadCredentialsException("申请的手机号和登录手机不一致");
		}
		if(applyCode != Integer.parseInt(inputCode)) {
			throw new BadCredentialsException("验证码错误");
		}
	}

}
