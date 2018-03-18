package com.wuhulala.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/3/18
 */
public class ZkClientDemo {
    public static void main(String[] args) {

        String zkServers = "zoo1:2181";
        ZkClient zk = ZkClientFactory.getInstance(zkServers);

        // 创建节点
        zk.createPersistent("/node", "my nodes");
        zk.createEphemeral("/node/user");

        // 获取节点
        List<String> childrenList = zk.getChildren("/node");
        System.out.println("子节点：" + childrenList);

        // 更新节点
        String data = zk.readData("/node");
        System.out.println(data);

        // 删除节点
        zk.delete("/node/user");
    }
}
