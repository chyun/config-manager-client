package com.chyun.config.manager.client.core;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

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
        zkClient = CuratorFrameworkFactory.newClient(zkConnectString, retryPolicy);
        zkClient.start();
    }

    public void setZkConnectString(String zkConnectString) {
        this.zkConnectString = zkConnectString;
    }

    public String getZkConnectString() {
        return this.zkConnectString;
    }
}
