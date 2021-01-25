package com.huawei.dmestore.entity;

/**
 * ScheduleConfig.
 *
 * @author Administrator .
 * @since 2020-12-08
 */
public class ScheduleConfig {
    /**
     * id .
     */
    private int id;
    /**
     * jobName .
     */
    private String jobName;
    /**
     * className .
     */
    private String className;
    /**
     * method .
     */
    private String method;
    /**
     * cron .
     */
    private String cron;

    /**
     * getId .
     *
     * @return int .
     */
    public int getId() {
        return id;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setId(final int param) {
        this.id = param;
    }

    /**
     * getJobName .
     *
     * @return String .
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * setJobName .
     *
     * @param param .
     */
    public void setJobName(final String param) {
        this.jobName = param;
    }

    /**
     * getClassName .
     *
     * @return String .
     */
    public String getClassName() {
        return className;
    }

    /**
     * setClassName .
     *
     * @param param .
     */
    public void setClassName(final String param) {
        this.className = param;
    }

    /**
     * getMethod .
     *
     * @return String .
     */
    public String getMethod() {
        return method;
    }

    /**
     * setMethod .
     *
     * @param param .
     */
    public void setMethod(final String param) {
        this.method = param;
    }

    /**
     * getCron .
     *
     * @return String .
     */
    public String getCron() {
        return cron;
    }

    /**
     * setCron .
     *
     * @param param .
     */
    public void setCron(final String param) {
        this.cron = param;
    }
}
