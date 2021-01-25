package com.huawei.vmware.util;

import static org.mockito.Mockito.spy;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vise.usersession.UserSessionService;
import com.vmware.vise.vim.data.VimObjectReferenceService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VmwarePluginContextFactoryTest
 * @description TODO
 * @date 2020/12/3 15:04
 */
public class VmwarePluginContextFactoryTest {

    @InjectMocks
    VmwarePluginContextFactory vmwarePluginContextFactory;

    UserSessionService userSessionService;
    VimObjectReferenceService vimObjectReferenceService;
    ManagedObjectReference mor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userSessionService = spy(UserSessionService.class);
        vimObjectReferenceService =spy(VimObjectReferenceService.class);
        mor = spy(ManagedObjectReference.class);
    }

    @Test
    public void getServerContext() throws Exception {
        vmwarePluginContextFactory.getServerContext(userSessionService, vimObjectReferenceService,
            "321");
    }

    @Test
    public void getAllContext() throws Exception {
        vmwarePluginContextFactory.getAllContext(userSessionService, vimObjectReferenceService);
    }

    @Test
    public void closeAll() {
        vmwarePluginContextFactory.closeAll();
    }
}