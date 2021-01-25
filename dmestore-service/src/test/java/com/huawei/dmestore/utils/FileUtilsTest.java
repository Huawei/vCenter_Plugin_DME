package com.huawei.dmestore.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className FileUtilsTest
 * @description TODO
 * @date 2020/12/1 19:40
 */
public class FileUtilsTest {

    @InjectMocks
    FileUtils fileUtils = new FileUtils();
    File file;
    UserPrincipal user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        file = new File("321");
        user = spy(UserPrincipal.class);
    }

    @Test
    public void setFilePermission() throws IOException {
        fileUtils.setFilePermission(file);
    }

    @Test
    public void setWindowsFilePermission() throws IOException {
        fileUtils.setWindowsFilePermission(file);
    }

    @Test
    public void setLinuxFilePermission() throws IOException {
        fileUtils.setLinuxFilePermission(file);
    }

    @Test
    public void isWindows() {
        fileUtils.isWindows();
    }

    @Test
    public void getPath() {
        fileUtils.getPath();
    }

    @Test
    public void testGetPath() {
        fileUtils.getPath(false);
    }

    @Test
    @Ignore
    public void createDir() {
        fileUtils.createDir("321");
    }

    @Test
    public void getOldDbFolder() {
        fileUtils.getOldDbFolder();
    }

    @Test
    public void getOldFolder() {
        fileUtils.getOldFolder();
    }
}