package com.huawei.vmware.util;

import static org.mockito.Mockito.spy;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vise.usersession.ServerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VmwareClientTest
 * @description TODO
 * @date 2020/12/3 14:51
 */
public class VmwareClientTest {

    @InjectMocks
    VmwareClient vmwareClient = new VmwareClient("321");

    ManagedObjectReference mor;
    ServerInfo serverInfo;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mor = spy(ManagedObjectReference.class);
        serverInfo = spy(ServerInfo.class);

    }

    @Test
    public void connect() throws Exception {
        vmwareClient.connect(serverInfo);

    }
    @Test
    public void connect1() throws Exception {
        vmwareClient.connect("321","321","321");

    }

    @Test
    public void getService() {
        vmwareClient.getService();

    }

    @Test
    public void getPbmService() {
        vmwareClient.getPbmService();

    }

    @Test
    public void getServiceContent() {
        vmwareClient.getServiceContent();

    }

    @Test
    public void getPbmServiceContent() {
        vmwareClient.getPbmServiceContent();

    }

    @Test
    public void getServiceCookie() {
        vmwareClient.getServiceCookie();

    }

    @Test
    public void getPropCol() {
        vmwareClient.getPropCol();

    }

    @Test
    public void getRootFolder() {
        vmwareClient.getRootFolder();

    }

    @Test
    public void getDynamicProperty() throws Exception {
        vmwareClient.getDynamicProperty(mor,"321");

    }

    @Test
    public void waitForTask() throws Exception {
        vmwareClient.waitForTask(mor);

    }

    @Test
    public void getDecendentMoRef() throws Exception {
        vmwareClient.getDecendentMoRef(mor, "321", "321");

    }

    @Test
    public void getMoRefProp() throws Exception {
        vmwareClient.getMoRefProp(mor, "321");

    }

    @Test
    public void setVcenterSessionTimeout() {
        vmwareClient.setVcenterSessionTimeout(1000);
    }

    @Test
    public void getVcenterSessionTimeout() {
        vmwareClient.getVcenterSessionTimeout();

    }
}