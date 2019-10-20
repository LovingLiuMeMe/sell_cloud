package cn.lovingliu.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * @Author：LovingLiu
 * @Description: 跨域设置 C-cross O-origin R-Resource S-Sharing
 * @Date：Created in 2019-10-20
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);// 是否支持cookie跨域
        config.setAllowedOrigins(Arrays.asList("*"));// 设置原始域 如:http://www.lovingliu.cn
        config.setAllowedHeaders(Arrays.asList("*"));// 设置允许头
        config.setAllowedMethods(Arrays.asList("*"));// 设置允许方法 GET POST
        config.setMaxAge(300l);// 缓存时间: 对于相同的跨域请求 不再进行检查 单位:秒
        source.registerCorsConfiguration("  /**",config);
        return new CorsFilter(source);
    }
}
