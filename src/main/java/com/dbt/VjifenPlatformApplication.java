package com.dbt;

import com.dbt.framework.util.PropertiesUtil;
import com.vjifen.server.base.module.login.service.SingleAccountService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

//管理平台包 com.dbt
//消息平台包 com.vjifen.module.message.scan
@SpringBootApplication(scanBasePackages = {"com.dbt", "com.vjifen.module.message.scan"})
//实例化mapper接口
@MapperScan("com.dbt.**.dao")
//根据xml注入bean
@Slf4j
@ImportResource(locations= {"classpath:applicationContext.xml"})
public class VjifenPlatformApplication extends SpringBootServletInitializer {
	public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(VjifenPlatformApplication.class, args);
        Environment env = run.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 运行成功! 访问连接: " +
                        "Swagger文档: \t\thttp://{}:{}/VjifenCOM\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                "127.0.0.1",
                env.getProperty("server.port"));
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VjifenPlatformApplication.class);
    }
    @Bean
    public SingleAccountService singleAccount() {
        return new SingleAccountService(PropertiesUtil.getPropertyValue("single_account_service"));
    }
}
