package com.huawei.vmware.autosdk.auth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.vmware.cis.Session;
import com.vmware.vapi.bindings.StubFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VapiAuthenticationHelperTest
 * @description TODO
 * @date 2020/12/3 14:21
 */
public class VapiAuthenticationHelperTest {

    @InjectMocks
    VapiAuthenticationHelper vapiAuthenticationHelper = new VapiAuthenticationHelper();

    private Session sessionSvc;
    private StubFactory stubFactory;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sessionSvc = mock(Session.class);
    }

    @Test
    public void loginByUsernameAndPassword() throws Exception {
        vapiAuthenticationHelper.loginByUsernameAndPassword("321", "321", "321", "321", null);
    }

    @Test
    public void logout() {
        vapiAuthenticationHelper.logout();
    }

    @Test
    public void createApiStubFactory() throws Exception {
        vapiAuthenticationHelper.createApiStubFactory("321", "321", null);
    }

    @Test
    public void getStubFactory() {
        vapiAuthenticationHelper.getStubFactory();
    }

    @Test
    public void buildHttpConfiguration() throws Exception {
        vapiAuthenticationHelper.buildHttpConfiguration(true);
    }
}