package com.huawei.dmestore.model;

/**
 * BestPracticeUpResultResponse
 *
 * @author wangxiangyong
 *
 * @since 2020-11-10
 *
 **/
public class AuthClient {
    /**
     * accessval .
     */
    private String accessval;

    /**
     * id .
     */
    private String id;

    /**
     * name .
     */
    private String name;

    /**
     * secure .
     */
    private String secure;

    /**
     * sync .
     */
    private String sync;

    /**
     * type .
     */
    private String type;

    private String clientIdInStorage;

    public String getClientIdInStorage() {
        return clientIdInStorage;
    }

    public void setClientIdInStorage(String clientIdInStorage) {
        this.clientIdInStorage = clientIdInStorage;
    }

    /**
     * getAccessval .
     *
     * @return .
     */
    public String getAccessval() {
        return accessval;
    }

    /**
     * setAccessval .
     *
     * @param param .
     */
    public void setAccessval(final String param) {
        this.accessval = param;
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
     * @return .
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

    /**
     * getSecure .
     *
     * @return .
     */
    public String getSecure() {
        return secure;
    }

    /**
     * setSecure .
     *
     * @param param .
     */
    public void setSecure(final String param) {
        this.secure = param;
    }

    /**
     * getSync .
     *
     * @return .
     */
    public String getSync() {
        return sync;
    }

    /**
     * setSync .
     *
     * @param param .
     */
    public void setSync(final String param) {
        this.sync = param;
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

    @Override
    public final String toString() {
        return "AuthClient{"
            + "accessval='" + accessval + '\''
            + ", id='" + id + '\''
            + ", name='" + name + '\''
            + ", secure='" + secure + '\''
            + ", sync='" + sync + '\''
            + ", type='" + type + '\''
            + '}';
    }
}
