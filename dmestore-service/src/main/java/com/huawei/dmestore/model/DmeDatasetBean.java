package com.huawei.dmestore.model;

/**
 * DmeDatasetBean
 *
 * @author wangxiangyong
 * @since 2021-01-11
 */
public class DmeDatasetBean {
    private float responseTime;
    private String name;
    private float throughput;
    private long timestamp;

    public float getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(float responseTime) {
        this.responseTime = responseTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getThroughput() {
        return throughput;
    }

    public void setThroughput(float throughput) {
        this.throughput = throughput;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
