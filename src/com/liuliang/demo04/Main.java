package com.liuliang.demo04;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
          如果多个线程同时读写共享变量，会出现数据不一致的问题
         */
        Thread addThread = new AddThread();
        Thread decThread = new DecThread();
        addThread.start();
        decThread.start();
        try {
            addThread.join();
            decThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 期望的结果是0，但是实际上可能是其他值
        System.out.println(Counter.count);

        /*
          对变量进行读取和写入时，结果要正确，必须保证是原子操作
          原子操作是指不能被中断的一个或一系列操作
          多线程模型下，要保证逻辑正确，对共享变量进行读写时，必须保证一组指令以原子方式执行：即某一个线程执行时，其它线程必须等待

          临界区（Critical Section）
          通过加锁和解锁的操作，就能保证多条指令总是在一个线程执行期间，不会有其它线程会进入此指令区间
          即使在执行期线程被操作系统中断执行，其它线程也会因为无法获得锁导致无法进入此指令区间
          只有执行线程将锁释放后，其它线程才有机会获得锁并执行
          这种加锁和解锁之间的代码块我们称之为临界区（Critical Section），任何时候临界区最多只有一个线程能执行

          Java程序使用 synchronized 关键字对一个对象进行加锁
          synchronized 保证了代码块在任意时刻最多只有一个线程能执行
          执行结束后，在 synchronized 语句块结束会自动释放锁

          如何使用synchronized：
            找出修改共享变量的线程代码块；
            选择一个共享实例作为锁；
            使用synchronized(lockObject) { ... }。

          在使用 synchronized 的时候，不必担心抛出异常。因为无论是否有异常，都会在 synchronized 结束处正确释放锁
         */

        Thread addThread1 = new AddThread1();
        Thread decThread1 = new DecThread1();
        addThread1.start();
        decThread1.start();
        try {
            addThread1.join();
            decThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Counter1.count);

        /*
          不需要 synchronized 的操作

          JVM规范定义了几种原子操作：
            基本类型（long 和 double 除外）赋值，例如：int n = m；
            引用类型赋值，例如：List<String> list = anotherList
          long 和 double 是64位数据，JVM 没有明确规定64位赋值操作是不是一个原子操作，不过在x64平台的 JVM 是把 long 和 double 的赋值作为原子操作实现的

          但是，如果是多行赋值语句，就必须保证是同步操作，不但写需要同步，读也需要同步

          如果多线程读写的是一个不可变对象，那么无需同步，因为不会修改对象的状态
         */
    }
}

class Counter {
    public static int count = 0;
}

class AddThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++)
            Counter.count += 1;
    }
}

class DecThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++)
            Counter.count -= 1;
    }
}

class Counter1 {
    public static final Object lock = new Object();
    public static int count = 0;
}

class AddThread1 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            synchronized (Counter1.lock) {
                Counter1.count += 1;
            }
        }
    }
}

class DecThread1 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            synchronized (Counter1.lock) {
                Counter1.count -= 1;
            }
        }
    }
}

class Data {
    List<String> names;

    /*
      如果多线程读写的是一个不可变对象，那么无需同步，因为不会修改对象的状态
     */
    public void set(String[] names) {
        // 创建了一个不可变的 List
        // List包含的对象也是不可变对象String，因此，整个List<String>对象都是不可变的，因此读写均无需同步
        this.names = List.of(names);
    }

    public List<String> get() {
        return this.names;
    }
}