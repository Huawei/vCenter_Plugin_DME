package com.huawei.vmware.mo;

import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;

import java.util.List;

/**
 * RootFsMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class RootFsMO extends BaseMO {
    /**
     * RootFsMO
     *
     * @param context context
     * @param mor     mor
     */
    public RootFsMO(VmwareContext context, ManagedObjectReference mor) {
        super(context, mor);
    }

    /**
     * getAllHostOnRootFs
     *
     * @return List
     * @throws InvalidPropertyFaultMsg InvalidPropertyFaultMsg
     * @throws RuntimeFaultFaultMsg    RuntimeFaultFaultMsg
     */
    public List<Pair<ManagedObjectReference, String>> getAllHostOnRootFs()
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<Pair<ManagedObjectReference, String>> hosts = context.inFolderByType(context.getRootFolder(),
            "HostSystem");
        return hosts;
    }

    /**
     * getAllClusterOnRootFs
     *
     * @return List
     * @throws InvalidPropertyFaultMsg InvalidPropertyFaultMsg
     * @throws RuntimeFaultFaultMsg    RuntimeFaultFaultMsg
     */
    public List<Pair<ManagedObjectReference, String>> getAllClusterOnRootFs()
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<Pair<ManagedObjectReference, String>> clusters = context.inFolderByType(context.getRootFolder(),
            "ClusterComputeResource");
        return clusters;
    }

    /**
     * getAllDatastoreOnRootFs
     *
     * @return List
     * @throws InvalidPropertyFaultMsg InvalidPropertyFaultMsg
     * @throws RuntimeFaultFaultMsg    RuntimeFaultFaultMsg
     */
    public List<Pair<ManagedObjectReference, String>> getAllDatastoreOnRootFs()
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<Pair<ManagedObjectReference, String>> hosts = context.inFolderByType(context.getRootFolder(), "Datastore");
        return hosts;
    }

    /**
     * findHostById
     *
     * @param id id
     * @return HostMO
     * @throws Exception Exception
     */
    public HostMO findHostById(String id) throws Exception {
        HostMO objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllHostOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                HostMO submo = new HostMO(context, obj.first());
                if (id.equals(submo.getMor().getValue())) {
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }

    /**
     * findClusterById
     *
     * @param id id
     * @return ClusterMO
     * @throws Exception Exception
     */
    public ClusterMO findClusterById(String id) throws Exception {
        ClusterMO objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllClusterOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                ClusterMO submo = new ClusterMO(context, obj.first());
                if (id.equals(submo.getMor().getValue())) {
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }
}
