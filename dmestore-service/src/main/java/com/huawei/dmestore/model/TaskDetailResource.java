package com.huawei.dmestore.model;

/**
 * TaskDetailResource
 *
 * @ClassName: TaskDetailResource
 * @Company: GH-CA
 * @author: liuxh
 * @since 2020-09-18
 **/
public class TaskDetailResource {
    /**
     * operate .
     */
    private String operate;
    /**
     * type .
     */
    private String type;
    /**
     * id .
     */
    private String id;
    /**
     * name .
     */
    private String name;

    /**
     * getOperate .
     *
     * @return String .
     */
    public String getOperate() {
        return operate;
    }

    /**
     * setOperate .
     *
     * @param param .
     */
    public void setOperate(final String param) {
        this.operate = param;
    }

    /**
     * getType .
     *
     * @return String .
     */
    public String getType() {
        return type;
    }

    /**
     * setType .
     *
     * @param param .
     */
    public void setType(final String param) {
        this.type = param;
    }

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
     * getName .
     *
     * @return String .
     */
    public String getName() {
        return name;
    }

    /**
     * setName .
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
    }
}
