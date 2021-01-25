package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.PrivilegePolicyDef;
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
 * @since  2020年12月02日 11:44:00
 */
public class CustomFieldsManagerMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private CustomFieldsManagerMO customFieldsManagerMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void addCustomerFieldDef() throws Exception {
        String fieldName = "test";
        String morType = "12";
        PrivilegePolicyDef fieldDefPolicy = mock(PrivilegePolicyDef.class);
        PrivilegePolicyDef fieldPolicy = mock(PrivilegePolicyDef.class);
        when(service.addCustomFieldDef(mor, fieldName, morType, fieldDefPolicy,
            fieldPolicy)).thenReturn(mock(CustomFieldDef.class));

        customFieldsManagerMO.addCustomerFieldDef(fieldName, morType, fieldDefPolicy, fieldPolicy);
    }

    @Test
    public void setField() throws Exception {
        ManagedObjectReference morEntity = mock(ManagedObjectReference.class);
        int key = 10;
        String value = "111";
        doNothing().when(service).setField(mor, morEntity, key, value);
        customFieldsManagerMO.setField(morEntity, key, value);
    }

    @Test
    public void getFields() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("field"))).thenReturn(mock(List.class));
        customFieldsManagerMO.getFields();
    }

    @Test
    public void getCustomFieldKey() throws Exception {
        String morType = "123";
        String fieldName = "22";
        List<CustomFieldDef> list = new ArrayList<>();
        CustomFieldDef customFieldDef = mock(CustomFieldDef.class);
        list.add(customFieldDef);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("field"))).thenReturn(list);
        when(customFieldDef.getName()).thenReturn(fieldName);
        when(customFieldDef.getManagedObjectType()).thenReturn(morType);
        when(customFieldDef.getKey()).thenReturn(10);
        customFieldsManagerMO.getCustomFieldKey(morType, fieldName);
    }
}