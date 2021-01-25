package com.huawei.vmware.util;

import com.huawei.vmware.mo.DatacenterMO;

/**
 * DatacenterMOFactory
 *
 * @author lianq
 * @ClassName: DatacenterMOFactory
 * @since 2020-12-09
 */
public class DatacenterVmwareMoFactory {
    private static DatacenterVmwareMoFactory datacenterVmwareMoFactory;

    private DatacenterVmwareMoFactory() {
    }

    /**
     * getInstance
     *
     * @return DatacenterMOFactory
     */
    public static DatacenterVmwareMoFactory getInstance() {
        if (datacenterVmwareMoFactory == null) {
            synchronized (DatacenterVmwareMoFactory.class) {
                if (datacenterVmwareMoFactory == null) {
                    datacenterVmwareMoFactory = new DatacenterVmwareMoFactory();
                }
            }
        }
        return datacenterVmwareMoFactory;
    }

    /**
     * build
     *
     * @param context context
     * @param dcName dcName
     * @return DatacenterMO
     * @throws Exception Exception
     */
    public DatacenterMO build(VmwareContext context, String dcName) throws Exception {
        return new DatacenterMO(context, dcName);
    }
}
