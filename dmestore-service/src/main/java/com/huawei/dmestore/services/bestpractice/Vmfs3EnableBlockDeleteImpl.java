package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;

/**
 * Vmfs3EnableBlockDeleteImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class Vmfs3EnableBlockDeleteImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "VMFS3.EnableBlockDelete";
    }

    @Override
    public Object getRecommendValue() {
        return 1L;
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        return super.getCurrentValue(vcsdkUtils, objectId, getHostSetting());
    }

    @Override
    public String getLevel() {
        return "Info";
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
