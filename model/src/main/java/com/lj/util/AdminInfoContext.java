package com.lj.util;

public class AdminInfoContext {
    private final static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long get(){
        return threadLocal.get();
    }

    public static void set(Long id){
        threadLocal.set(id);
    }
}
