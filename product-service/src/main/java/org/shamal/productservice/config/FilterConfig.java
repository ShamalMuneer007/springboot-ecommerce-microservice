package org.shamal.productservice.config;
import org.shamal.productservice.filters.RoleAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
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
