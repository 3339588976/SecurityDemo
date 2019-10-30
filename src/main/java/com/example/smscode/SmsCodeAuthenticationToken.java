package com.example.smscode;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
* @author : ShengShuli
* @Date: 2019年10月25日
* @Description: 短信验证码登录的Token
*/
@SuppressWarnings("serial")
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken{

	//principle当前认证主体代表用户登录的手机号
	private Object principal;
	
	/**
	    * 构建一个没有鉴权的Token-false
	 * @param authorities
	 */
	public SmsCodeAuthenticationToken(Object principal) {
		super(null);
		this.principal = principal;
		setAuthenticated(false);
	}
	/**
	 * 构建一个有鉴权的Token-true
	 * @param authorities:鉴权
	 */
	public SmsCodeAuthenticationToken(Object principal,Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		super.setAuthenticated(true);
	}
	//获取认证
	@Override
	public Object getCredentials() {
		return null;
	}
	//获取登录主体
	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	//设置鉴权
	@Override
	public void setAuthenticated(boolean authenticated) {
		//如果已经鉴权，抛出异常：不能设置受信任的令牌，使用鉴权列表的构造函数替代
		if(authenticated) {
			throw new IllegalArgumentException("cannot set the token to trusted,use constructor which take a GrantAuthority list instead");
		}
		super.setAuthenticated(false);
	}
	//删除认证
	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
	}

}
