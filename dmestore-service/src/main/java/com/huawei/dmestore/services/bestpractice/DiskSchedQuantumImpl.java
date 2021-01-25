package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;

/**
 * DiskSchedQuantumImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class DiskSchedQuantumImpl extends BaseBestPracticeService implements BestPracticeService {
    private static final long RECOMMEND_VALUE = 64L;

    @Override
    public String getHostSetting() {
        return "Disk.SchedQuantum";
    }

    @Override
    public Object getRecommendValue() {
        return RECOMMEND_VALUE;
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        return super.getCurrentValue(vcsdkUtils, objectId, getHostSetting());
    }

    @Override
    public String getLevel() {
        return "Warning";
    }

    @Override
    public boolean needReboot() {
        return false;
    }

    @Override
    public boolean autoRepair() {
        return true;
    }

    @Override
    public boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        return super.check(vcsdkUtils, objectId, getHostSetting(), getRecommendValue());
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        super.update(vcsdkUtils, objectId, getHostSetting(), getRecommendValue());
    }
}
