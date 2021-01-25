package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.DatastoreMO;
import com.huawei.vmware.mo.HostMO;
import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.ManagedObjectReference;

import java.util.List;

/**
 * NumberOfVolumesInDatastoreImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class NumberOfVolumesInDatastoreImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "Number of volumes in Datastore";
    }

    @Override
    public Object getRecommendValue() {
        return 1;
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = hostMo.getDatastoreMountsOnHost();
        JsonArray array = new JsonArray();
        for (Pair<ManagedObjectReference, String> pair : datastoreMountsOnHost) {
            ManagedObjectReference dsMor = pair.first();
            DatastoreMO datastoreMo = this.getDatastoreMoFactory().build(context, dsMor);
            DatastoreSummary summary = datastoreMo.getSummary();
            if (summary.getType().equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS)) {
                JsonObject object = new JsonObject();
                int volumeSize = datastoreMo.getVmfsDatastoreInfo().getVmfs().getExtent().size();
                if (volumeSize > Integer.parseInt(getRecommendValue().toString())) {
                    object.addProperty("dataStoreName", summary.getName());
                    object.addProperty("volumeSize", volumeSize);
                }
                array.add(object);
            }
        }

        JsonObject reObject = new JsonObject();
        reObject.add("dataStores", array);
        return reObject.toString();
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
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = hostMo.getDatastoreMountsOnHost();
        for (Pair<ManagedObjectReference, String> pair : datastoreMountsOnHost) {
            ManagedObjectReference dsMor = pair.first();
            DatastoreMO datastoreMo = this.getDatastoreMoFactory().build(context, dsMor);
            DatastoreSummary summary = datastoreMo.getSummary();
            if (summary.getType().equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS)) {
                int volumeSize = datastoreMo.getVmfsDatastoreInfo().getVmfs().getExtent().size();

                // 只要有一个存储对应的卷超过推荐值就认为该主机不满足最佳实践
                if (volumeSize > Integer.parseInt(getRecommendValue().toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        // 不可自动修复，无需处理
    }
}
