package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.HostNetworkConfig;
import com.vmware.vim25.HostNetworkConfigResult;
import com.vmware.vim25.HostVirtualSwitchSpec;
import com.vmware.vim25.ManagedObjectReference;

/**
 * HostNetworkSystemMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class HostNetworkSystemMO extends BaseMO {
    /**
     * HostNetworkSystemMO
     *
     * @param context context
     * @param morNetworkSystem morNetworkSystem
     */
    public HostNetworkSystemMO(VmwareContext context, ManagedObjectReference morNetworkSystem) {
        super(context, morNetworkSystem);
    }

    /**
     * getNetworkConfig
     *
     * @return HostNetworkConfig
     * @throws Exception Exception
     */
    public HostNetworkConfig getNetworkConfig() throws Exception {
        return (HostNetworkConfig) context.getVimClient().getDynamicProperty(mor, "networkConfig");
    }

    /**
     * updateVirtualSwitch
     *
     * @param switchName switchName
     * @param spec spec
     * @throws Exception Exception
     */
    public void updateVirtualSwitch(String switchName, HostVirtualSwitchSpec spec) throws Exception {
        context.getService().updateVirtualSwitch(mor, switchName, spec);
    }

    /**
     * updateNetworkConfig
     *
     * @param config config
     * @param changeMode changeMode
     * @return HostNetworkConfigResult
     * @throws Exception Exception
     */
    public HostNetworkConfigResult updateNetworkConfig(HostNetworkConfig config, String changeMode) throws Exception {
        return context.getService().updateNetworkConfig(mor, config, changeMode);
    }
}
