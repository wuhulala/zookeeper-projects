package com.wuhulala.zookeeper.demo;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/3/17
 */
public class ZookeeperNodeCreator {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String connectString = "zoo1:2181,zoo2:2182,zoo3:2183";
        ZooKeeper zooKeeper = ZookeeperConstructor.getInstance(connectString);

        //=====================================================================================
        //            同步创建一个节点
        //=====================================================================================

        //createNodeBySync(zooKeeper);



        //=====================================================================================
        //            异步创建一个节点
        //=====================================================================================
        createNodeByAsync(zooKeeper);

        Thread.sleep(6000);
    }

    private static void createNodeByAsync(ZooKeeper zooKeeper) {
        zooKeeper.create("/sync-user", "sync-user".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new StringCallback(), "上下文");
    }

    private static void createNodeBySync(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {

        String parentPath = zooKeeper.create("/user", "user".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Success create znode : " + parentPath);

        String path1 = zooKeeper.create("/user/wuhulala", "wuhulala".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Success create znode : " + path1);

        String path2 = zooKeeper.create("/user/test", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Success create znode : " + path2);
    }

    static class StringCallback implements AsyncCallback.StringCallback{
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("响应码: " + rc);
            System.out.println("传入路径: " + path);
            System.out.println("上下文: " + ctx);
            System.out.println("创建成功的节点名: " + name);
        }
    }
}
