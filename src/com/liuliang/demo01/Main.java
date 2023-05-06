package com.liuliang.demo01;

public class Main {
    public static void main(String[] args) {
        /*
          Java 线程的状态：
            1. New：新创建的线程，尚未执行
            2. Runnable：运行中的线程，正在执行 run 方法的 Java 代码
            3. Blocked：运行中的线程，因为某些操作被阻塞而挂起
            4. Waiting：运行中的线程，因为某些操作在等待中
            5. Timed Waiting：运行中的线程，因为执行 sleep 方法正在计时等待
            6. Terminated：线程已终止，因为 run 方法执行完毕
         */

        /*
          线程终止的原因有：
            1. run 方法正常执行完成
            2. 线程抛出一个未捕获的异常
            3. 直接调用该线程的 interrupt 方法
         */

        // 一个线程可以等待另一个线程完成后再继续执行
        Thread thread = new Thread(() -> {
            System.out.println("Start thread");
        });

        System.out.println("Start");
        thread.start();
        try {
            // 调用 join 方法会阻塞当前线程，直到目标线程执行完毕
            // join 方法可以传入一个毫秒数，表示最多等待多少毫秒
            // 如果目标线程在指定的毫秒数内没有执行完毕，join 方法会返回
            // 如果目标线程已经执行完毕，调用 join 方法会立刻返回
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End");
    }

    public static void main2(String[] args) {
        System.out.println("main start...");
        Thread thread = new Thread(() -> {
            System.out.println("thread start...");
            try {
                // Thread.sleep 方法会让当前线程休眠指定的毫秒数
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread end.");
        });
        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main end.");
    }

    public static void main1(String[] args) {
        // 1. 从 Thread 派生一个自定义类，然后覆写 run 方法
        Thread thread1 = new MyThread();
        // 必须调用 Thread 实例的 start 方法才能启动新线程
        // start 方法内部调用 start0 方法（private native void start0();）
        // native 修饰符表示该方法是一个本地方法，由 JVM 的本地库实现
        // start 方法会在内部自动调用实例的 run 方法
        // run 方法内部实现了线程要执行的任务
        // 一个线程对象只能调用一次 start 方法，否则会抛出 IllegalThreadStateException 异常
        thread1.start();

        // 2. 创建 Thread 实例时，传入一个 Runnable 实例
        Thread thread2 = new Thread(new MyRunnable());
        thread2.start();

        // 使用 lambda 语法简化
        Thread thread3 = new Thread(() -> {
            System.out.println("Start new thread");
        });
        thread3.start();
    }
}
