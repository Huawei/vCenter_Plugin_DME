package com.huawei.dmestore.model;

/**
 * FileSystemTurning
 *
 * @author lianq
 * @ClassName: FileSystemTurning
 * @since 2020-10-15
 */
public class FileSystemTurning {
    /**
     * 重复数据删除。默认关闭 .
     */
    private Boolean deduplicationEnabled;
    /**
     * 数据压缩。默认关闭 .
     */
    private Boolean compressionEnabled;
    /**
     * 文件系统分配类型 .
     */
    private String allocationType;
    /**
     * smartQos .
     */
    private SmartQos smartQos;

    /**
     * getDeduplicationEnabled .
     *
     * @return Boolean .
     */
    public Boolean getDeduplicationEnabled() {
        return deduplicationEnabled;
    }

    /**
     * setDeduplicationEnabled .
     *
     * @param param .
     */
    public void setDeduplicationEnabled(final Boolean param) {
        this.deduplicationEnabled = param;
    }

    /**
     * getCompressionEnabled .
     *
     * @return Boolean .
     */
    public Boolean getCompressionEnabled() {
        return compressionEnabled;
    }

    /**
     * setCompressionEnabled .
     *
     * @param param .
     */
    public void setCompressionEnabled(final Boolean param) {
        this.compressionEnabled = param;
    }

    /**
     * getAllocationType .
     *
     * @return String .
     */
    public String getAllocationType() {
        return allocationType;
    }

    /**
     * setAllocationType .
     *
     * @param param .
     */
    public void setAllocationType(final String param) {
        this.allocationType = param;
    }

    /**
     * getSmartQos .
     *
     * @return SmartQos .
     */
    public SmartQos getSmartQos() {
        return smartQos;
    }

    /**
     * setSmartQos .
     *
     * @param param .
     */
    public void setSmartQos(final SmartQos param) {
        this.smartQos = param;
    }
}
