package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className StorageDetailTest
 * @description TODO
 * @date 2020/12/3 11:29
 */
public class StorageDetailTest {

    @InjectMocks
    StorageDetail storageDetail = new StorageDetail();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        StorageDetail storageDetail = new StorageDetail();
        storageDetail.setPatchVersion("321");
        storageDetail.setMaintenanceStart("321");
        storageDetail.setMaintenanceOvertime("321");
        storageDetail.setWarning("321");
        storageDetail.setEvent("321");
        storageDetail.setStoragePool("321");
        storageDetail.setVolume("321");
        storageDetail.setFileSystem("321");
        storageDetail.setdTrees("321");
        storageDetail.setNfsShares("321");
        storageDetail.setBandPorts("321");
        storageDetail.setLogicPorts("321");
        storageDetail.setStorageControllers("321");
        storageDetail.setStorageDisks("321");
        storageDetail.setId("321");
        storageDetail.setName("321");
        storageDetail.setIp("321");
        storageDetail.setStatus("321");
        storageDetail.setSynStatus("321");
        storageDetail.setSn("321");
        storageDetail.setVendor("321");
        storageDetail.setModel("321");
        storageDetail.setProductVersion("321");
        storageDetail.setLocation("321");
        String[] azIds = {"21"};
        storageDetail.setAzIds(azIds);
        storageDetail.setUsedCapacity(21.0);
        storageDetail.setTotalCapacity(21.0);
        storageDetail.setTotalEffectiveCapacity(21.0);
        storageDetail.setFreeEffectiveCapacity(21.0);
        storageDetail.setSubscriptionCapacity(21.0);
        storageDetail.setProtectionCapacity(21.0);
        storageDetail.setFileCapacity(21.0);
        storageDetail.setBlockCapacity(21.0);
        storageDetail.setDedupedCapacity(21.0);
        storageDetail.setCompressedCapacity(21.0);
        storageDetail.setOptimizeCapacity(21.0);

        storageDetail.getPatchVersion();
        storageDetail.getMaintenanceStart();
        storageDetail.getMaintenanceOvertime();
        storageDetail.getWarning();
        storageDetail.getEvent();
        storageDetail.getStoragePool();
        storageDetail.getVolume();
        storageDetail.getFileSystem();
        storageDetail.getdTrees();
        storageDetail.getNfsShares();
        storageDetail.getBandPorts();
        storageDetail.getLogicPorts();
        storageDetail.getStorageControllers();
        storageDetail.getStorageDisks();
        storageDetail.getId();
        storageDetail.getName();
        storageDetail.getIp();
        storageDetail.getStatus();
        storageDetail.getSynStatus();
        storageDetail.getSn();
        storageDetail.getVendor();
        storageDetail.getModel();
        storageDetail.getProductVersion();
        storageDetail.getLocation();
        storageDetail.getAzIds();
        storageDetail.getUsedCapacity();
        storageDetail.getTotalCapacity();
        storageDetail.getTotalEffectiveCapacity();
        storageDetail.getFreeEffectiveCapacity();
        storageDetail.getSubscriptionCapacity();
        storageDetail.getProtectionCapacity();
        storageDetail.getFileCapacity();
        storageDetail.getBlockCapacity();
        storageDetail.getDedupedCapacity();
        storageDetail.getCompressedCapacity();
        storageDetail.getOptimizeCapacity();


    }

    @Test
    public void testToString() {
        storageDetail.toString();
    }
}