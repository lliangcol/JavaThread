package com.liuliang.demo10;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>Description: ReadWriteLock</p>
 *
 * @author <a href="mail to: lliang@outlook.com" rel="nofollow">liu liang</a>
 * @version v1.0, 2023/5/8 - 6:17
 */
public class Counter {
    /*
     * 读写锁的目的是保证：
     * 只要没有线程写入，那么读取就是允许的，多个线程同时读取；
     * 只要没有线程读取或者写入，那么写入就是允许的；
     * 不能同时读取和写入。
     *
     * 读写锁实际上维护了一对锁：
     * 读锁：允许多个线程同时读取；
     * 写锁：只允许一个线程写入。
     *
     * 读写锁的目的就是让多个线程同时读取，但只要有一个线程在写入，就必须等待。
     * 因此，ReadWriteLock 接口的方法签名如下：
     * Lock readLock(); // 获取读锁
     * Lock writeLock(); // 获取写锁
     *
     * 读写锁的另一个目的是降低锁的粒度，提高读写的效率。
     * 例如，一个线程在持有写锁时，其他线程对于读锁的获取会等待，但是其他线程仍然可以同时获取读锁，这样就保证了多个线程同时读的效率。
     *
     * 读写锁的另一个特点是只要写入锁未释放，读锁也必须等待。
     * 因为如果读锁不等待，那么就可能读到一个正在被写入的数据，这样就会造成数据不一致。
     * 因此，使用读写锁时，对于写入，我们需要：
     * 获取写入锁；
     * 写入数据；
     * 释放写入锁。
     * 对于读取，我们需要：
     * 获取读取锁；
     * 读取数据；
     * 释放读取锁。
     *
     * 读写锁的另一个特点是适合读多写少的场景。
     * 如果读写频率差不多，那么读写锁的优势就无法体现。
     *
     * 读写锁的实现类是 ReentrantReadWriteLock。
     * 读写锁的特点是，只要没有写入，多个线程持有读锁，这样就保证了读取的正确性，但这同时也带来了一个负面影响，就是只要有一个线程持有写锁，其他线程就必须等待。
     * 因此，读写锁只适合读多写少的场景。
     *
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock(); // 读锁
    private final Lock writeLock = readWriteLock.writeLock(); // 写锁
    private int[] counts = new int[10];

    public void inc(int index) {
        writeLock.lock();
        try {
            counts[index] += 1;
        } finally {
            writeLock.unlock();
        }
    }

    public int[] get() {
        readLock.lock();
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally {
            readLock.unlock();
        }
    }
}
