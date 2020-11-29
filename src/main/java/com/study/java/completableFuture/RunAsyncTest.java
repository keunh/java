package com.study.java.completableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class RunAsyncTest {

    private static final Logger log = LoggerFactory.getLogger(RunAsyncTest.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // thenrun은 Async 작업이 끝나고 해당 스레드에서 계속 작업을 수행한다.
        // CompletableFuture<Void> future =
        CompletableFuture
                .runAsync(() -> log.info("runAsync"))   // runAsync() : 리턴값이 없다
                .thenRun(() -> log.info("thenRun"))
                .thenRun(() -> log.info("thenRun"));

        // log.info("get() : {}", future.get());
        log.info("exit");

        // 별도의 pool을 설정하지 않으면 자바7 부터는 ForkJoinPool이 자동으로 사용된다.
        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
    }
}
