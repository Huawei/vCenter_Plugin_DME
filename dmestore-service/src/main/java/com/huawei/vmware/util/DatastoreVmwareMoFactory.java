package com.huawei.vmware.util;

import com.huawei.vmware.mo.DatastoreMO;

import com.vmware.vim25.ManagedObjectReference;

/**
 * DatastoreMOFactory
 *
 * @author lianq
 * @ClassName: DatastoreMOFactory
 * @since 2020-12-09
 */
public class DatastoreVmwareMoFactory {
    private static DatastoreVmwareMoFactory datastoreVmwareMoFactory;

    private DatastoreVmwareMoFactory() {
    }

    /**
     * getInstance
     *
     * @return DatastoreMOFactory
     */
    public static DatastoreVmwareMoFactory getInstance() {
        if (datastoreVmwareMoFactory == null) {
            synchronized (DatastoreVmwareMoFactory.class) {
                if (datastoreVmwareMoFactory == null) {
                    datastoreVmwareMoFactory = new DatastoreVmwareMoFactory();
                }
            }
        }
        return datastoreVmwareMoFactory;
    }

    /**
     * build
     *
     * @param context context
     * @param morDatastore morDatastore
     * @return DatastoreMO
     * @throws Exception Exception
     */
    public DatastoreMO build(VmwareContext context, ManagedObjectReference morDatastore) throws Exception {
        return new DatastoreMO(context, morDatastore);
    }
}
