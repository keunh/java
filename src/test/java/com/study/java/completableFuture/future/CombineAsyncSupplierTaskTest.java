package com.study.java.completableFuture.future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CombineAsyncSupplierTaskTest {

    private final int RESULT_1 = 1;
    private final int RESULT_2 = 2;

    @Test
    @DisplayName("비동기 결과 합치기 : 모든 task 성공 시")
    void executeAsyncTest1() {
        List<Integer> testResult = CombineAsyncSupplierTask
                                    .newTask(() -> RESULT_1, () -> 0)
                                    .addTask(() -> RESULT_2, () -> 0)
                                    .executeAsync();

        assertAll(
                () -> assertEquals(2, testResult.size()),
                () -> assertEquals(3, testResult.stream().mapToInt(v -> v).sum())
        );
    }

    @Test
    @DisplayName("비동기 결과 합치기 : task 한 개 실패 시")
    void executeAsyncTest() {
        List<Integer> testResult = CombineAsyncSupplierTask
                                    .newTask(() -> {
                                        throw new RuntimeException("RuntimeException");
                                    }, () -> 0)
                                    .addTask(() -> RESULT_2, () -> 0)
                                    .executeAsync();

        assertAll(
                () -> assertEquals(2, testResult.size()),
                () -> assertEquals(2, testResult.stream().mapToInt(v -> v).sum())
        );
    }
}
