package com.liuliang.demo03;

import com.liuliang.demo02.MyThread;

public class Main {
    public static void main(String[] args) {
        /*
          Java 程序入口就是由 JVM 启动 main 线程，main 线程又可以启动其他线程
          当所有线程都运行结束时，JVM 退出，进程结束
          如果有一个线程没有退出，JVM 进程就不会退出，所以，必须保证所有线程都能及时结束

          守护线程（Daemon Thread）
          守护线程是指为其它线程服务的线程
          在 JVM 中，所有非守护线程都执行完毕后，无论有没有守护线程，虚拟机都会自动退出
          因此，JVM退出时，不必关心守护线程是否已结束

          创建守护线程的方法：
          调用 setDaemon(true) 把该线程标记为守护线程
          必须在 start() 方法调用之前设置，否则会抛出 IllegalThreadStateException 异常

          守护线程不能持有任何需要关闭的资源，例如打开文件等，因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失
         */

        Thread thread = new MyThread();
        thread.setDaemon(true);
        thread.start();
    }
}
