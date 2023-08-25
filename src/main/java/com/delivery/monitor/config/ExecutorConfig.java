package com.delivery.monitor.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

    /**
     * ScheduledExecutorService 빈을 설정하는 메서드
     * 
     * 스케줄링 작업을 수행할 수 있는 스레드 풀을 생성하여 반환함
     * 스레드 풀의 크기는 10으로 설정됨
     *
     * @return ScheduledExecutorService 스케줄링 작업을 처리할 스레드 풀
     */
    @Bean
    ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(10);
    }
}
