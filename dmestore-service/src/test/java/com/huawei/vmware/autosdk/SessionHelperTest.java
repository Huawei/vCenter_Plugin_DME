package com.huawei.vmware.autosdk;

import com.huawei.vmware.autosdk.auth.VapiAuthenticationHelper;
import com.vmware.vapi.bindings.StubConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className SessionHelperTest
 * @description TODO
 * @date 2020/12/3 14:31
 */
public class SessionHelperTest {

    protected VapiAuthenticationHelper vapiAuthHelper;
    protected StubConfiguration sessionStubConfig;

    @InjectMocks
    SessionHelper sessionHelper = new SessionHelper();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void login() throws Exception {
        sessionHelper.login("321", "321", "321","321");
    }

    @Test
    public void logout() {
        sessionHelper.logout();
    }

    @Test
    public void fil() {
    }
}