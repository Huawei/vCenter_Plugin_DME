package com.huawei.vmware;

import static org.mockito.Mockito.spy;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vise.usersession.UserSessionService;
import com.vmware.vise.vim.data.VimObjectReferenceService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lianq
 * @className VCConnectionHelperTest
 * @description TODO
 * @date 2020/12/3 15:29
 */
public class VcConnectionHelpersTest {

    @Autowired
    VcConnectionHelpers vcConnectionHelpers;

    UserSessionService userSessionService;
    VimObjectReferenceService vimObjectReferenceService;
    ManagedObjectReference mor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userSessionService = spy(UserSessionService.class);
        vimObjectReferenceService = spy(VimObjectReferenceService.class);
        mor = spy(ManagedObjectReference.class);
    }

    @Test
    public void getUserSessionService() {
        vcConnectionHelpers.getUserSessionService();
    }

    @Test
    public void setUserSessionService() {
        vcConnectionHelpers.setUserSessionService(userSessionService);

    }

    @Test
    public void getVimObjectReferenceService() {
        vcConnectionHelpers.getVimObjectReferenceService();

    }

    @Test
    public void setVimObjectReferenceService() {
        vcConnectionHelpers.setVimObjectReferenceService(vimObjectReferenceService);

    }

    @Test
    public void getServerurl() {
        vcConnectionHelpers.getServerurl();

    }

    @Test
    public void setServerurl() {
        vcConnectionHelpers.setServerurl("321");

    }

    @Test
    public void getUsername() {
        vcConnectionHelpers.getUsername();

    }

    @Test
    public void setUsername() {
        vcConnectionHelpers.setUsername("321");

    }

    @Test
    public void getPassword() {
        vcConnectionHelpers.getPassword();

    }

    @Test
    public void setPassword() {
        vcConnectionHelpers.setPassword("321");

    }

    @Test
    public void getServerport() {
        vcConnectionHelpers.getServerport();

    }

    @Test
    public void setServerport() {
        vcConnectionHelpers.setServerport(321);

    }

    @Test
    public void getServerContext() throws Exception {
        vcConnectionHelpers.getServerContext("321:321321");

    }

    @Test
    public void objectId2Mor() {
        vcConnectionHelpers.objectId2Mor("321:321:321:321");

    }

    @Test
    public void objectId2Serverguid() {
        vcConnectionHelpers.objectId2Serverguid("321:321:321:321");

    }

    @Test
    public void mor2ObjectId() {
        vcConnectionHelpers.mor2ObjectId(mor, "321:213:321:321");

    }
}