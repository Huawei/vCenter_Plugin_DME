package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.HostConfigFaultFaultMsg;
import com.vmware.vim25.HostFileSystemVolumeInfo;
import com.vmware.vim25.HostInternetScsiHbaSendTarget;
import com.vmware.vim25.HostMultipathInfoLogicalUnitPolicy;
import com.vmware.vim25.HostStorageDeviceInfo;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;

import java.util.List;

/**
 * HostStorageSystemMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class HostStorageSystemMO extends BaseMO {
    /**
     * HostStorageSystemMO
     *
     * @param context context
     * @param morHostDatastore morHostDatastore
     */
    public HostStorageSystemMO(VmwareContext context, ManagedObjectReference morHostDatastore) {
        super(context, morHostDatastore);
    }

    /**
     * getStorageDeviceInfo
     *
     * @return HostStorageDeviceInfo
     * @throws Exception Exception
     */
    public HostStorageDeviceInfo getStorageDeviceInfo() throws Exception {
        return (HostStorageDeviceInfo) context.getVimClient().getDynamicProperty(mor, "storageDeviceInfo");
    }

    /**
     * getHostFileSystemVolumeInfo
     *
     * @return HostFileSystemVolumeInfo
     * @throws Exception Exception
     */
    public HostFileSystemVolumeInfo getHostFileSystemVolumeInfo() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "fileSystemVolumeInfo");
    }

    /**
     * rescanHba
     *
     * @param isCsiHbaDevice isCsiHbaDevice
     * @throws Exception Exception
     */
    public void rescanHba(String isCsiHbaDevice) throws Exception {
        context.getService().rescanHba(mor, isCsiHbaDevice);
    }

    /**
     * rescanVmfs
     *
     * @throws Exception Exception
     */
    public void rescanVmfs() throws Exception {
        context.getService().rescanVmfs(mor);
    }

    /**
     * refreshStorageSystem
     *
     * @throws Exception Exception
     */
    public void refreshStorageSystem() throws Exception {
        context.getService().refreshStorageSystem(mor);
    }

    /**
     * mountVmfsVolume
     *
     * @param datastoreUuid datastoreUuid
     * @throws Exception Exception
     */
    public void mountVmfsVolume(String datastoreUuid) throws Exception {
        context.getService().mountVmfsVolume(mor, datastoreUuid);
    }

    /**
     * unmountVmfsVolume
     *
     * @param datastoreUuid datastoreUuid
     * @throws Exception Exception
     */
    public void unmountVmfsVolume(String datastoreUuid) throws Exception {
        context.getService().unmountVmfsVolume(mor, datastoreUuid);
    }

    /**
     * unmapVmfsVolumeExTask
     *
     * @param vmfsUuids vmfsUuids
     * @throws Exception Exception
     */
    public void unmapVmfsVolumeExTask(List<String> vmfsUuids) throws Exception {
        context.getService().unmapVmfsVolumeExTask(mor, vmfsUuids);
    }

    /**
     * setMultipathLunPolicy
     *
     * @param lunId lunId
     * @param hostMultipathInfoLogicalUnitPolicy hostMultipathInfoLogicalUnitPolicy
     * @throws Exception Exception
     */
    public void setMultipathLunPolicy(String lunId,
        HostMultipathInfoLogicalUnitPolicy hostMultipathInfoLogicalUnitPolicy) throws Exception {
        context.getService().setMultipathLunPolicy(mor, lunId, hostMultipathInfoLogicalUnitPolicy);
    }

    /**
     * addInternetScsiSendTargets
     *
     * @param iscsiHbaDevice iscsiHbaDevice
     * @param targets targets
     * @throws HostConfigFaultFaultMsg HostConfigFaultFaultMsg
     * @throws NotFoundFaultMsg NotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg RuntimeFaultFaultMsg
     */
    public void addInternetScsiSendTargets(String iscsiHbaDevice, List<HostInternetScsiHbaSendTarget> targets)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
        context.getService().addInternetScsiSendTargets(mor, iscsiHbaDevice, targets);
    }
}
