package com.delivery.monitor.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    // 비동기 작업을 위한 Executor를 정의합니다.
    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 코어 스레드 풀 크기 설정
        executor.setMaxPoolSize(5); // 최대 스레드 풀 크기 설정
        executor.setQueueCapacity(100); // 작업 큐의 용량 설정
        executor.setThreadNamePrefix("Async-"); // 스레드 이름 접두사 설정
        executor.initialize(); // 스레드 풀 초기화
        return executor;
    }

    // 비동기 작업 중 발생한 예외를 처리하기 위한 핸들러를 정의합니다.
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler(); // 단순 예외 처리 핸들러 사용
    }
}
