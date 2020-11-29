package com.study.java.completableFuture.future;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CombineAsyncSupplierTaskTest {

    private final int RESULT_1 = 1;
    private final int RESULT_2 = 2;

    @Test
    public void executeAsyncTest() {
        List<Integer> testResult = CombineAsyncSupplierTask
                                        .newTask(() -> RESULT_1, () -> 0)
                                        .addTask(() -> RESULT_2, () -> 0)
                                        .executeAsync();

        assertThat(testResult.size()).isEqualTo(2);
    }
}
