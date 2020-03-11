package com.edu.esedu.service;

import javafx.application.Application;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author wangcunlei
 * @Date 2020-1-3 14:11
 * @Description
 */
@Component
public class InitComponent implements ApplicationRunner {
    @Autowired
    private EsService esService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean b  = esService.indexExist("msg_detail");
        if(!b){
            esService.addIndex("msg_detail");
        }
        System.out.println("启动 执行此方法"+b);
    }
}
