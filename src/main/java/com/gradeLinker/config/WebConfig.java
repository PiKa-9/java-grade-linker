package com.gradeLinker.config;

import com.gradeLinker.web.filter.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    private final UserFilter userFilter;

    @Autowired
    public WebConfig(UserFilter userFilter) {
        this.userFilter = userFilter;
    }

    @Bean
    public FilterRegistrationBean<UserFilter> userFilterRegistration() {
        FilterRegistrationBean<UserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(userFilter);

        // Specify URL patterns to which the filter should be applied
        registrationBean.addUrlPatterns("/h", "/create-course", "/join-course", "/c/*");

        return registrationBean;
    }
}