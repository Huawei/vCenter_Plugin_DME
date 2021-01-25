package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VmfsDatastoreVolumeDetailTest
 * @description TODO
 * @date 2020/12/3 14:13
 */
public class VmfsDatastoreVolumeDetailTest {

    @InjectMocks
    VmfsDatastoreVolumeDetail vmfsDatastoreVolumeDetail = new VmfsDatastoreVolumeDetail();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        VmfsDatastoreVolumeDetail vmfsDatastoreVolumeDetail = new VmfsDatastoreVolumeDetail();
        vmfsDatastoreVolumeDetail.setCompression(true);
        vmfsDatastoreVolumeDetail.setProvisionType("321");
        vmfsDatastoreVolumeDetail.setName("321");
        vmfsDatastoreVolumeDetail.setWwn("32");
        vmfsDatastoreVolumeDetail.setSmartTier("321");
        vmfsDatastoreVolumeDetail.setEvolutionaryInfo("321");
        vmfsDatastoreVolumeDetail.setDevice("321");
        vmfsDatastoreVolumeDetail.setStoragePool("321");
        vmfsDatastoreVolumeDetail.setServiceLevel("321");
        vmfsDatastoreVolumeDetail.setStorage("321");
        vmfsDatastoreVolumeDetail.setDudeplication(true);
        vmfsDatastoreVolumeDetail.setApplicationType("321");
        vmfsDatastoreVolumeDetail.setControlPolicy("321");
        vmfsDatastoreVolumeDetail.setTrafficControl("321");

        vmfsDatastoreVolumeDetail.getCompression();
        vmfsDatastoreVolumeDetail.getProvisionType();
        vmfsDatastoreVolumeDetail.getName();
        vmfsDatastoreVolumeDetail.getWwn();
        vmfsDatastoreVolumeDetail.getSmartTier();
        vmfsDatastoreVolumeDetail.getEvolutionaryInfo();
        vmfsDatastoreVolumeDetail.getDevice();
        vmfsDatastoreVolumeDetail.getStoragePool();
        vmfsDatastoreVolumeDetail.getServiceLevel();
        vmfsDatastoreVolumeDetail.getStorage();
        vmfsDatastoreVolumeDetail.getDudeplication();
        vmfsDatastoreVolumeDetail.isDudeplication();
        vmfsDatastoreVolumeDetail.isCompression();
        vmfsDatastoreVolumeDetail.getApplicationType();
        vmfsDatastoreVolumeDetail.getControlPolicy();
        vmfsDatastoreVolumeDetail.getTrafficControl();


    }

    @Test
    public void testToString() {
        vmfsDatastoreVolumeDetail.toString();
    }
}