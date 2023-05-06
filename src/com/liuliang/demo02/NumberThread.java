package com.liuliang.demo02;

public class NumberThread extends Thread {
    // 用一个 running 标志位来标识线程是否应该继续运行，在外部线程中，通过把 running 置为 false，就可以让线程结束
    // running 线程间共享变量，所以要用 volatile 修饰符修饰
    // running 用 volatile 修饰符修饰，因为多线程中，每个线程都有自己的工作内存，线程在运行过程中，直接从工作内存中读取变量，而不是每次都从主内存中读取变量
    // running 用 volatile 修饰符修饰，如果线程修改了变量的值，那么该线程会把修改后的值刷新到主内存中，而其它线程在读取该变量时，会直接从主内存中读取变量
    // volatile 修饰符用来保证其它线程读取到的 running 值是最新的
    // volatile 关键字解决的是可见性问题：当一个线程修改了某个共享变量的值，其它线程能够立刻看到修改后的值
    public volatile boolean running = true;

    @Override
    public void run() {
        int n = 0;
        while (running) {
            n++;
            System.out.println(n);
        }
        System.out.println("NumberThread is over");
    }
}
