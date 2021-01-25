package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;

/**
 * LunQueueDepthForEmulexImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class LunQueueDepthForEmulexImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "LUN Queue Depth for Emulex";
    }

    @Override
    public Object getRecommendValue() {
        return "512";
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        return super.getCurrentModuleOption(vcsdkUtils, objectId, getOptionName());
    }

    @Override
    public String getLevel() {
        return "Warning";
    }

    @Override
    public boolean needReboot() {
        return true;
    }

    @Override
    public boolean autoRepair() {
        return true;
    }

    @Override
    public boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        return super.checkModuleOption(vcsdkUtils, objectId, getOptionName(), getRecommendValue());
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        super.updateModuleOption(vcsdkUtils, objectId, getOptionName(), getRecommendValue());
    }

    private String getOptionName() {
        return "lpfc0_lun_queue_depth";
    }
}
