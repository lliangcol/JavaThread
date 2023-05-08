package com.liuliang.demo11;

import java.util.UUID;
import java.util.concurrent.Semaphore;

/**
 * <p>Description: Semaphore</p>
 * <p>Semaphore本质上就是一个信号计数器，用于限制同一时间的最大访问数量</p>
 *
 * @author <a href="mail to: lliang@outlook.com" rel="nofollow">liu liang</a>
 * @version v1.0, 2023/5/8 - 8:10
 */
public class AccessLimitControl {
    // 信号量，只允许 10 个线程同时访问
    // 如果 semaphore.getQueueLength() > 0 说明当前请求被限流了
    // 可以把这个信息记录到日志中，便于排查问题
    // 也可以直接抛出异常，让调用方感知到请求被限流了
    // 也可以把请求放到 MQ 中，然后异步处理
    // 也可以返回一些静态的提示信息，告诉用户稍后重试
    // 也可以返回一些动态的提示信息，告诉用户接口限流了，过几秒钟重试
    // 也可以返回一些动态的提示信息，告诉用户当前排队人数，让用户知道大概要多久才能处理完请求
    final Semaphore semaphore = new Semaphore(10);

    public String access() throws Exception {
        // 获取一个许可
        // 如果没有许可，当前线程就会阻塞
        // 直到有线程释放一个许可或者当前线程被中断
        // 通过这种方式，我们就能够控制对互联网资源的访问了
        // 比如数据库连接
        // 比如文件流
        // 比如网络爬虫
        // 比如 HttpClient
        semaphore.acquire();
        try {
            return UUID.randomUUID().toString();
        } finally {
            semaphore.release();
        }
    }
}
