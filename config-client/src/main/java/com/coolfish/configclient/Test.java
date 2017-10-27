package com.coolfish.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * author : Forrest
 * date : 2017/10/27.
 */
@Component
@EnableScheduling
public class Test {
    @Value("${testiflocalfirst}")
    private String gitRemoteContent;

    @Scheduled(initialDelayString = "1000", fixedRateString = "1000")
    private void sysout() {
        System.out.println("output :" + gitRemoteContent);
    }
}
