package com.wuhulala.zookeeper.demo;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 节点删除测试
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/3/17
 */
public class ZookeeperNodeDeleter {
    public static void main(String[] args) throws InterruptedException, IOException {
        String connectString = "zoo1:2181,zoo2:2182,zoo3:2183";
        ZooKeeper zooKeeper = ZookeeperConstructor.getInstance(connectString);

        //=====================================================================================
        //            同步删除一个节点
        //=====================================================================================

        deleteNodeBySync(zooKeeper);



        //=====================================================================================
        //            异步删除一个节点
        //=====================================================================================
        deleteNodeByAsync(zooKeeper);

        Thread.sleep(6000);
    }

    private static void deleteNodeByAsync(ZooKeeper zooKeeper) {
        zooKeeper.delete("/sync-user", 1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx) {
                System.out.println("状态码： " + rc);
                System.out.println("节点路径： " + path);
                System.out.println("操作上下文： " + ctx);

            }
        }, "删除上下文！！");
    }

    private static void deleteNodeBySync(ZooKeeper zooKeeper) {
        try {
            zooKeeper.delete("/user/wuhulala", 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
