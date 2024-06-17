package com.dbt.web;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dbt.framework.interceptor.AntiSqlInjectionIterceptor;
import com.dbt.framework.interceptor.SessionTimeoutInterceptor;
import com.dbt.framework.interceptor.TokenInterceptor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.TextCommandFactory;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

@Configuration
public class DefaultWebConfigurer implements WebMvcConfigurer {
    @Autowired
    private VjifenManageConfig manageConfig;

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //session拦截器
    	SessionTimeoutInterceptor si = new SessionTimeoutInterceptor();
    	
    	//添加不拦截路径
    	si.setAllowUrls(manageConfig.getSessionFreeUrl());

        // 参数长度1000接口配置
        si.setLongParameterLengthUrls(manageConfig.getParameterLengthLongUrl());

        // 不验证参数长度接口配置
        si.setUnlimitedParameterLengthUrls(manageConfig.getAntiParameterLengthFreeUrl());
    	
    	// 拦截
        InterceptorRegistration session = registry.addInterceptor(si);
        session.addPathPatterns("/*/*");

        //防sql注入拦截器
        AntiSqlInjectionIterceptor as = new AntiSqlInjectionIterceptor();
        
        // 添加不拦截
        as.setAllowUrls(manageConfig.getAntiSqlFreeUrl());
        
        // 拦截
        InterceptorRegistration antiSql = registry.addInterceptor(as);
        antiSql.addPathPatterns("/*/*");

        //Token拦截器
        TokenInterceptor tokenInterceptor = new TokenInterceptor(manageConfig);
        InterceptorRegistration tokenRegistration = registry.addInterceptor(tokenInterceptor);
        tokenRegistration.addPathPatterns("/auth/**");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .setUseSuffixPatternMatch(true)     //设置是否是开启后缀模式匹配,即:/test.*
                .setUseTrailingSlashMatch(true);    //设置是否自动后缀路径模式匹配,即：/test/
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(20);
        taskExecutor.setKeepAliveSeconds(200);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }

    @Bean(destroyMethod = "shutdown")
    public MemcachedClient memcachedClient() throws IOException {
        //memCache构建器
        List<InetSocketAddress> address = AddrUtil.getAddresses(manageConfig.getMemCacheAddress());
        XMemcachedClientBuilder builder = new XMemcachedClientBuilder(address, new int[]{1});
        //通信编码方式
        builder.setTranscoder(new SerializingTranscoder());
        //采用文本协议
        builder.setCommandFactory(new TextCommandFactory());
        return builder.build();
    }

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxJob() {
        if (StringUtils.isBlank(manageConfig.getXxJobAddresses())) return null;
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(manageConfig.getXxJobAddresses());
        executor.setAppName(manageConfig.getXxJobAppName());
        executor.setIp(manageConfig.getXxJobIp());
        executor.setPort(manageConfig.getXxJobPort());
        executor.setAccessToken(manageConfig.getXxJobAccessToken());
        executor.setLogPath(manageConfig.getXxJobLogPath());
        executor.setLogRetentionDays(manageConfig.getXxJobLogRetentionDays());
        return executor;
    }

    @Bean
    public MultipartResolver multipartResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxInMemorySize(10240);
        resolver.setUploadTempDir(new FileSystemResource("upload/"));
        resolver.setMaxUploadSize(-1);
        return resolver;
    }
}
