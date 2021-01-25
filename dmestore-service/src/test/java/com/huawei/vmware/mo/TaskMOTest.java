package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.MethodFault;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VimPortType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName TaskMOTest.java
 * @Description TODO
 * @createTime 2020年12月03日 09:43:00
 */
public class TaskMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private TaskMO taskMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void getTaskFailureInfo() throws Exception {
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("info"))).thenReturn(taskInfo);
        LocalizedMethodFault fault = mock(LocalizedMethodFault.class);
        when(taskInfo.getError()).thenReturn(fault);
        when(fault.getLocalizedMessage()).thenReturn("adf");
        MethodFault methodFault = mock(MethodFault.class);
        when(fault.getFault()).thenReturn(methodFault);
        TaskMO.getTaskFailureInfo(context, mor);
    }
}