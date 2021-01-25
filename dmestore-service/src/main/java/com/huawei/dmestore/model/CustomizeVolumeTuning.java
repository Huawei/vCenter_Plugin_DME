package com.huawei.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CustomizeVolumeTuning
 *
 * @author lianq
 * @ClassName: CustomizeVolumeTuning
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class CustomizeVolumeTuning {
    /**
     * smartQos .
     */
    private SmartQos smartQos;

    /**
     * compressionEnabled .
     */
    @JsonProperty(value = "compression_enabled")
    private Boolean compressionEnabled;

    /**
     * dedupeEnabled .
     */
    @JsonProperty(value = "dedupe_enabled")
    private Boolean dedupeEnabled;

    /**
     * 数据迁移策略，取值范围 0：不迁移，1：自动迁移，
     * 2：向高性能层迁移，3：向低性能层迁移  默认值：不迁移 .
     */
    private Integer smarttier;

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

    /**
     * getCompressionEnabled .
     *
     * @return Boolean .
     */
    public Boolean getCompressionEnabled() {
        return compressionEnabled;
    }

    /**
     * setSmartQos .
     *
     * @param param .
     */
    public void setCompressionEnabled(final Boolean param) {
        this.compressionEnabled = param;
    }

    /**
     * getDedupeEnabled .
     *
     * @return Boolean .
     */
    public Boolean getDedupeEnabled() {
        return dedupeEnabled;
    }

    /**
     * setSmartQos .
     *
     * @param param .
     */
    public void setDedupeEnabled(final Boolean param) {
        this.dedupeEnabled = param;
    }

    /**
     * getSmarttier .
     *
     * @return Integer .
     */
    public Integer getSmarttier() {
        return smarttier;
    }

    /**
     * setSmartQos .
     *
     * @param param .
     */
    public void setSmarttier(final Integer param) {
        this.smarttier = param;
    }

    @Override
    public final String toString() {
        return "CustomizeVolumeTuning{" + "smartQos=" + smartQos + ", compressionEnabled=" + compressionEnabled
            + ", dedupeEnabled=" + dedupeEnabled + ", smarttier=" + smarttier + '}';
    }
}
