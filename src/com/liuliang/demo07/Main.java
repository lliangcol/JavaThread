package com.liuliang.demo07;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * <p>Description:
 * 使用 wait 和 notify
 * 多线程协调运行的原则就是：当条件不满足时，线程进入等待状态；当条件满足时，线程被唤醒，继续执行任务
 * wait 和 notify 用于多线程协调运行：
 * 在 synchronized 内部可以调用 wait() 使线程进入等待状态；
 * 必须在已获得的锁对象上调用 wait() 方法；
 * 在 synchronized 内部可以调用 notify() 或 notifyAll() 唤醒其他等待线程；
 * 必须在已获得的锁对象上调用 notify() 或 notifyAll() 方法；
 * 已唤醒的线程还需要重新获得锁后才能继续执行。
 * </p>
 *
 * @author <a href="mail to: lliang@outlook.com" rel="nofollow">liu liang</a>
 * @version v1.0, 2023/5/7 - 15:14
 */
public class Main {
    public static void main(String[] args) {
        var queue = new TaskQueue();
        var threads = new ArrayList<Thread>();
        // 消费者
        for (int i = 0; i < 5; i++) {
            var t = new Thread(() -> {
                // 执行 task:
                while (true) {
                    try {
                        String s = queue.getTask();
                        System.out.println("execute task: " + s);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            });
            t.start();
            threads.add(t);
        }

        // 生产者
        var addThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                // 放入 task:
                String s = "t-" + Math.random();
                System.out.println("add task: " + s);
                queue.addTask(s);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        });
        addThread.start();
        try {
            addThread.join();
        } catch (InterruptedException ignored) {
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }

        // 请求结束所有线程
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}

class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s) {
        this.queue.add(s);
        // 如何让等待的线程被重新唤醒，然后从 wait() 方法返回？
        // 答案是在相同的锁对象上调用 notify() 方法
        // 使用 notifyAll() 将唤醒所有当前正在 this 锁等待的线程，而 notify() 只会唤醒其中一个（具体哪个依赖操作系统，有一定的随机性）
        // 通常来说，使用 notifyAll() 是更安全的做法：有些时候，如果我们的代码逻辑考虑不周，用 notify() 会导致只唤醒了一个线程，而其他线程可能永远等待下去醒不过来了
        this.notifyAll();
    }

    public synchronized String getTask() throws InterruptedException {
        // 判断队列是否为空
        while (queue.isEmpty()) {
            // 为空时，当前线程进入等待状态
            /*
             * wait() 方法必须在 synchronized 方法内部当前获取的锁对象上调用
             * 这里获取的是 this 锁
             * 调用 wait() 方法后，线程进入等待状态
             *
             * wait() 方法不会返回，直到将来某个时刻，线程从等待状态被其他线程唤醒后，wait() 方法才会返回，然后，继续执行下一条语句
             * wait() 方法不是一个普通的 Java 方法，而是定义在 Object 类的一个 native 方法，也就是由 JVM 的 C 代码实现的
             * 必须在 synchronized 块中才能调用 wait() 方法，因为 wait() 方法调用时，会释放线程获得的锁，wait() 方法返回后，线程又会重新试图获得锁
             * 因此，只能在锁对象上调用 wait() 方法
             */
            this.wait();
            /*
             * wait() 方法返回时需要重新获得 this 锁，如果多个线程等待重新获取 this 锁，其中一个线程获取 this 锁后，其它线程将继续等待
             * 为什么 wait() 方法要放在 while 循环中？
             *
             * 因为即使收到了 notifyAll() 唤醒，wait() 方法也不会立刻返回，它还要等待当前线程释放锁后，再次尝试获取锁，才能从 wait() 方法返回
             * 多个线程被唤醒后，只有一个线程能获取 this 锁，此刻，该线程执行 queue.remove() 可以获取到队列的元素，
             * 然而，剩下的线程如果获取 this 锁后执行 queue.remove()，此刻队列可能已经没有任何元素了，
             * 所以，要始终在 while 循环中 wait()，并且每次被唤醒后拿到 this 锁就必须再次判断
             * （从wait() 返回并执行后面语句的条件不一定是被 notifyAll() 唤醒之后第一个抢到锁的消费者，
             *   有可能是唤醒之后抢不到锁处于 blocking 状态后又拿到锁的消费者，这就有可能出现 queue 为空的情况）
             */
        }
        return queue.remove();
    }
}
