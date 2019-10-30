package com.example.smscode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

/**
* @author : ShengShuli
* @Date: 2019年10月25日
* @Description:短信登录的鉴权过滤-模仿UsernamePasswordAuthenticationFilter
*/
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter{

	//用户名为用户手机号，没有密码
	public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
	private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
	//是否仅为POST方式
	private boolean postOnly = true;


	public SmsCodeAuthenticationFilter() {
		//短信登录的请求
		super(new AntPathRequestMatcher("/sms/login", "POST"));
	}

	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}

		String mobile = obtainMobile(request);

		if (mobile == null) {
			mobile = "";
		}

		mobile = mobile.trim();

		//通过手机号创建令牌鉴权的认证请求
		SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	
	/**
	 *获得手机号
	 */
	@Nullable
	protected String obtainMobile(HttpServletRequest request) {
		return request.getParameter(mobileParameter);
	}

	/**
	 * Provided so that subclasses may configure what is put into the authentication
	 * request's details property.
	 *
	 * @param request that an authentication request is being created for
	 * @param authRequest the authentication request object that should have its details
	 * set
	 */
	protected void setDetails(HttpServletRequest request,
			SmsCodeAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	/**
	 *设置手机号参数
	 */
	public void setMobileParameter(String mobileParameter) {
		Assert.hasText(mobileParameter, "mobile parameter must not be empty or null");
		this.mobileParameter = mobileParameter;
	}

	/**
	 * Defines whether only HTTP POST requests will be allowed by this filter. If set to
	 * true, and an authentication request is received which is not a POST request, an
	 * exception will be raised immediately and authentication will not be attempted. The
	 * <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
	 * authentication.
	 * <p>
	 * Defaults to <tt>true</tt> but may be overridden by subclasses.
	 */
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getMobileParameter() {
		return mobileParameter;
	}


	
}
