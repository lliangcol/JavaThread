package com.liuliang.demo08;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Description: ReentrantLock 可重入锁</p>
 *
 * @author <a href="mail to: lliang@outlook.com" rel="nofollow">liu liang</a>
 * @version v1.0, 2023/5/7 - 16:02
 */
public class Main {
}

class Counter {
    /*
     * ReentrantLock 是可重入锁，它和 synchronized 一样，一个线程可以多次获取同一个锁
     * ReentrantLock 可以替代 synchronized 进行同步；
     * ReentrantLock 获取锁更安全；
     * 必须先获取到锁，再进入 try {...} 代码块，最后使用 finally 保证释放锁；
     *
     * 可以使用 tryLock() 尝试获取锁:
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try {
        ...
    } finally {
        lock.unlock();
    }
}

     * 上述代码在尝试获取锁的时候，最多等待1秒。
     * 如果1秒后仍未获取到锁，tryLock() 返回 false，程序就可以做一些额外处理，而不是无限等待下去。
     */
    private final Lock lock = new ReentrantLock();
    private int count;

    public void add(int n) {
        lock.lock();
        try {
            count += n;
        } finally {
            lock.unlock();
        }
    }
}
