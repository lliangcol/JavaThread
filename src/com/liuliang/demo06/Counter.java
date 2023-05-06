package com.liuliang.demo06;

/**
 * <p>Description: </p>
 *
 * @author <a href="mail to: lliang@outlook.com" rel="nofollow">liu liang</a>
 * @version v1.0, 2023/5/6 - 23:23
 */
public class Counter {
    private int count = 0;

    /*
     * synchronized 修饰的方法，同一时刻只能有一个线程访问
     * synchronized 修饰的方法，会锁住整个对象，其他线程无法访问该对象的其它 synchronized 修饰的方法
     * 可重入锁：
     * 一旦线程执行到 inc 方法内部，说明它已经获取了当前实例的 this 锁
     * 如果传入的 n < 0，将在 inc 方法内部调用 dec 方法
     * 对同一个线程，能否在获取到锁以后继续获取同一个锁？
     * JVM 允许同一个线程重复获取同一个锁，这种能被同一个线程反复获取的锁，就叫做可重入锁
     * 由于 Java 的线程锁是可重入锁，所以，获取锁的时候，不但要判断是否是第一次获取，还要记录这是第几次获取
     * 每获取一次锁，记录+1，每退出 synchronized 块，记录-1，减到0的时候，才会真正释放锁
     */
    public synchronized void inc(int n) {
        if (n < 0) {
            dec(-n);
        } else {
            count += n;
        }
    }

    public synchronized void dec(int n) {
        count -= n;
    }
}
