package com.example;

import com.example.config.VerifyServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.example.dao")
public class SecurityDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityDemoApplication.class, args);
        System.out.println("启动程序....");
    }

    /**
     * 注入验证码Servlet
     */
    @Bean
    public ServletRegistrationBean indexServletRegisterBean(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new VerifyServlet());
        registrationBean.addUrlMappings("/getVerifyCode");
        return registrationBean;
    }

}
