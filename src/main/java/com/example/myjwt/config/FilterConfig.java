package com.example.myjwt.config;

import com.example.myjwt.filter.SAfterFilter;
import com.example.myjwt.filter.SAfterFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<SAfterFilter> sAfterFilter(){
        FilterRegistrationBean<SAfterFilter> bean = new FilterRegistrationBean<>(new SAfterFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); //우선순위 적용
        //여기서 만든 필터들은 시큐리티 필더보다 우선순위가 낮아서 스큐리티 필터가 끝난뒤 실행이 된다.
        return bean;
    }

    @Bean
    public FilterRegistrationBean<SAfterFilter2> sAfterFilter2(){
        FilterRegistrationBean<SAfterFilter2> bean = new FilterRegistrationBean<>(new SAfterFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1); //우선순위 적용
        //여기서 만든 필터들은 시큐리티 필더보다 우선순위가 낮아서 스큐리티 필터가 끝난뒤 실행이 된다.
        return bean;
    }
}
