package org.example;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MultiThreading implements Runnable {
    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName());

        MultiThreading m = new MultiThreading();
        m.run();

        ArrayList<String> arr = new ArrayList<>();

        arr.add("aw");
//        arr.forEach();
//        BiConsumer;
//        Predicate
//        BiPredicate;
//        Consumer;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }



}
