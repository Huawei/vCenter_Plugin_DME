package com.huawei.dmestore.task;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * ApplicationContextHelper
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * ApplicationContextHelper
     */
    public ApplicationContextHelper() {
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    /**
     * getBean
     *
     * @param param beanName
     * @return Object
     */
    public static Object getBean(final String param) {
        return applicationContext != null ? applicationContext.getBean(param) : null;
    }
}
