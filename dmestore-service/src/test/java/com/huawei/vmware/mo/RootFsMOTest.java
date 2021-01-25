package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.ManagedObjectReference;
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
 * @ClassName RootFsMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 19:57:00
 */
public class RootFsMOTest {
    @Mock
    private VmwareContext context;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private RootFsMO rootFsMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void getAllHostOnRootFs() throws Exception {
        List<Pair<ManagedObjectReference, String>> list = new ArrayList<>();
        when(context.inFolderByType(anyObject(),eq("HostSystem"))).thenReturn(list);
        rootFsMO.getAllHostOnRootFs();
    }

    @Test
    public void getAllClusterOnRootFs() throws Exception {
        List<Pair<ManagedObjectReference, String>> list = new ArrayList<>();
        when(context.inFolderByType(anyObject(),eq("ClusterComputeResource"))).thenReturn(list);
        rootFsMO.getAllClusterOnRootFs();
    }

    @Test
    public void getAllDatastoreOnRootFs() throws Exception {
        List<Pair<ManagedObjectReference, String>> list = new ArrayList<>();
        when(context.inFolderByType(anyObject(),eq("Datastore"))).thenReturn(list);
        rootFsMO.getAllDatastoreOnRootFs();
    }

    @Test
    public void findHostById() throws Exception {
        String id = "1313";
        List<Pair<ManagedObjectReference, String>> list = new ArrayList<>();
        Pair<ManagedObjectReference, String> pair = mock(Pair.class);
        list.add(pair);
        when(context.inFolderByType(anyObject(),eq("HostSystem"))).thenReturn(list);
        ManagedObjectReference first = mock(ManagedObjectReference.class);
        when(pair.first()).thenReturn(first);
        rootFsMO.findHostById(id);
    }

    @Test
    public void findClusterById() throws Exception{
        String id = "1313";
        List<Pair<ManagedObjectReference, String>> list = new ArrayList<>();
        Pair<ManagedObjectReference, String> pair = mock(Pair.class);
        list.add(pair);
        when(context.inFolderByType(anyObject(),eq("ClusterComputeResource"))).thenReturn(list);
        ManagedObjectReference first = mock(ManagedObjectReference.class);
        when(pair.first()).thenReturn(first);
        rootFsMO.findClusterById(id);
    }
}