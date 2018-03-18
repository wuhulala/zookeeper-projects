package com.wuhulala.zookeeper.demo;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * 节点查看器
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/3/17
 */
public class ZookeeperNodeReader implements Watcher {

    private static ZooKeeper zooKeeper = null;

    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String connectString = "zoo1:2181,zoo2:2182,zoo3:2183";
        zooKeeper = ZookeeperConstructor.getInstance(connectString, new ZookeeperNodeReader());

        zooKeeper.create("/user/wuhulala1", "wuhulala".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        byte[] data = zooKeeper.getData("/user/wuhulala1", true, stat);
        System.out.println(new String(data));
        List<String> children = zooKeeper.getChildren("/user", true);
        System.out.println(children);

        zooKeeper.create("/user/xueaohui1", "xueaohui".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Thread.sleep(6000);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeChildrenChanged) {
            try {
                System.out.println("接受到子节点变化时间，目前子节点有：" + zooKeeper.getChildren(event.getPath(), true));
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class StringCallback implements AsyncCallback.StringCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("响应码: " + rc);
            System.out.println("传入路径: " + path);
            System.out.println("上下文: " + ctx);
            System.out.println("创建成功的节点名: " + name);
        }
    }
}
