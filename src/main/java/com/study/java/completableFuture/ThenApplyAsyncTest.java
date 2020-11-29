package com.study.java.completableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThenApplyAsyncTest {

    private static final Logger log = LoggerFactory.getLogger(SupplyAsyncTest1.class);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);

        // thenRun은 Async 작업이 끝나고 해당 스레드에서 계속 작업을 수행한다.
        // CompletableFuture<Void> future =
        CompletableFuture
                // supplyAsync() : 리턴값 존재
                .supplyAsync(() -> {
                    log.info("supplyAsync");
                    return 1;
                }, es)
                // return이 CompletableFuture인 경우 thenCompose를 사용한다.
                .thenCompose(s -> {
                    log.info("thenCompose {}", s);
                    return CompletableFuture.completedFuture(s + 1);
                })
                // thenApply : 앞의 비동기 작업의 결과를 받아 사용해 새로운 값을 return 한다.
                .thenApply(s -> {
                    log.info("thenApply {}", s);
                    return s + 2;
                })
                // 이 작업은 다른 스레드에서 처리를 하려고 할 때, thenApplyAsync를 사용한다.
                // 스레드의 사용을 더 효율적으로 하고 자원을 더 효율적으로 사용한다.
                // 현재 스레드 풀의 정책에 따라 새로운 스레드를 할당하거나 대기중인 스레드를 사용한다.(스레드 풀 전략에 따라 다름)
                .thenApplyAsync(s -> {
                    log.info("thenApplyAsync {}", s);
                    return s + 3;
                }, es)
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
