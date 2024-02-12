package org.shamal.cartservice.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.shamal.cartservice.filters.RoleAuthFilter;
import org.shamal.util.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig{
    @Autowired
    RoleAuthFilter roleAuthFilter;

    @Bean
    public FilterRegistrationBean<RoleAuthFilter> loggingFilter(){
        FilterRegistrationBean<RoleAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(roleAuthFilter);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
