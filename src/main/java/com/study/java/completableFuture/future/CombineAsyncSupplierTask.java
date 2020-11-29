package com.study.java.completableFuture.future;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CombineAsyncSupplierTask<T> {

    private Queue<SupplierTask<T>> tasks = new ConcurrentLinkedQueue<>();
    private Executor executor;

    private CombineAsyncSupplierTask(SupplierTask<T> task) {
        this.tasks.add(task);
    }

    private CombineAsyncSupplierTask(Executor executor, SupplierTask<T> task) {
        this.tasks.add(task);
        this.executor = executor;
    }

    public static <T> CombineAsyncSupplierTask<T> newTask(Supplier<T> task, Supplier<T> doError) {
        return new CombineAsyncSupplierTask<>(SupplierTask.from(task, doError));
    }

    public static <T> CombineAsyncSupplierTask<T> newTask(Executor executor, Supplier<T> task, Supplier<T> doError) {
        return new CombineAsyncSupplierTask<>(executor, SupplierTask.from(task, doError));
    }

    public CombineAsyncSupplierTask<T> addTask(Supplier<T> task, Supplier<T> doError) {
        this.tasks.add(SupplierTask.from(task, doError));
        return this;
    }

    public List<T> executeAsync() {
        return this.tasks
                    .stream()
                    .map(task -> CompletableFuture
                                    .supplyAsync(() -> task.getTask().get())
                                    .exceptionally(throwable -> task.getDoError().get())
                                    .join()
                    )
                    .collect(Collectors.toList());
    }
}