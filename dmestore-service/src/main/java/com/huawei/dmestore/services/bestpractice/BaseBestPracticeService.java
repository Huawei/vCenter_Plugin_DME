package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostAdvanceOptionMO;
import com.huawei.vmware.mo.HostMO;
import com.huawei.vmware.util.DatastoreVmwareMoFactory;
import com.huawei.vmware.util.HostVmwareFactory;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OptionValue;

import java.util.List;

/**
 * BaseBestPracticeService 最佳实践实现基类
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class BaseBestPracticeService {
    protected boolean check(final VCSDKUtils vcsdkUtils, final String objectId, final String hostSetting,
        final Object recommendValue) throws Exception {
        Object currentValue = getCurrentValue(vcsdkUtils, objectId, hostSetting);
        if (String.valueOf(currentValue).equals(String.valueOf(recommendValue))) {
            return true;
        } else {
            return false;
        }
    }

    protected void update(final VCSDKUtils vcsdkUtils, final String objectId, final String hostSetting,
        final Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        if (check(vcsdkUtils, objectId, hostSetting, recommendValue)) {
            return;
        }
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        List<OptionValue> values = hostMo.getHostAdvanceOptionMo().queryOptions(hostSetting);
        for (OptionValue value : values) {
            value.setValue(recommendValue);
        }
        hostMo.getHostAdvanceOptionMo().updateOptions(values);
    }

    protected Object getCurrentValue(final VCSDKUtils vcsdkUtils, final String objectId, final String hostSetting)
        throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        HostAdvanceOptionMO hostAdvanceOptionMo = hostMo.getHostAdvanceOptionMo();
        List<OptionValue> values = hostAdvanceOptionMo.queryOptions(hostSetting);
        for (OptionValue value : values) {
            return value.getValue();
        }
        return "--";
    }

    protected boolean checkModuleOption(final VCSDKUtils vcsdkUtils, final String objectId, final String optionName,
        final Object recommendValue) throws Exception {
        String currentValue = getCurrentModuleOption(vcsdkUtils, objectId, optionName);
        if (currentValue.equals(String.valueOf(recommendValue))) {
            return true;
        } else {
            return false;
        }
    }

    protected String getCurrentModuleOption(final VCSDKUtils vcsdkUtils, final String objectId, final String optionName)
        throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        String modlueOption = hostMo.getHostKernelModuleSystemMo().queryConfiguredModuleOptionString(optionName);
        String[] modlues = modlueOption.split("=");
        return modlues[1];
    }

    protected void updateModuleOption(final VCSDKUtils vcsdkUtils, final String objectId, final String optionName,
        final Object recommendValue) throws Exception {
        if (checkModuleOption(vcsdkUtils, objectId, optionName, recommendValue)) {
            return;
        }
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        String options = optionName + "=" + recommendValue;
        hostMo.getHostKernelModuleSystemMo().updateModuleOptionString(optionName, options);
    }

    public HostVmwareFactory getHostMoFactory() {
        return HostVmwareFactory.getInstance();
    }

    public DatastoreVmwareMoFactory getDatastoreMoFactory() {
        return DatastoreVmwareMoFactory.getInstance();
    }
}
