package com.wuhulala.zookeeper.curator;

import com.sun.org.apache.xerces.internal.xs.StringList;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * master节点 选举
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/3/18
 */
public class MasterSelector {

    public static String masterPath = "/master";

    public static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("zoo1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws InterruptedException {
        client.start();



        CyclicBarrier barrier = new CyclicBarrier(3);

        for (int i = 1; i <= 3; i++) {
            final int index = i;
            new Thread(()->{
                String name = "selector-" + index;
                MySelector selector = new MySelector(client, masterPath, new MyLeaderSelectorListenerAdapter(name), name);
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                selector.startSelect();
            }).start();


        }

        Thread.sleep(Integer.MAX_VALUE);
    }

    static class MySelector extends LeaderSelector {

        private String name;
        private CuratorFramework curatorFramework;
        private String path;
        private LeaderSelectorListener listener;

        MySelector(CuratorFramework curatorFramework, String path,
                   LeaderSelectorListener listener, String name) {
            super(curatorFramework, path, listener);

            this.name = name;
        }

        void startSelect() {
            System.out.println(name + " 开始竞选..." + System.currentTimeMillis());
            // 退出master节点之后，再次自动进入选举人队列
            this.autoRequeue();
            this.start();
        }
    }

    static class MyLeaderSelectorListenerAdapter extends LeaderSelectorListenerAdapter{

        private String selectorName;

        public MyLeaderSelectorListenerAdapter(String selectorName) {
            this.selectorName = selectorName;
        }

        @Override
        public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
            System.out.println(selectorName + " 成为了Master节点");
            Thread.sleep(3000);
            System.out.println(selectorName + " 完成Master内容,我累了，放弃身份");
        }
    };
}
