package com.test.threads.java.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class JobService {

    // Simple synchronous job
    public String processSync(String jobId) {
        log.info("Start sync job {} on thread {}", jobId, Thread.currentThread().getName());
        doWork(jobId);
        log.info("End sync job {} on thread {}", jobId, Thread.currentThread().getName());
        return "done:" + jobId;
    }

    // Async using @Async with named executor
    @Async("jobExecutor")
    public CompletableFuture<String> processAsync(String jobId) {
        log.info("Start async job {} on thread {}", jobId, Thread.currentThread().getName());
        doWork(jobId);
        log.info("End async job {} on thread {}", jobId, Thread.currentThread().getName());
        return CompletableFuture.completedFuture("done:" + jobId);
    }

    // Helper: simulate IO bound work
    private void doWork(String jobId) {
        try {
            // simulate variable work time
            Thread.sleep(2000 + (int)(Math.random()*2000));
            // simulate some CPU work if needed
            int sum = 0;
            for (int i = 0; i < 1000; i++) sum += i;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Job {} interrupted", jobId);
        }
    }
}
