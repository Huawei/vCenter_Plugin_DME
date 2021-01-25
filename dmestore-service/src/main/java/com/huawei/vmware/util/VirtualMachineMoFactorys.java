package com.huawei.vmware.util;

import com.huawei.vmware.mo.VirtualMachineMO;

import com.vmware.vim25.ManagedObjectReference;

/**
 * VirtualMachineMOFactory
 *
 * @author lianq
 * @ClassName: VirtualMachineMOFactory
 * @since 2020-12-10
 */
public class VirtualMachineMoFactorys {
    private static VirtualMachineMoFactorys virtualMachineMoFactorys;

    private VirtualMachineMoFactorys() {
    }

    /**
     * VirtualMachineMOFactory\
     *
     * @return VirtualMachineMOFactory
     */
    public static VirtualMachineMoFactorys getInstance() {
        if (virtualMachineMoFactorys == null) {
            synchronized (VirtualMachineMoFactorys.class) {
                if (virtualMachineMoFactorys == null) {
                    virtualMachineMoFactorys = new VirtualMachineMoFactorys();
                }
            }
        }
        return virtualMachineMoFactorys;
    }

    /**
     * build
     *
     * @param context context
     * @param morVm morVm
     * @return VirtualMachineMO
     */
    public VirtualMachineMO build(VmwareContext context, ManagedObjectReference morVm) {
        return new VirtualMachineMO(context, morVm);
    }
}
