package com.huawei.vmware.util;

import com.huawei.vmware.mo.ClusterMO;

import com.vmware.vim25.ManagedObjectReference;

/**
 * ClusterMOFactory
 *
 * @author lianq
 * @ClassName: ClusterMOFactory
 * @since 2020-12-09
 */
public class ClusterVmwareMoFactory {
    private static ClusterVmwareMoFactory clusterVmwareMoFactory;

    private ClusterVmwareMoFactory() {
    }

    /**
     * getInstance
     *
     * @return ClusterMOFactory
     */
    public static ClusterVmwareMoFactory getInstance() {
        if (clusterVmwareMoFactory == null) {
            synchronized (ClusterVmwareMoFactory.class) {
                if (clusterVmwareMoFactory == null) {
                    clusterVmwareMoFactory = new ClusterVmwareMoFactory();
                }
            }
        }
        return clusterVmwareMoFactory;
    }

    /**
     * build
     *
     * @param context context
     * @param morCluster morCluster
     * @return ClusterMO
     * @throws Exception Exception
     */
    public ClusterMO build(VmwareContext context, ManagedObjectReference morCluster) throws Exception {
        return new ClusterMO(context, morCluster);
    }
}
