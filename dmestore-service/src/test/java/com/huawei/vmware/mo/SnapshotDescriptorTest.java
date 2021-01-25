package com.huawei.vmware.mo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName SnapshotDescriptorTest.java
 * @Description TODO
 * @createTime 2020年12月02日 20:20:00
 */
public class SnapshotDescriptorTest {
    @InjectMocks
    private SnapshotDescriptor snapshotDescriptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void parse() throws IOException {
        String test = "dd=145";
        snapshotDescriptor.parse(test.getBytes());
    }

    @Test
    public void removeDiskReferenceFromSnapshot() {
        String diskFileName = "11";
        snapshotDescriptor.getProperties().put("snapshot.numSnapshots", "1");
        snapshotDescriptor.getProperties().put("snapshot0.numDisks", "1");
        snapshotDescriptor.getProperties().put("snapshot0.disk0.fileName", diskFileName);
        snapshotDescriptor.removeDiskReferenceFromSnapshot(diskFileName);
    }

    @Test
    public void getVmsdContent() {
        snapshotDescriptor.getProperties().put("snapshot.lastUID", "1");
        snapshotDescriptor.getProperties().put("snapshot.current", "2");
        snapshotDescriptor.getProperties().put("snapshot.numSnapshots", "1");
        snapshotDescriptor.getProperties().put("snapshot0.numDisks", "1");

        snapshotDescriptor.getVmsdContent();
    }

    @Test
    public void getCurrentDiskChain() {
        snapshotDescriptor.getProperties().put("snapshot.current", "1");
        snapshotDescriptor.getProperties().put("snapshot.numSnapshots", "1");
        snapshotDescriptor.getProperties().put("snapshot0.uid", "1");
        snapshotDescriptor.getProperties().put("snapshot0.numDisks", "1");
        snapshotDescriptor.getCurrentDiskChain();
    }
}