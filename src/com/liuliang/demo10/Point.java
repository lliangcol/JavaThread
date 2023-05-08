package com.liuliang.demo10;

import java.util.concurrent.locks.StampedLock;

/**
 * <p>Description: StampedLock</p>
 *
 * @author <a href="mail to: lliang@outlook.com" rel="nofollow">liu liang</a>
 * @version v1.0, 2023/5/8 - 6:34
 */
public class Point {
    /*
     * ReadWriteLock：读写锁，读写锁允许多个线程同时读共享变量，但是只允许一个线程写共享变量
     * 如果有线程正在读，写线程需要等待读线程释放锁后才能获取写锁，即读的过程中不允许写，这是一种悲观的读锁策略
     *
     * 要进一步提升并发执行效率，Java 8引入了新的读写锁：StampedLock
     * StampedLock和ReadWriteLock相比，改进之处在于：
     * 读的过程中也允许获取写锁后写入！这样一来，我们读的数据就可能不一致，所以，需要一点额外的代码来判断读的过程中是否有写入，这种读锁是一种乐观锁
     *
     * 乐观锁的意思就是乐观地估计读的过程中大概率不会有写入，因此被称为乐观锁
     * 如果读取的过程中确实没有写入，那么就没有必要获取锁
     * 反过来，悲观锁则是读的过程中拒绝有写入，也就是写入必须等待
     * 显然乐观锁的并发效率更高，但一旦有小概率的写入导致读取的数据不一致，需要能检测出来，再读一遍就行
     *
     * StampedLock：读写锁的改进版本，StampedLock 支持读锁、写锁和乐观读锁
     * StampedLock 支持三种模式的锁
     * 1. 写锁（writeLock）
     * 2. 悲观读锁（readLock）
     * 3. 乐观读锁（tryOptimisticRead）
     *
     * StampedLock 是不可重入的，不能在一个线程中反复获取同一个锁
     * StampedLock 支持读锁和写锁的相互转换
     * StampedLock 支持锁的降级，不支持锁的升级
     * StampedLock 是不可重入的，如果一个线程已经持有了写锁，再去获取写锁的话就会造成死锁
     * StampedLock 支持中断请求
     * StampedLock 是一种乐观的读策略，所以它不支持条件变量
     * StampedLock 有可能导致线程饥饿，因为 StampedLock 读的过程中写线程一直等待，那么写线程就会一直等待下去
     * StampedLock 有可能导致线程活锁，因为 StampedLock 读的过程中写线程一直修改数据，那么读线程就会一直重试
     *
     * StampedLock 还提供了更复杂的将悲观读锁升级为写锁的功能，它主要使用在if-then-update的场景：即先读，如果读的数据满足条件，就返回，如果读的数据不满足条件，再尝试写
     */
    private final StampedLock stampedLock = new StampedLock();

    private double x;
    private double y;

    public void move(double deltaX, double deltaY) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    /*
     * 首先我们通过 tryOptimisticRead() 获取一个乐观读锁，并返回版本号
     * 注意，tryOptimisticRead() 并不是获取读锁，它只是返回一个版本号
     * 接着进行读取，读取完成后，我们通过 validate() 去验证版本号，如果在读取过程中没有写入，版本号不变，验证成功，我们就可以放心地继续后续操作
     * 如果在读取过程中有写入，版本号会发生变化，验证将失败
     * 在失败的时候，我们再通过获取悲观读锁再次读取
     * 由于写入的概率不高，程序在绝大部分情况下可以通过乐观读锁获取数据，极少数情况下使用悲观读锁获取数据
     * 这样一来，乐观读锁和悲观读锁的组合使用，就可以大大提高读取效率
     */
    public double distanceFromOrigin() {
        long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁
        // 注意下面两行代码不是原子操作
        // 如果这两行代码之间发生了写操作，则可能导致读取到的数据是脏数据
        // 因此需要通过 validate 方法校验读取的数据是否正确
        // 如果 validate 返回 false，则需要获取一个悲观读锁
        // 如果 validate 返回 true，则可以直接使用读取到的数据
        // 假设 x,y = (100,200)
        double currentX = x;
        // currentX = 100
        // 此处可能发生了写操作，x,y = (300,400)
        double currentY = y;
        // 此处已读取到y，如果没有写入，读取是正确的(100,200)
        // 如果有写入，读取是错误的(100,400)
        if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
            stamp = stampedLock.readLock(); // 获取一个悲观读锁
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp); // 释放悲观读锁
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}
