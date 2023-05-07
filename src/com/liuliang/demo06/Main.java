package com.liuliang.demo06;

/**
 * <p>Description: </p>
 *
 * @author <a href="mail to: lliang@outlook.com" rel="nofollow">liu liang</a>
 * @version v1.0, 2023/5/6 - 23:22
 */
public class Main {
    public static void main(String[] args) {
        /*
         * 一个线程可以获取一个锁后，再继续获取另一个锁
         * 在获取多个锁的时候，不同线程获取多个不同对象的锁可能导致死锁
         * 线程1和线程2如果分别执行 add() 和 dec() 方法时：
            线程1：进入 add()，获得 lockA；
            线程2：进入 dec()，获得 lockB。
            随后：
            线程1：准备获得 lockB，失败，等待中；
            线程2：准备获得 lockA，失败，等待中。
         * 由于线程1和线程2互相等待对方释放锁，因此，这两个线程就会无限等待下去，从而导致死锁
         * 死锁发生后，没有任何机制能解除死锁，只能强制结束JVM进程
         * 为了避免死锁，只能让线程1和线程2获取锁的顺序相同，例如，先获取 lockA 再获取 lockB，就不会产生死锁
         *
         * Java 的 synchronized 锁是可重入锁；
         * 死锁产生的条件是多线程各自持有不同的锁，并互相试图获取对方已持有的锁，导致无限等待；
         * 避免死锁的方法是多线程获取锁的顺序要一致。
         */
    }
}
