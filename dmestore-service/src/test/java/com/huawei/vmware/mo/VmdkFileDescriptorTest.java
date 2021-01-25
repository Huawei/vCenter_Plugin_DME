package com.huawei.vmware.mo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName VmdkFileDescriptorTest.java
 * @Description TODO
 * @createTime 2020年12月03日 09:56:00
 */
public class VmdkFileDescriptorTest {
    @InjectMocks
    private VmdkFileDescriptor vmdkFileDescriptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void parse() throws IOException {
        String str = "aa=\"123";
        vmdkFileDescriptor.parse(str.getBytes());

        String str2 = "RW\"test01\"ppp";
        vmdkFileDescriptor.parse(str2.getBytes());
    }

    @Test
    public void getBaseFileName() {
        vmdkFileDescriptor.getBaseFileName();
    }

    @Test
    public void getParentFileName() {
        vmdkFileDescriptor.getProperties().put("parentFileNameHint", "222");
        vmdkFileDescriptor.getParentFileName();
    }

    @Test
    public void getProperties() {
        vmdkFileDescriptor.getProperties();
    }
}