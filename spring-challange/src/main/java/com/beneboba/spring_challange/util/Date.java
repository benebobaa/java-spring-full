package com.beneboba.spring_challange.util;

public class Date {
    public static Long next7Days(){
        return System.currentTimeMillis() + (1000 * 16 * 24 * 7);
    }
}
