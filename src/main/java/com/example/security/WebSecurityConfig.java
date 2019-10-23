package com.example.security;

import com.example.config.CustomAuthenticationFailureHandler;
import com.example.config.CustomAuthenticationSuccessHandler;
import com.example.config.CustomExpireSessionStrategy;
import com.example.config.CustomPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //注入自定义UserDetailsService
    @Autowired
    private CustomUserDetailsService userDetailsService;
    //注入datasource
    @Autowired
    private DataSource dataSource;
    //注入自定义的认证成功处理类
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
    //注入自定义的认证失败处理类
    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    //创建Bean
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    //注册自定义的PermissionEvaluator
    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return handler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 如果有允许匿名的url，填在下面
               .antMatchers("/getVerifyCode").permitAll()
                .antMatchers("/login/invalid").permitAll()
                .anyRequest().authenticated()
                .and()
                // 设置登陆页
                .formLogin().loginPage("/login")
                // 设置登陆成功页- home.html
                //.defaultSuccessUrl("/success").permitAll()
                //登录失败的url
                //.failureUrl("/login/error")
                // 自定义登陆用户名和密码参数，默认为username和password
//                .usernameParameter("username")
//                .passwordParameter("password")
                //.and()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                //.logout()
                .permitAll()
                //自动登录
                .and().rememberMe()
                //数据库存储自动登录
                .tokenRepository(persistentTokenRepository())
                //有效时间
                .tokenValiditySeconds(60)
                .userDetailsService(userDetailsService)
                //session过时后跳转
                .and().sessionManagement().invalidSessionUrl("/login/invalid")
                //允许最大登录数
                .maximumSessions(1)
                //当达到最大值时，是否保留已登录用户
                .maxSessionsPreventsLogin(false)
                //当达到最大值时，旧用户被踢出的操作
                .expiredSessionStrategy(new CustomExpireSessionStrategy())
        ;
        // 关闭CSRF跨域
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
    }


}

