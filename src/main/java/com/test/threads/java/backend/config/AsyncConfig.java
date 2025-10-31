package com.test.threads.java.backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "jobExecutor")
    public Executor jobExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);      // start threads
        executor.setMaxPoolSize(20);      // max threads
        executor.setQueueCapacity(50);    // task queue
        executor.setThreadNamePrefix("job-exec-");
        executor.initialize();
        return executor;
    }
}
