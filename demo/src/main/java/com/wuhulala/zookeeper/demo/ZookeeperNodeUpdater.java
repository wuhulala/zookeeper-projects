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
 * 节点更新
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/3/17
 */
public class ZookeeperNodeUpdater implements Watcher {

    private static ZooKeeper zooKeeper = null;

    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String connectString = "zoo1:2181,zoo2:2182,zoo3:2183";
        zooKeeper = ZookeeperConstructor.getInstance(connectString, new ZookeeperNodeUpdater());

        zooKeeper.create("/user/wuhulala1", "wuhulala".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        byte[] data = zooKeeper.getData("/user/wuhulala1", true, stat);
        System.out.println("oldData: " + new String(data));

        // -1 表示 更新最新的数据
        zooKeeper.setData("/user/wuhulala1", "12231123213".getBytes(), -1);

        // 判断zookeeper节点是否存在
        zooKeeper.exists("/user/wuhulala", true);
        zooKeeper.create("/user/wuhulala", "wuhulala".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.delete("/user/wuhulala", -1);

        Thread.sleep(6000);
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getType() == Event.EventType.NodeDataChanged){
            try {
                System.out.println(new String(zooKeeper.getData(event.getPath(), true, stat)));
                System.out.println(stat);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
