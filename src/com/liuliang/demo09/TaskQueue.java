package com.liuliang.demo09;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Description: Condition</p>
 *
 * @author <a href="mail to: lliang@outlook.com" rel="nofollow">liu liang</a>
 * @version v1.0, 2023/5/7 - 16:19
 */
public class TaskQueue {
    /*
     * Condition 提供的 await()、signal()、signalAll() 原理和 synchronized 锁对象的 wait()、notify()、notifyAll() 是一致的，并且其行为也是一样的：
       await() 会释放当前锁，进入等待状态；
       signal() 会唤醒某个等待线程；
       signalAll() 会唤醒所有等待线程；
       唤醒线程从 await() 返回后需要重新获得锁。
       和 tryLock() 类似，await() 可以在等待指定时间后，如果还没有被其他线程通过 signal() 或 signalAll() 唤醒，可以自己醒来：
if (condition.await(1, TimeUnit.SECOND)) {
 // 被其他线程唤醒
} else {
// 指定时间内没有被其他线程唤醒
}

       使用 Condition 配合 Lock，我们可以实现更灵活的线程同步
     */
    private final Lock lock = new ReentrantLock();
    // 获得绑定了 Lock 实例的 Condition 实例
    private final Condition condition = lock.newCondition();
    private Queue<String> queue = new LinkedList<>();

    public void addTask(String s) {
        lock.lock();
        try {
            queue.add(s);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String getTask() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
