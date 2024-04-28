package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.intercepter.LogIntercepter;
import hello.login.web.intercepter.LoginCheckIntercepter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;


@Configuration
public class WebConfig implements WebMvcConfigurer {


    /*@Bean*/
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
    /*@Bean*/
    public FilterRegistrationBean loginCheckFilter () {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
        filterFilterRegistrationBean.setOrder(2);
        filterFilterRegistrationBean.addUrlPatterns("/*");

        return filterFilterRegistrationBean;
    }

    //로그 필터는 addIntercerptors를 사용
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogIntercepter())
                .addPathPatterns("/**")
                .order(1)
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/fonts/**", "/favicon.ico","/error");

        registry.addInterceptor(new LoginCheckIntercepter())
                .addPathPatterns("/**")
                .order(2)
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/fonts/**", "/favicon.ico","/error");
    }
}
