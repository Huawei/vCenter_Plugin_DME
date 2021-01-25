package com.huawei.vmware.mo;

import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.DatastoreHostMount;
import com.vmware.vim25.DatastoreInfo;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.VmfsDatastoreInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * DatastoreMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class DatastoreMO extends BaseMO {
    private String name;
    private Pair<DatacenterMO, String> ownerDc;

    /**
     * DatastoreMO
     *
     * @param context context
     * @param morDatastore morDatastore
     */
    public DatastoreMO(VmwareContext context, ManagedObjectReference morDatastore) {
        super(context, morDatastore);
    }

    /**
     * DatastoreMO
     *
     * @param context context
     * @param morType morType
     * @param morValue morValue
     */
    public DatastoreMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    /**
     * DatastoreMO
     *
     * @param context context
     * @param dsName dsName
     * @throws Exception Exception
     */
    public DatastoreMO(VmwareContext context, String dsName) throws Exception {
        super(context, null);
        mor = this.context.getVimClient().getDecendentMoRef(this.context.getRootFolder(), "Datastore", dsName);
    }

    @Override
    public String getName() throws Exception {
        if (name == null) {
            name = context.getVimClient().getDynamicProperty(mor, "name");
        }

        return name;
    }

    /**
     * getInfo
     *
     * @return DatastoreInfo
     * @throws Exception Exception
     */
    public DatastoreInfo getInfo() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "info");
    }

    /**
     * getVmfsDatastoreInfo
     *
     * @return VmfsDatastoreInfo
     * @throws Exception Exception
     */
    public VmfsDatastoreInfo getVmfsDatastoreInfo() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "info");
    }

    /**
     * getSummary
     *
     * @return DatastoreSummary
     * @throws Exception Exception
     */
    public DatastoreSummary getSummary() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "summary");
    }

    /**
     * getVm
     *
     * @return List
     * @throws Exception Exception
     */
    public List<ManagedObjectReference> getVm() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "vm");
    }

    /**
     * getHostMounts
     *
     * @return List
     * @throws Exception Exception
     */
    public List<DatastoreHostMount> getHostMounts() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "host");
    }

    /**
     * getOwnerDatacenter
     *
     * @return Pair
     * @throws Exception Exception
     */
    public Pair<DatacenterMO, String> getOwnerDatacenter() throws Exception {
        if (ownerDc != null) {
            return ownerDc;
        }

        PropertySpec propertySpec = new PropertySpec();
        propertySpec.setType("Datacenter");
        propertySpec.getPathSet().add("name");

        TraversalSpec folderParentTraversal = new TraversalSpec();
        folderParentTraversal.setType("Folder");
        folderParentTraversal.setPath("parent");
        folderParentTraversal.setName("folderParentTraversal");
        SelectionSpec sSpec = new SelectionSpec();
        sSpec.setName("folderParentTraversal");
        folderParentTraversal.getSelectSet().add(sSpec);

        TraversalSpec dsParentTraversal = new TraversalSpec();
        dsParentTraversal.setType("Datastore");
        dsParentTraversal.setPath("parent");
        dsParentTraversal.setName("dsParentTraversal");
        dsParentTraversal.getSelectSet().add(folderParentTraversal);

        ObjectSpec objectSpec = new ObjectSpec();
        objectSpec.setObj(getMor());
        objectSpec.setSkip(true);
        objectSpec.getSelectSet().add(dsParentTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(propertySpec);
        pfSpec.getObjectSet().add(objectSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> ocs = context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        assert ocs != null && ocs.size() > 0;
        assert ocs.get(0).getObj() != null;
        assert ocs.get(0).getPropSet() != null;
        String dcName = ocs.get(0).getPropSet().get(0).getVal().toString();
        ownerDc = new Pair<>(new DatacenterMO(context, ocs.get(0).getObj()), dcName);
        return ownerDc;
    }

    /**
     * renameDatastore
     *
     * @param newDatastoreName newDatastoreName
     * @throws Exception Exception
     */
    public void renameDatastore(String newDatastoreName) throws Exception {
        context.getService().renameDatastore(mor, newDatastoreName);
    }

    /**
     * moveDatastoreFile
     *
     * @param srcFilePath srcFilePath
     * @param morSrcDc morSrcDc
     * @param morDestDs morDestDs
     * @param destFilePath destFilePath
     * @param morDestDc morDestDc
     * @param forceOverwrite forceOverwrite
     * @return boolean
     * @throws Exception Exception
     */
    public boolean moveDatastoreFile(String srcFilePath, ManagedObjectReference morSrcDc,
        ManagedObjectReference morDestDs, String destFilePath, ManagedObjectReference morDestDc, boolean forceOverwrite)
        throws Exception {
        String srcDsName = getName();
        DatastoreMO destDsMo = new DatastoreMO(context, morDestDs);
        String destDsName = destDsMo.getName();

        ManagedObjectReference morFileManager = context.getServiceContent().getFileManager();
        String srcFullPath = srcFilePath;
        if (!DatastoreFile.isFullDatastorePath(srcFullPath)) {
            srcFullPath = String.format("[%s] %s", srcDsName, srcFilePath);
        }

        String destFullPath = destFilePath;
        if (!DatastoreFile.isFullDatastorePath(destFullPath)) {
            destFullPath = String.format("[%s] %s", destDsName, destFilePath);
        }

        ManagedObjectReference morTask = context.getService()
            .moveDatastoreFileTask(morFileManager, srcFullPath, morSrcDc, destFullPath, morDestDc, forceOverwrite);

        boolean result = context.getVimClient().waitForTask(morTask);
        if (result) {
            context.waitForTaskProgressDone(morTask);
            return true;
        }
        return false;
    }

    /**
     * refreshDatastore
     *
     * @throws Exception Exception
     */
    public void refreshDatastore() throws Exception {
        context.getService().refreshDatastore(mor);
    }
}
