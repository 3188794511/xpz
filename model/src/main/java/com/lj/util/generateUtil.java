package com.lj.util;

public class generateUtil {
    private final static String USER_NAME_PREFIX = "user_";
    public static String generateUserName(){
        long suffix = (long) (Math.random() * 1000000);
        return USER_NAME_PREFIX + suffix;
    }

    public static void main(String[] args) {
        System.out.println(generateUserName());
        System.out.println(generateUserName());
        System.out.println(generateUserName());
        System.out.println(generateUserName());
    }

}
