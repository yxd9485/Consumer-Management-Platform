package com.dbt.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryUtil implements ApplicationContextAware{
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        
    }

    /**
     * 从servlet中获取bean
     * @param beanname
     * @return
     */
    public static Object getbeanFromWebContext(String beanname){
            Object bean = applicationContext.getBean(beanname);
            return bean;
    }
    
    public static <T> T getBean(Class<T> cls) {
        return applicationContext.getBean(cls);
    }
    
}
