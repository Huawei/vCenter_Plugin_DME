package com.huawei.vmware;

import com.huawei.vmware.util.VmwareContext;
import com.huawei.vmware.util.VmwarePluginContextFactory;

/**
 * VmwarePluginConnectionHelper
 *
 * @author Administrator
 * @since 2020-12-10
 */
public class VmwarePluginConnectionHelpers extends VcConnectionHelpers {
    @Override
    public VmwareContext getServerContext(String serverguid) throws Exception {
        return VmwarePluginContextFactory
            .getServerContext(getUserSessionService(), getVimObjectReferenceService(), serverguid);
    }

    @Override
    public VmwareContext[] getAllContext() throws Exception {
        return VmwarePluginContextFactory.getAllContext(getUserSessionService(), getVimObjectReferenceService());
    }
}
