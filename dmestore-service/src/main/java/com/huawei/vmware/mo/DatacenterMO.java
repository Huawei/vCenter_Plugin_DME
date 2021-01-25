package com.huawei.vmware.mo;

import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.TraversalSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DatacenterMO
 *
 * @author wangxiangyong
 * @since 2020-12-01
 *
 **/
public class DatacenterMO extends BaseMO {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatacenterMO.class);
    private static final String NAME = "name";
    private static final String DATACENTER_TYPE = "Datacenter";

    /**
     * DatacenterMO
     *
     * @param context context
     * @param morDc morDc
     */
    public DatacenterMO(VmwareContext context, ManagedObjectReference morDc) {
        super(context, morDc);
    }

    /**
     * DatacenterMO
     *
     * @param context context
     * @param dcName dcName
     * @throws Exception Exception
     */
    public DatacenterMO(VmwareContext context, String dcName) throws Exception {
        super(context, null);

        mor = this.context.getVimClient().getDecendentMoRef(this.context.getRootFolder(), DATACENTER_TYPE, dcName);
        if (mor == null) {
            LOGGER.error("Unable to locate DC {}", dcName);
        }
    }

    @Override
    public String getName() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, NAME);
    }

    /**
     * findDatastore
     *
     * @param name name
     * @return ManagedObjectReference
     * @throws Exception Exception
     */
    public ManagedObjectReference findDatastore(String name) throws Exception {
        assert name != null;

        List<ObjectContent> ocs = getDatastorePropertiesOnDatacenter(new String[] {NAME});
        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                if (oc.getPropSet().get(0).getVal().toString().equals(name)) {
                    return oc.getObj();
                }
            }
        }
        return null;
    }

    /**
     * getDatastorePropertiesOnDatacenter
     *
     * @param propertyPaths propertyPaths
     * @return List
     * @throws Exception Exception
     */
    public List<ObjectContent> getDatastorePropertiesOnDatacenter(String[] propertyPaths) throws Exception {
        PropertySpec propertySpec = new PropertySpec();
        propertySpec.setType("Datastore");
        propertySpec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec dc2DatastoreTraversal = new TraversalSpec();
        dc2DatastoreTraversal.setType(DATACENTER_TYPE);
        dc2DatastoreTraversal.setPath("datastore");
        dc2DatastoreTraversal.setName("dc2DatastoreTraversal");

        ObjectSpec objectSpec = new ObjectSpec();
        objectSpec.setObj(mor);
        objectSpec.setSkip(Boolean.TRUE);
        objectSpec.getSelectSet().add(dc2DatastoreTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(propertySpec);
        pfSpec.getObjectSet().add(objectSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        return context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);
    }

    /**
     * getOwnerDatacenter
     *
     * @param context context
     * @param morEntity morEntity
     * @return Pair
     * @throws Exception Exception
     */
    public static Pair<DatacenterMO, String> getOwnerDatacenter(VmwareContext context, ManagedObjectReference morEntity)
        throws Exception {
        PropertySpec propertySpec = new PropertySpec();
        propertySpec.setType(DATACENTER_TYPE);
        propertySpec.getPathSet().add(NAME);

        TraversalSpec entityParentTraversal = new TraversalSpec();
        entityParentTraversal.setType("ManagedEntity");
        entityParentTraversal.setPath("parent");
        entityParentTraversal.setName("entityParentTraversal");
        SelectionSpec selSpec = new SelectionSpec();
        selSpec.setName("entityParentTraversal");
        entityParentTraversal.getSelectSet().add(selSpec);

        ObjectSpec objectSpec = new ObjectSpec();
        objectSpec.setObj(morEntity);
        objectSpec.setSkip(Boolean.TRUE);
        objectSpec.getSelectSet().add(entityParentTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(propertySpec);
        pfSpec.getObjectSet().add(objectSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> ocs = context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        assert ocs != null && ocs.size() > 0;
        assert ocs.get(0).getObj() != null;
        assert ocs.get(0).getPropSet().get(0) != null;
        assert ocs.get(0).getPropSet().get(0).getVal() != null;

        String dcName = ocs.get(0).getPropSet().get(0).getVal().toString();
        return new Pair<>(new DatacenterMO(context, ocs.get(0).getObj()), dcName);
    }
}
