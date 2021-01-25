package com.huawei.dmestore.model;

/**
 * FileSystemDetail
 *
 * @author lianq
 * @since 2020-10-15
 */
public class FileSystemDetail {
    /**
     * id .
     */
    private String id;
    /**
     * name .
     */
    private String name;
    /**
     * fileSystemTurning .
     */
    private FileSystemTurning fileSystemTurning;
    /**
     * 自动调整容量开关。 false: 关闭；true：打开。默认打开 .
     */
    private CapacityAutonegotiation capacityAutonegotiation;

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

    /**
     * getFileSystemTurning .
     *
     * @return FileSystemTurning .
     */
    public FileSystemTurning getFileSystemTurning() {
        return fileSystemTurning;
    }

    /**
     * setFileSystemTurning .
     *
     * @param param .
     */
    public void setFileSystemTurning(final FileSystemTurning param) {
        this.fileSystemTurning = param;
    }

    /**
     * getCapacityAutonegotiation .
     *
     * @return CapacityAutonegotiation .
     */
    public CapacityAutonegotiation getCapacityAutonegotiation() {
        return capacityAutonegotiation;
    }

    /**
     * setCapacityAutonegotiation .
     *
     * @param param .
     */
    public void setCapacityAutonegotiation(
        final CapacityAutonegotiation param) {
        this.capacityAutonegotiation = param;
    }
}
