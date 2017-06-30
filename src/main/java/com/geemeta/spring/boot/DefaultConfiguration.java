package com.geemeta.spring.boot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取属性文件信息
 * @author itechgee@126.com
 */
public class DefaultConfiguration implements ApplicationContextAware {

    protected ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.applicationContext = applicationContext;
    }

    public String getProperty(String key,String defaultValue){
        //"xgee.mybatis.mapperBasePackage"
        return applicationContext.getEnvironment().getProperty(key,defaultValue);
    }

}
