package com.lj.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolUtil {
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(3,5,5,
            TimeUnit.SECONDS,new LinkedBlockingQueue<>(5));

    public static <T> T submit(Callable<T> c)  {
        T res = null;
        try {
            Future<T> future = THREAD_POOL_EXECUTOR.submit(c);
            res = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
