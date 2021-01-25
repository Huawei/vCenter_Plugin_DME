package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;

/**
 * HostKernelModuleSystemMO
 *
 * @author Administrator
 * @since 2020-12-02
 */
public class HostKernelModuleSystemMO extends BaseMO {
    /**
     * HostKernelModuleSystemMO
     *
     * @param context context
     * @param morFirewallSystem morFirewallSystem
     */
    public HostKernelModuleSystemMO(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    /**
     * queryConfiguredModuleOptionString
     *
     * @param name name
     * @return String
     * @throws Exception Exception
     */
    public String queryConfiguredModuleOptionString(String name) throws Exception {
        return context.getService().queryConfiguredModuleOptionString(mor, name);
    }

    /**
     * updateModuleOptionString
     *
     * @param name name
     * @param options options
     * @throws Exception Exception
     */
    public void updateModuleOptionString(String name, String options) throws Exception {
        context.getService().updateModuleOptionString(mor, name, options);
    }
}
