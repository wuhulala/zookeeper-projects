package com.wuhulala.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;

/**
 * 使用ZkClient创建一个客户端连接
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/3/18
 */
public class ZkClientFactory {

    private static ZkClient zkClient = null;

    private ZkClientFactory(){

    }

    public static ZkClient getInstance(String zkServers){
        if(zkClient == null) {
            synchronized (ZkClientFactory.class) {
                if(zkClient == null) {
                    zkClient = new ZkClient(zkServers);
                }
            }
        }
        return zkClient;
    }
}
