package com.demo.listener;

import com.demo.utils.SqlSessionFactoryUtils;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitSqlSessionListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent){
        System.out.println("容器加载中...");
        // 初始化我们的SqlSessionFactory对象
        SqlSessionFactoryUtils.initSqlSessionFactory();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent){
        System.out.println("容器销毁中...");
        // 关闭Sqlsession对象
        SqlSessionFactoryUtils.close();
    }

}
