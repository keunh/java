package com.study.java.completableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThenCombineTest {

    private static final Logger log = LoggerFactory.getLogger(SupplyAsyncTest1.class);

    public static void main(String[] args) throws InterruptedException {
        // 뒤에 Async가 붙은 메서드는 다른 쓰레드에서 처리하고 싶을 때 사용한다.

        CompletableFuture<Integer> future1 =
                CompletableFuture
                        .supplyAsync(() -> {
                            log.info("supplyAsync 1");
                            return 1;
                        })
                        .thenApplyAsync(s -> {
                            log.info("thenApply {}", s);
                            return s + 2;
                        });
        CompletableFuture<Integer> future2 =
                CompletableFuture
                        .supplyAsync(() -> {
                            log.info("supplyAsync 2");
                            return 10;
                        })
                        .thenApplyAsync(s -> {
                            log.info("thenApply {}", s);
                            return s + 12;
                        });

        future1.thenCombine(future2, (s1, s2) -> s1 + s2)
                .thenAccept(s -> log.info("thenCombine {}", s));

        log.info("exit");

        // 별도의 pool을 설정하지 않으면 자바7 부터는 ForkJoinPool이 자동으로 사용된다.
        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
    }
}
