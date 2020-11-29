package com.study.java.completableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class ExceptionallyTest {

    private static final Logger log = LoggerFactory.getLogger(SupplyAsyncTest1.class);

    public static void main(String[] args) throws InterruptedException {
        // thenRun은 Async 작업이 끝나고 해당 스레드에서 계속 작업을 수행한다.
        // CompletableFuture<Void> future =
        CompletableFuture
                // supplyAsync() : 리턴값 존재
                .supplyAsync(() -> {
                    log.info("supplyAsync");
                    return 1;
                })
                // return이 CompletableFuture인 경우 thenCompose를 사용한다.
                .thenCompose(s -> {
                    log.info("thenCompose {}", s);
                    if (1 == 1) {
                        throw new RuntimeException();
                    }
                    return CompletableFuture.completedFuture(s + 1);
                })
                .exceptionally(e -> {
                    log.info("exceptionally");
                    return -10;
                })
                // thenApply : 앞의 비동기 작업의 결과를 받아 사용해 새로운 값을 return 한다.
                .thenApply(s -> {
                    log.info("thenApply {}", s);
                    return s + 2;
                })
                // thenAccept : 앞의 비동기 작업의 결과를 받아 사용하며 return이 없다.
                .thenAccept(s ->        // thenAccept() : CompletableFuture<Void> 리턴
                    log.info("thenAccept {}", s)
                );
        log.info("exit");

        // 별도의 pool을 설정하지 않으면 자바7 부터는 ForkJoinPool이 자동으로 사용된다.
        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
    }
}
