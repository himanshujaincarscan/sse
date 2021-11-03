package com.example.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
public class SseController {

    @GetMapping("/get")
    @CrossOrigin
    public SseEmitter get(@RequestParam String userId) {
        log.info("Inside sse");
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sseEmitter.onCompletion(() -> log.info("SseEmitter is completed"));

        sseEmitter.onTimeout(() -> log.info("SseEmitter is timed out"));

        sseEmitter.onError((ex) -> log.info("SseEmitter got error:", ex));

        //Wait for one and server sends data
        sleep(10, sseEmitter);

        try {
            sseEmitter.send("Message from server");
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
        }finally {
            sseEmitter.complete();
        }
        log.info("Controller exits");
        return sseEmitter;
    }

    private void sleep(int seconds, SseEmitter sseEmitter) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            sseEmitter.completeWithError(e);
        }
    }
}
