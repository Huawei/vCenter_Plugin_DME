package com.huawei.vmware.mo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldStringValue;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName HypervisorHostHelperTest.java
 * @Description TODO
 * @createTime 2020年12月02日 19:31:00
 */
public class HypervisorHostHelperTest {
    @Mock
    private VmwareContext context;

    @Test
    public void findVmFromObjectContent() {
        ObjectContent objectContent = mock(ObjectContent.class);
        ObjectContent[] ocs = {objectContent};
        String name = "12";
        String instanceNameCustomField = "13";
        List<DynamicProperty> dynamicPropertyList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertyList.add(dynamicProperty);
        when(objectContent.getPropSet()).thenReturn(dynamicPropertyList);
        when(dynamicProperty.getName()).thenReturn("name");
        when(dynamicProperty.getVal()).thenReturn(name);
        when(objectContent.getObj()).thenReturn(mock(ManagedObjectReference.class));
        HypervisorHostHelper.findVmFromObjectContent(context, ocs, name, instanceNameCustomField);
    }

    @Test
    public void findVmFromObjectContent1() {
        ObjectContent objectContent = mock(ObjectContent.class);
        ObjectContent[] ocs = {objectContent};
        String name = "12";
        String instanceNameCustomField = "13";
        List<DynamicProperty> dynamicPropertyList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertyList.add(dynamicProperty);
        when(objectContent.getPropSet()).thenReturn(dynamicPropertyList);
        when(dynamicProperty.getName()).thenReturn("1324");
        CustomFieldStringValue customFieldStringValue = mock(CustomFieldStringValue.class);
        when(dynamicProperty.getVal()).thenReturn(customFieldStringValue);
        when(objectContent.getObj()).thenReturn(mock(ManagedObjectReference.class));
        when(customFieldStringValue.getValue()).thenReturn(name);

        HypervisorHostHelper.findVmFromObjectContent(context, ocs, name, instanceNameCustomField);
    }
}