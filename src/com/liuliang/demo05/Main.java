package com.liuliang.demo05;

/**
 * <p>Description: </p>
 *
 * @author <a href="mail to: lliang@outlook.com" rel="nofollow">liu liang</a>
 * @version v1.0, 2023/5/6 - 23:02
 */
public class Main {
    public static void main(String[] args) {
        /*
          如果一个类被设计为允许多线程正确访问，我们就说这个类就是“线程安全”的（thread-safe）
          Counter类就是线程安全的，Java标准库的java.lang.StringBuffer也是线程安全的
          还有一些不变类，例如 String，Integer，LocalDate，它们的所有成员变量都是 final，多线程同时访问时只能读不能写，这些不变类也是线程安全的
          最后，类似Math这些只提供静态方法，没有成员变量的类，也是线程安全的

          除了上述几种少数情况，大部分类，例如ArrayList，都是非线程安全的类，我们不能在多线程中修改它们
          但是，如果所有线程都只读取，不写入，那么ArrayList是可以安全地在线程间共享的

          没有特殊说明时，一个类默认是非线程安全的

          用 synchronized 修饰方法可以把整个方法变为同步代码块，synchronized 方法加锁对象是 this；
          通过合理的设计和数据封装可以让一个类变为“线程安全”；
         */

        Counter counter1 = new Counter();
        Counter counter2 = new Counter();

        new Thread(() -> {
            counter1.add(1);
        }).start();
        new Thread(() -> {
            counter1.dec(1);
        }).start();

        new Thread(() -> {
            counter2.add(1);
        }).start();
        new Thread(() -> {
            counter2.dec(1);
        }).start();

        System.out.println(counter1.get());
        System.out.println(counter2.get());
    }
}

class Counter {
    private int count = 0;

    public void add(int n) {
        synchronized (this) {
            count += n;
        }
    }

    /*
      当我们锁住的是this实例时，实际上可以用synchronized修饰这个方法
      add1 方法与 add 方法等价

      因此，用synchronized修饰的方法就是同步方法，它表示整个方法都必须用this实例加锁
     */
    public synchronized void add1(int n) {
        count += n;
    }

    public void dec(int n) {
        synchronized (this) {
            count -= n;
        }
    }

    public int get() {
        return count;
    }

    /*
      对于static方法，是没有this实例的，因为static方法是针对类而不是实例
      任何一个类都有一个由 JVM 自动创建的 Class 实例
      因此，对于 static 方法，我们可以使用 synchronized(Class) 来锁定该方法，也可以使用 synchronized static 方法来锁定
     */
    public static void test(int n) {
        synchronized (Counter.class) {
            System.out.println(n);
        }
    }

    public synchronized static void test1(int n) {
        System.out.println(n);
    }
}
