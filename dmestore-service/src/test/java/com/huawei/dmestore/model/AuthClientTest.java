package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className AuthClientTest
 * @description TODO
 * @date 2020/12/3 10:40
 */
public class AuthClientTest {

    @InjectMocks
    AuthClient authClient = new AuthClient();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        AuthClient authClient = new AuthClient();
        authClient.setAccessval("321");
        authClient.setId("321");
        authClient.setName("321");
        authClient.setSecure("321");
        authClient.setSync("321");
        authClient.setType("321");
        authClient.getAccessval();
        authClient.getId();
        authClient.getName();
        authClient.getSecure();
        authClient.getSync();
        authClient.getType();
    }

    @Test
    public void testToString() {
        authClient.toString();
    }
}