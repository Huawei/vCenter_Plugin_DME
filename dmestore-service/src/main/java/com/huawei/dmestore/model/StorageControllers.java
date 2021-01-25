package com.huawei.dmestore.model;

/**
 * StorageControllers
 *
 * @author lianq
 * @ClassName: StorageDisk
 * @since 2020-12-08
 */
public class StorageControllers {
    /**
     * id .
     */
    private String id;
    /**
     * name .
     */
    private String name;
    /**
     * status .
     */
    private String status;
    /**
     * softVer .
     */
    private String softVer;
    /**
     * cpuInfo .
     */
    private String cpuInfo;
    /**
     * cpuUsage .
     */
    private Float cpuUsage;
    /**
     * iops .
     */
    private Float iops;
    /**
     * ops .
     */
    private Float ops;
    /**
     * lantency .
     */
    private Float lantency;
    /**
     * bandwith .
     */
    private Float bandwith;

    /**
     * getName .
     *
     * @return String 。
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
     * getStatus .
     *
     * @return String 。
     */
    public String getStatus() {
        return status;
    }

    /**
     * setStatus .
     *
     * @param param .
     */
    public void setStatus(final String param) {
        this.status = param;
    }

    /**
     * getSoftVer .
     *
     * @return String 。
     */
    public String getSoftVer() {
        return softVer;
    }

    /**
     * setSoftVer .
     *
     * @param param .
     */
    public void setSoftVer(final String param) {
        this.softVer = param;
    }

    /**
     * getCpuInfo .
     *
     * @return String 。
     */
    public String getCpuInfo() {
        return cpuInfo;
    }

    /**
     * setCpuInfo .
     *
     * @param param .
     */
    public void setCpuInfo(final String param) {
        this.cpuInfo = param;
    }

    /**
     * getCpuUsage .
     *
     * @return Float 。
     */
    public Float getCpuUsage() {
        return cpuUsage;
    }

    /**
     * setCpuUsage .
     *
     * @param param .
     */
    public void setCpuUsage(final Float param) {
        this.cpuUsage = param;
    }

    /**
     * getIops .
     *
     * @return Float 。
     */
    public Float getIops() {
        return iops;
    }

    /**
     * setIops .
     *
     * @param param .
     */
    public void setIops(final Float param) {
        this.iops = param;
    }

    /**
     * getOps .
     *
     * @return Float 。
     */
    public Float getOps() {
        return ops;
    }

    /**
     * setOps .
     *
     * @param param .
     */
    public void setOps(final Float param) {
        this.ops = param;
    }

    /**
     * getLantency .
     *
     * @return Float 。
     */
    public Float getLantency() {
        return lantency;
    }

    /**
     * setLantency .
     *
     * @param param .
     */
    public void setLantency(final Float param) {
        this.lantency = param;
    }

    /**
     * getBandwith .
     *
     * @return Float 。
     */
    public Float getBandwith() {
        return bandwith;
    }

    /**
     * setBandwith .
     *
     * @param param .
     */
    public void setBandwith(final Float param) {
        this.bandwith = param;
    }

    /**
     * getId .
     *
     * @return String 。
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
}
