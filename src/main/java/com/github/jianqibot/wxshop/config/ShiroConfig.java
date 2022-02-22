package com.github.jianqibot.wxshop.config;

import com.github.jianqibot.wxshop.service.ShiroRealm;
import com.github.jianqibot.wxshop.service.UserLoginInterceptor;
import com.github.jianqibot.wxshop.service.UserService;
import com.github.jianqibot.wxshop.service.VerificationCodeCheckService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig implements WebMvcConfigurer {

    @Inject
    private UserService userService;
    @Inject
    private ShiroLoginFilter shiroLoginFilter;

    @Value("${shiro-redis.redis-manager.host}")
    private String redisHost;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> pattern = new HashMap<>();
        // no filter will be applied to anon
        pattern.put("/api/code", "anon");
        pattern.put("/api/login", "anon");
        pattern.put("/api/logout", "anon");
        pattern.put("/api/status", "anon");
        pattern.put("/api/v1/testRpc", "anon");
        pattern.put("/**", "authc");

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("shiroLoginFilter", shiroLoginFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(pattern);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SessionsSecurityManager securityManager(ShiroRealm shiroRealm, RedisCacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(new DefaultWebSessionManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost);
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }

    @Bean
    public ShiroRealm myShiroRealm(VerificationCodeCheckService codeCheckService) {
        return new ShiroRealm(codeCheckService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor(userService));
    }
}
