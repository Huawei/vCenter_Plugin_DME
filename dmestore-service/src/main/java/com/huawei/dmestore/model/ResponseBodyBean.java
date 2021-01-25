package com.huawei.dmestore.model;

/**
 * ResponseBodyBean
 *
 * @author Administrator .
 * @since 2020-12-08
 */
public class ResponseBodyBean {
    /**
     * code .
     */
    private String code;
    /**
     * data .
     */
    private Object data;
    /**
     * description .
     */
    private String description;

    /**
     * ResponseBodyBean .
     */
    public ResponseBodyBean() {
    }

    /**
     * ResponseBodyBean .
     *
     * @param param1 code .
     * @param param2 data .
     * @param param3 description .
     */
    public ResponseBodyBean(final String param1,
                            final Object param2,
                            final String param3) {
        this.code = param1;
        this.data = param2;
        this.description = param3;
    }

    /**
     * getData .
     *
     * @return Object .
     */
    public Object getData() {
        return data;
    }

    /**
     * setData .
     *
     * @param param .
     */
    public void setData(final Object param) {
        this.data = param;
    }

    /**
     * getDescription .
     *
     * @return String .
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription .
     *
     * @param param .
     */
    public void setDescription(final String param) {
        this.description = param;
    }

    /**
     * getCode .
     *
     * @return String .
     */
    public String getCode() {
        return code;
    }

    /**
     * setCode .
     *
     * @param param .
     */
    public void setCode(final String param) {
        this.code = param;
    }

    @Override
    public final String toString() {
        return "ResponseBodyBean{"
            + "code='" + code + '\''
            + ", data='" + data + '\''
            + ", description='" + description + '\''
            + '}';
    }
}
