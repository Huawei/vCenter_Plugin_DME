package com.huawei.vmware.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.huawei.vmware.mo.DatacenterMO;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.TaskInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VmwareContextTest
 * @description TODO
 * @date 2020/12/2 16:57
 */
public class VmwareContextTest {

    @InjectMocks
    VmwareContext vmwareContext = new VmwareContext();

    @Mock
    private DatacenterVmwareMoFactory datacenterVmwareMoFactory = DatacenterVmwareMoFactory.getInstance();
    @Mock
    VmwareClient vimClient;
    ManagedObjectReference managedObjectReference;
    DatacenterMO datacenterMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        managedObjectReference = spy(ManagedObjectReference.class);
        managedObjectReference.setValue("321");
        datacenterMO = mock(DatacenterMO.class);
        vimClient = mock(VmwareClient.class);
    }

    @Test
    public void clearStockObjects() {
        vmwareContext.clearStockObjects();
    }

    @Test
    public void getServerAddress() {
        vmwareContext.getServerAddress();
    }

    @Test
    public void getService() {
        vmwareContext.getService();
    }

    @Test
    public void getPbmService() {
        vmwareContext.getPbmService();
    }

    @Test
    public void getServiceContent() {
        vmwareContext.getServiceContent();
    }

    @Test
    public void getPbmServiceContent() {
        vmwareContext.getPbmServiceContent();
    }

    @Test
    public void getPropertyCollector() {
        vmwareContext.getPropertyCollector();
    }

    @Test
    public void getRootFolder() {
        vmwareContext.getRootFolder();
    }

    @Test
    public void getVimClient() {
        vmwareContext.getVimClient();
    }

    @Test
    public void setPoolInfo() {
        VmwareContextPool pool = spy(VmwareContextPool.class);
        vmwareContext.setPoolInfo(pool, "321");
    }

    @Test
    public void getPool() {
        vmwareContext.getPool();
    }

    @Test
    public void getPoolKey() {
        vmwareContext.getPoolKey();
    }

    @Test
    public void idleCheck() throws Exception {
        vmwareContext.idleCheck();
    }

    @Test
    public void getOutstandingContextCount() {
        vmwareContext.getOutstandingContextCount();
    }

    @Test
    public void registerOutstandingContext() {
        vmwareContext.registerOutstandingContext();
    }

    @Test
    public void unregisterOutstandingContext() {
        vmwareContext.unregisterOutstandingContext();
    }

    @Test
    public void getDatastoreMorByPath() throws Exception {
        when(datacenterVmwareMoFactory.build(vmwareContext, "321")).thenReturn(datacenterMO);
        when(datacenterMO.getMor()).thenReturn(managedObjectReference);
        vmwareContext.getDatastoreMorByPath("/321/456");


    }

    @Test
    public void waitForTaskProgressDone() throws Exception {
        TaskInfo tinfo = spy(TaskInfo.class);
        when(vimClient.getDynamicProperty(managedObjectReference,"info")).thenReturn(tinfo);
        when(tinfo.getProgress()).thenReturn(101);
        vmwareContext.waitForTaskProgressDone(managedObjectReference);

    }

    @Test
    public void getResourceContent() throws Exception {
        when(vimClient.getServiceCookie()).thenReturn("321");
        vmwareContext.getResourceContent("https://10.143.132.248/websso/SAML2/SSO/vsphere.local");
    }

    @Test
    public void uploadResourceContent() {
    }

    @Test
    public void listDatastoreDirContent() {
    }

    @Test
    public void composeDatastoreBrowseUrl() {
    }

    @Test
    public void testComposeDatastoreBrowseUrl() {
    }

    @Test
    public void getHttpConnection() {
    }

    @Test
    public void testGetHttpConnection() {
    }

    @Test
    public void close() {
        vmwareContext.close();

    }

    @Test
    public void inFolderByType() {
    }

    @Test
    public void testInFolderByType() {
    }

    @Test
    public void inContainerByType() {
    }

    @Test
    public void toList() {
    }

    @Test
    public void containerViewByType() {
    }

    @Test
    public void testContainerViewByType() {
    }

    @Test
    public void testContainerViewByType1() {
    }

    @Test
    public void propertyFilterSpecs() {
    }

    @Test
    public void populate() {
    }
}