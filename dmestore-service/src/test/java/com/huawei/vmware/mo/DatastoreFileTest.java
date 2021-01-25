package com.huawei.vmware.mo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName DatastoreFileTest.java
 * @Description TODO
 * @createTime 2020年12月03日 15:41:00
 */
public class DatastoreFileTest {
    @InjectMocks
    private DatastoreFile datastoreFile;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        String path = "[12321]";
        datastoreFile.setPath(path);
    }

    @Test
    public void getDatastoreName() {
        String path = "[12321]";
        datastoreFile.setPath(path);
        datastoreFile.getDatastoreName();
    }

    @Test
    public void getPath() {
        datastoreFile.getPath();
    }

    @Test
    public void getRelativePath() {
        datastoreFile.getRelativePath();
    }

    @Test
    public void getFileName() {
        datastoreFile.getFileName();
    }

    @Test
    public void isFullDatastorePath() {
        String path = "[131]123";
        DatastoreFile.isFullDatastorePath(path);
    }

    @Test
    public void getDatastoreNameFromPath() {
        String path = "[131]123";
        DatastoreFile.getDatastoreNameFromPath(path);
    }

    @Test
    public void getCompanionDatastorePath() {
        String path = "[4546]D:\\\\aa/";
        String companionFileName = "123";
        DatastoreFile.getCompanionDatastorePath(path, companionFileName);
    }
}