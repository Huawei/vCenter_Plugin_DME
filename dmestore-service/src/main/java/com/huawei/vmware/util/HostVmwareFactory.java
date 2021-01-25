package com.huawei.vmware.util;

import com.huawei.vmware.mo.HostMO;

import com.vmware.vim25.ManagedObjectReference;

/**
 * HostMOFactory
 *
 * @author lianq
 * @ClassName: HostMOFactory
 * @since 2020-12-09
 */
public class HostVmwareFactory {
    private static HostVmwareFactory hostVmwareFactory;

    private HostVmwareFactory() {
    }

    /**
     * getInstance
     *
     * @return HostMOFactory
     */
    public static HostVmwareFactory getInstance() {
        if (hostVmwareFactory == null) {
            synchronized (HostVmwareFactory.class) {
                if (hostVmwareFactory == null) {
                    hostVmwareFactory = new HostVmwareFactory();
                }
            }
        }
        return hostVmwareFactory;
    }

    /**
     * build
     *
     * @param context context
     * @param morHost morHost
     * @return HostMO
     * @throws Exception Exception
     */
    public HostMO build(VmwareContext context, ManagedObjectReference morHost) throws Exception {
        return new HostMO(context, morHost);
    }

    /**
     * build
     *
     * @param context  context
     * @param hostName hostName
     * @return HostMO HostMO
     * @throws Exception Exception
     */
    public HostMO build(VmwareContext context, String hostName) throws Exception {
        return new HostMO(context, hostName);
    }
}
