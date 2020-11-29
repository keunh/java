package com.study.java.completableFuture.future;

import java.util.function.Supplier;

public class SupplierTask<T> {
    private Supplier<T> task;
    private Supplier<T> doError;

    private SupplierTask(Supplier<T> task, Supplier<T> doError) {
        this.task = task;
        this.doError = doError;
    }

    public static <T> SupplierTask<T> from(Supplier<T> task, Supplier<T> doError) {
        return new SupplierTask<>(task, doError);
    }

    public Supplier<T> getTask() {
        return task;
    }

    public Supplier<T> getDoError() {
        return doError;
    }
}