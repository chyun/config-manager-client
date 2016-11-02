package com.chyun.config.manager.client.core;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * 类的实现描述: Created by Calix on 31/10/16.
 */
public class RemoteConfigReader {
    private static final String DEFAULT_NAMESPACE = "/config-center";
    private static final String DEFAULT_DECODE = "utf-8";
    private CuratorFramework zkClient;
    private String zkConnectString;

    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        zkConnectString = "127.0.0.1:2181";
        zkClient = CuratorFrameworkFactory.newClient(zkConnectString, retryPolicy);
        zkClient.start();
    }

    public byte[] read(String path) throws Exception {
        ZKWatch watch = new ZKWatch(path);
        byte[] buffer = zkClient.
                getData().
                usingWatcher(watch).forPath(path);
        return buffer;
    }

    public void setZkConnectString(String zkConnectString) {
        this.zkConnectString = zkConnectString;
    }

    public String getZkConnectString() {
        return this.zkConnectString;
    }

    public class ZKWatch implements CuratorWatcher {
        private final String path;

        public String getPath() {
            return path;
        }

        public ZKWatch(String path) {
            this.path = path;
        }

        @Override
        public void process(WatchedEvent event) throws Exception {
            //System.out.println(event.getType());
            if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
                byte[] data = zkClient.
                        getData().
                        usingWatcher(this).forPath(path);
                //System.out.println("NodeDataChanged path: " + path + new String(data, Charset.forName("utf-8")));
                ConfigUtil.modifyProperty(path, data);
            }
        }
    }
}
