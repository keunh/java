package com.study.java.completableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HandleTest {

    private static final Logger log = LoggerFactory.getLogger(SupplyAsyncTest1.class);

    public static void main(String[] args) {
        CompletableFuture<Integer> future1 =
                CompletableFuture
                        .supplyAsync(() -> {
                            log.info("supplyAsync 1");
                            if(1 == 1) {
                                throw new RuntimeException();
                            }
                            return 1;
                        })
                        .thenApplyAsync(s -> {
                            log.info("thenApply {}", s);
                            return s + 2;
                        })
                        .handle((integer, throwable) -> {
                            if(throwable != null) {
                                return 0;
                            }
                            return integer;
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
                        })
                        .handle((integer, throwable) -> {
                            if(throwable != null) {
                                return 0;
                            }
                            return integer;
                        });

        List<CompletableFuture> completableFutures = Arrays.asList(future1, future2);
        List<Object> list = completableFutures
            .stream()
            .map(future -> future.join())
            .collect(Collectors.toList());

        list.forEach(System.out::println);

        log.info("exit");
    }
}
