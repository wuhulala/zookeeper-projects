package com.wuhulala.zookeeper.demo;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 原生zookeeper客户端启动
 *
 * @author wuhulala
 * @version 1.0
 */
public class ZookeeperConstructor implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConstructor.class);

    private static CountDownLatch connectionCDL = new CountDownLatch(1);

    private static volatile ZooKeeper zooKeeper = null;

    private ZookeeperConstructor() {

    }

    public static ZooKeeper getInstance(String connectString) throws InterruptedException, IOException {
        ZooKeeper zooKeeper =  getInstance(connectString, new ZookeeperConstructor());
        connectionCDL.await();
        return zooKeeper;
    }

    public static ZooKeeper getInstance(String connectString, Watcher watcher) throws InterruptedException, IOException {
        logger.info("get zookeeper instance [{}]", connectString);
        if (zooKeeper == null) {
            synchronized (ZookeeperConstructor.class) {
                if (zooKeeper == null) {
                    zooKeeper = new ZooKeeper(connectString, 5000, watcher);
                }
            }
        }
        System.out.println(zooKeeper.getState());

        return zooKeeper;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String connectString = "zoo1:2181,zoo2:2182,zoo3:2183";

        ZooKeeper zooKeeper = new ZooKeeper(connectString, 5000, new ZookeeperConstructor());

        System.out.println(zooKeeper.getState());

        connectionCDL.await();

        System.out.println("zookeeper session established。");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event: " + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectionCDL.countDown();
        }
    }
}
