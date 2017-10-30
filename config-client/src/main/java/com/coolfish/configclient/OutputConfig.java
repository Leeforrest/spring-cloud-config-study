package com.coolfish.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author : Forrest
 * date : 2017/10/27.
 */
@RestController
@EnableScheduling
@RefreshScope // 使用该注解的类，会在接到SpringCloud配置中心配置刷新的时候，自动将新的配置更新到该类对应的字段中。
public class OutputConfig {
    @Value("${test}")
    private String gitRemoteContent;

    @Scheduled(initialDelayString = "1000", fixedRateString = "1000")
    public void sysout() {
        System.out.println("key :" + gitRemoteContent);
    }

    @RequestMapping("/hello")
    public String fresh() {
        return "received refresh request";
    }
}
