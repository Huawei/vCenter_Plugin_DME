package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;

/**
 * LunQueueDepthForQlogicImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class LunQueueDepthForQlogicImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "LUN Queue Depth for Qlogic";
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
        return "ql2xmaxqdepth";
    }
}
