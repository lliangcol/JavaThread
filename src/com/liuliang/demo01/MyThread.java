package com.liuliang.demo01;

public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Start new thread");
    }
}
