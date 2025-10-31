package com.test.threads.java.backend.controller;

import com.test.threads.java.backend.service.JobService;
import com.test.threads.java.backend.service.ManualExecutorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final ManualExecutorService manualExecutorService;
    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    // sync processing
    @PostMapping("/sync")
    public ResponseEntity<String> sync(@RequestParam(defaultValue = "1") int count) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            results.add(jobService.processSync("sync-" + i));
        }
        return ResponseEntity.ok("processed:" + results.size());
    }

    // async fire-and-forget
    @PostMapping("/async/fire")
    public ResponseEntity<String> asyncFire(@RequestParam(defaultValue = "5") int count) {
        for (int i = 0; i < count; i++) {
            jobService.processAsync("fire-" + i);
        }
        return ResponseEntity.ok("submitted:" + count);
    }

    // async wait for all results
    @PostMapping("/async/wait")
    public ResponseEntity<String> asyncWait(@RequestParam(defaultValue = "5") int count) throws Exception {
        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            futures.add(jobService.processAsync("wait-" + i));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        long completed = futures.stream().filter(CompletableFuture::isDone).count();
        return ResponseEntity.ok("completed:" + completed);
    }

    // manual executor submission
    @PostMapping("/manual/submit")
    public ResponseEntity<String> manualSubmit(@RequestParam(defaultValue = "3") int count) throws Exception {
        List<Future<String>> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(manualExecutorService.submitJob("man-" + i));
        }
        for (Future<String> f : list) {
            log.info("Manual result: {}", f.get()); // blocking to show sync retrieval
        }
        return ResponseEntity.ok("manual completed");
    }
}
