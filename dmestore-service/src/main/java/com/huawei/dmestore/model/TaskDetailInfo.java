package com.huawei.dmestore.model;

import java.util.List;

/**
 * TaskDetailInfo
 *
 * @ClassName: TaskDetailInfo
 * @Company: GH-CA
 * @author: liuxh
 * @since 2020-09-08
 **/
public class TaskDetailInfo {
    /**
     * taskID.
     */
    private String id;
    /**
     * task name.
     */
    private String taskName;
    /**
     * status.
     */
    private int status;
    /**
     * progress.
     */
    private int progress;
    /**
     * ownerName.
     */
    private String ownerName;
    /**
     * createTiem.
     */
    private long createTiem;
    /**
     * startTime.
     */
    private long startTime;
    /**
     * endTime.
     */
    private long endTime;
    /**
     * detail.
     */
    private String detail;
    /**
     * resources.
     */
    private List<TaskDetailResource> resources;

    /**
     * getId .
     *
     * @return String .
     */
    public String getId() {
        return id;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setId(final String param) {
        this.id = param;
    }

    /**
     * getTaskName .
     *
     * @return String .
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * setTaskName .
     *
     * @param param .
     */
    public void setTaskName(final String param) {
        this.taskName = param;
    }

    /**
     * getStatus .
     *
     * @return String .
     */
    public int getStatus() {
        return status;
    }

    /**
     * setStatus .
     *
     * @param param .
     */
    public void setStatus(final int param) {
        this.status = param;
    }

    /**
     * getProgress .
     *
     * @return int .
     */
    public int getProgress() {
        return progress;
    }

    /**
     * setProgress .
     *
     * @param param .
     */
    public void setProgress(final int param) {
        this.progress = param;
    }

    /**
     * getOwnerName .
     *
     * @return String .
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * setOwnerName .
     *
     * @param param .
     */
    public void setOwnerName(final String param) {
        this.ownerName = param;
    }

    /**
     * getCreateTiem .
     *
     * @return long .
     */
    public long getCreateTiem() {
        return createTiem;
    }

    /**
     * setCreateTiem .
     *
     * @param param .
     */
    public void setCreateTiem(final long param) {
        this.createTiem = param;
    }

    /**
     * getStartTime .
     *
     * @return long .
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * setStartTime .
     *
     * @param param .
     */
    public void setStartTime(final long param) {
        this.startTime = param;
    }

    /**
     * getEndTime .
     *
     * @return long .
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * setEndTime .
     *
     * @param param .
     */
    public void setEndTime(final long param) {
        this.endTime = param;
    }

    /**
     * getDetail .
     *
     * @return String .
     */
    public String getDetail() {
        return detail;
    }

    /**
     * setDetail .
     *
     * @param param .
     */
    public void setDetail(final String param) {
        this.detail = param;
    }

    /**
     * getResources .
     *
     * @return List<> .
     */
    public List<TaskDetailResource> getResources() {
        return resources;
    }

    /**
     * setResources .
     *
     * @param param .
     */
    public void setResources(final List<TaskDetailResource> param) {
        this.resources = param;
    }
}
