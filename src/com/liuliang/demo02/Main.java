package com.liuliang.demo02;

public class Main {
    public static void main(String[] args) {
        /*
          另一个常用的中断线程的方法是设置标志位：
          用一个 running 标志位来标识线程是否应该继续运行，在外部线程中，通过把 running 置为 false，就可以让线程结束
         */

        NumberThread thread = new NumberThread();
        thread.start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.running = false;
    }

    public static void main1(String[] args) {
        /*
          中断线程就是其他线程给该线程发一个信号，该线程收到信号后结束执行run()方法，使得自身线程能立刻结束运行。
          其它线程调用该线程的interrupt()方法，该线程的中断标志位将被置为true，表示该线程已经被中断。
          目标线程需要反复检测自身状态是否是interrupted状态，如果是，就立刻结束运行。
         */
        Thread thread1 = new MyThread();
        thread1.start();
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1.interrupt();
        try {
            // main 线程等待 thread1 线程结束
            // 如果对 main 线程调用 interrupt(), join() 方法会立刻抛出 InterruptedException
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread is over");
    }
}
