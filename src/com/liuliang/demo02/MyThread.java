package com.liuliang.demo02;

import java.util.Date;

public class MyThread extends Thread {
    @Override
    public void run() {
        while (!isInterrupted()) {
            Date date = new Date();
            System.out.println(date);
        }
    }
}
