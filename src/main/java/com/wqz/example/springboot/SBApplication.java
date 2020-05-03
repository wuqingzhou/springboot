package com.wqz.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @EnableAutoConfiguration 作用：开启自动配置
 * 修饰范围：类
 * 注：根据pom.xml文件中引入的依赖自动判断项目所需要配置的环境。如果引用了spring-boot-starter-web，那么则会自动构建springmvc
 * 环境、web容器环境。
 * @ComponentScan 作用：开启注解扫描
 * 修饰范围：类
 * 注解扫描范围：默认为启动类所在包及其子包
 */

/**
 * @SpringBootApplication 作用：整合@EnableAutoConfiguration、@ComponentScan注解
 * 修饰范围：类
 */
@SpringBootApplication
public class SBApplication {
    public static void main(String[] args) {
        SpringApplication.run(SBApplication.class, args);
    }
}
