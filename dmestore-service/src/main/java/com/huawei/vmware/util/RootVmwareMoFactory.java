package com.huawei.vmware.util;

import com.huawei.vmware.mo.RootFsMO;

import com.vmware.vim25.ManagedObjectReference;

/**
 * RootFsMOFactory
 *
 * @author lianq
 * @ClassName: RootFsMOFactory
 * @since 2020-12-09
 */
public class RootVmwareMoFactory {
    private static RootVmwareMoFactory rootFactory;

    private RootVmwareMoFactory() {
    }

    /**
     * getInstance
     *
     * @return RootFsMOFactory
     */
    public static RootVmwareMoFactory getInstance() {
        if (rootFactory == null) {
            synchronized (RootVmwareMoFactory.class) {
                if (rootFactory == null) {
                    rootFactory = new RootVmwareMoFactory();
                }
            }
        }
        return rootFactory;
    }

    /**
     * build
     *
     * @param context context
     * @param mor mor
     * @return RootFsMO
     */
    public RootFsMO build(VmwareContext context, ManagedObjectReference mor) {
        return new RootFsMO(context, mor);
    }
}
