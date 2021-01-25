package com.huawei.dmestore.model;

/**
 * RelationInstance
 *
 * @ClassName: RelationInstance
 * @Company: GH-CA
 * @author: liuxh
 * @since 2020-09-15
 **/
public class RelationInstance {
    /**
     * sourceInstanceId .
     */
    private String sourceInstanceId;
    /**
     * targetInstanceId .
     */
    private String targetInstanceId;
    /**
     * relationName .
     */
    private String relationName;
    /**
     * id .
     */
    private String id;
    /**
     * lastModified .
     */
    private long lastModified;
    /**
     * resId .
     */
    private String resId;
    /**
     * relationId .
     */
    private int relationId;

    /**
     * getSourceInstanceId .
     *
     * @return String.
     */
    public String getSourceInstanceId() {
        return sourceInstanceId;
    }

    /**
     * setSourceInstanceId .
     *
     * @param param .
     */
    public void setSourceInstanceId(final String param) {
        this.sourceInstanceId = param;
    }

    /**
     * getTargetInstanceId .
     *
     * @return String.
     */
    public String getTargetInstanceId() {
        return targetInstanceId;
    }

    /**
     * setTargetInstanceId .
     *
     * @param param .
     */
    public void setTargetInstanceId(final String param) {
        this.targetInstanceId = param;
    }

    /**
     * getRelationName .
     *
     * @return String.
     */
    public String getRelationName() {
        return relationName;
    }

    /**
     * setRelationName .
     *
     * @param param .
     */
    public void setRelationName(final String param) {
        this.relationName = param;
    }

    /**
     * getId .
     *
     * @return String.
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
     * getLastModified .
     *
     * @return long.
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * setSourceInstanceId .
     *
     * @param param .
     */
    public void setLastModified(final long param) {
        this.lastModified = param;
    }

    /**
     * getResId .
     *
     * @return String.
     */
    public String getResId() {
        return resId;
    }

    /**
     * setResId .
     *
     * @param param .
     */
    public void setResId(final String param) {
        this.resId = param;
    }

    /**
     * getRelationId .
     *
     * @return int.
     */
    public int getRelationId() {
        return relationId;
    }

    /**
     * setRelationId .
     *
     * @param param .
     */
    public void setRelationId(final int param) {
        this.relationId = param;
    }
}
