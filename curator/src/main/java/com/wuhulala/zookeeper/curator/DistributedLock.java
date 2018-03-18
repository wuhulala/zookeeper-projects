package com.wuhulala.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.SimpleFormatter;

/**
 * 分布式锁---自动订单号
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/3/18
 */
public class DistributedLock {

    public static String lockPath = "/wuhulala_lock";
    public static String counterPath = "/wuhulala_counter";

    public static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("zoo1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws InterruptedException {
        client.start();
        final InterProcessLock lock = new InterProcessMutex(client, lockPath);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final DistributedAtomicLong counter = new DistributedAtomicLong(client, lockPath, new ExponentialBackoffRetry(1000, 3));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String orderNoPrefix = sdf.format(new Date());
        Thread.sleep(2000);
        for (int i = 0; i < 300; i++) {
            //new Thread(() -> {
                try {
                    //countDownLatch.await();
                    lock.acquire();

                    long curNo = counter.increment().postValue();
                    System.out.println("当前订单为:" + orderNoPrefix + String.format("%06d", curNo));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
           // }).start();
        }
        //countDownLatch.countDown();
    }
}
