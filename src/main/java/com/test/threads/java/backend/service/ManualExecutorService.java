package com.test.threads.java.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;

@Slf4j
@Service
public class ManualExecutorService {

    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    public Future<String> submitJob(String jobId) {
        return pool.submit(() -> {
            log.info("Manual job {} running on {}", jobId, Thread.currentThread().getName());
            Thread.sleep(1500);
            return "manual-done:" + jobId;
        });
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down manual executor");
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("Forcing shutdown...");
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
