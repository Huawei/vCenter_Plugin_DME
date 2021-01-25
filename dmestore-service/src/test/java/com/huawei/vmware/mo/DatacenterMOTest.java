package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.VimPortType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName DatacenterMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 10:51:00
 */
public class DatacenterMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    @InjectMocks
    private DatacenterMO datacenterMo;

    private VmwareClient vmwareClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);
    }

    @Test
    public void getName() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("name"))).thenReturn("11");
        datacenterMo.getName();
    }

    @Test
    public void findDatastore() throws Exception {
        String name = "123";
        List<ObjectContent> objectContentList = new ArrayList<>();
        ObjectContent objectContent = mock(ObjectContent.class);
        objectContentList.add(objectContent);
        VimPortType service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(objectContentList);
        List<DynamicProperty> dynamicPropertieList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertieList.add(dynamicProperty);
        when(objectContent.getPropSet()).thenReturn(dynamicPropertieList);
        when(dynamicProperty.getVal()).thenReturn(name);
        when(objectContent.getObj()).thenReturn(mock(ManagedObjectReference.class));
        datacenterMo.findDatastore(name);
    }

    @Test
    public void getDatastorePropertiesOnDatacenter() throws Exception {
        String[] propertyPaths = {"22"};
        List<ObjectContent> objectContentList = new ArrayList<>();
        ObjectContent objectContent = mock(ObjectContent.class);
        objectContentList.add(objectContent);
        VimPortType service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(objectContentList);
        datacenterMo.getDatastorePropertiesOnDatacenter(propertyPaths);
    }

    @Test
    public void getOwnerDatacenter() throws Exception {
        List<ObjectContent> objectContentList = new ArrayList<>();
        ObjectContent objectContent = mock(ObjectContent.class);
        objectContentList.add(objectContent);
        VimPortType service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(objectContentList);
        when(objectContent.getObj()).thenReturn(mock(ManagedObjectReference.class));
        List<DynamicProperty> dynamicPropertieList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertieList.add(dynamicProperty);
        when(objectContent.getPropSet()).thenReturn(dynamicPropertieList);
        when(dynamicProperty.getVal()).thenReturn("cc");

        datacenterMo.getOwnerDatacenter(context, mor);
    }
}