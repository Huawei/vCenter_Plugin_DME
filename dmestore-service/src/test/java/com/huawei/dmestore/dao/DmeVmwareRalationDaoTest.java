package com.huawei.dmestore.dao;

import com.huawei.dmestore.DMEServiceApplication;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeSqlException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lianq
 * @className DmeVmwareRalationDaoTest
 * @description TODO
 * @date 2020/12/2 21:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DMEServiceApplication.class)
public class DmeVmwareRalationDaoTest {

    @Autowired
    private  DmeVmwareRalationDao dmeVmwareRalationDao;
    List<DmeVmwareRelation> list = new ArrayList<>();
    DmeVmwareRelation dmeVmwareRelation;

    @Before
    public void setUp() throws Exception {
        dmeVmwareRelation = new DmeVmwareRelation();
        dmeVmwareRelation.setId(321);
        dmeVmwareRelation.setStoreId("321");
        dmeVmwareRelation.setStoreName("321");
        dmeVmwareRelation.setVolumeId("321");
        dmeVmwareRelation.setVolumeName("321");
        dmeVmwareRelation.setVolumeWwn("321");
        dmeVmwareRelation.setVolumeShare("321");
        dmeVmwareRelation.setVolumeFs("321");
        dmeVmwareRelation.setShareId("321");
        dmeVmwareRelation.setShareName("321");
        dmeVmwareRelation.setFsId("321");
        dmeVmwareRelation.setFsName("321");
        dmeVmwareRelation.setLogicPortId("321");
        dmeVmwareRelation.setLogicPortName("321");
        dmeVmwareRelation.setStoreType("NFS");
        dmeVmwareRelation.setCreateTime(new Date());
        dmeVmwareRelation.setUpdateTime(new Date());
        dmeVmwareRelation.setState(1);
        dmeVmwareRelation.setStorageDeviceId("321");
        list.add(dmeVmwareRelation);
    }

    @Test
    public void getDbFileName() {
        dmeVmwareRalationDao.getDbFileName();
    }

    @Test
    public void setUrl() {
        dmeVmwareRalationDao.setUrl("321");
    }

    @Test
    public void getDmeVmwareRelation() throws DmeSqlException {
        dmeVmwareRalationDao.getDmeVmwareRelation("321");
    }

    @Test
    public void getDmeVmwareRelationByDsId() throws DmeSqlException {
        dmeVmwareRalationDao.getDmeVmwareRelationByDsId("321");
    }

    @Test
    public void getAllWwnByType() throws DmeSqlException {
        dmeVmwareRalationDao.getAllWwnByType("321");
    }

    @Test
    public void getAllStorageIdByType() throws DmeSqlException {
        dmeVmwareRalationDao.getAllStorageIdByType("321");
    }

    @Test
    public void update() {
        dmeVmwareRalationDao.update(list);

    }

    @Test
    public void testUpdate() {
        list.add(dmeVmwareRelation);
        dmeVmwareRalationDao.update(list, "VMFS");
    }

    @Test
    public void save() {
        dmeVmwareRalationDao.save(list);
    }

    @Test
    public void deleteByWwn() {
        List<String> list = new ArrayList<>();
        list.add("321");
        dmeVmwareRalationDao.deleteByWwn(list);
    }

    @Test
    public void deleteByStorageId() {
        List<String> list = new ArrayList<>();
        list.add("321");
        dmeVmwareRalationDao.deleteByStorageId(list);
    }

    @Test
    public void getFsIdByStorageId() {
        List<String> list = new ArrayList<>();
        list.add("321");
        dmeVmwareRalationDao.deleteByStorageId(list);
    }

    @Test
    public void getShareIdByStorageId() throws DmeSqlException {
        dmeVmwareRalationDao.getShareIdByStorageId("321");
    }

    @Test
    public void getLogicPortIdByStorageId() throws DmeSqlException {
        dmeVmwareRalationDao.getLogicPortIdByStorageId("321");
    }

    @Test
    public void getFsIdsByStorageId() throws DmeSqlException {
        dmeVmwareRalationDao.getFsIdsByStorageId("321");
    }

    @Test
    public void getShareIdsByStorageId() throws DmeSqlException {
        dmeVmwareRalationDao.getShareIdsByStorageId("321");
    }

    @Test
    public void getLogicPortIdsByStorageId() throws DmeSqlException {
        dmeVmwareRalationDao.getLogicPortIdsByStorageId("321");
    }

    @Test
    public void getVolumeIdsByStorageId() throws DmeSqlException {
        dmeVmwareRalationDao.getVolumeIdsByStorageId("321");
    }

    @Test
    public void getDmeVmwareRelationsByStorageIds() throws DmeSqlException {
        List<String> storageIds = new ArrayList<>();
        storageIds.add("321");
        storageIds.add("456");
        dmeVmwareRalationDao.getDmeVmwareRelationsByStorageIds(storageIds);
    }

    @Test
    public void concatValues() {
        Collection<String> values = new ArrayList<>();
        values.add("321");
        dmeVmwareRalationDao.concatValues(values);
    }

    @Test
    public void getVmfsNameByVolumeId() throws DmeSqlException {
        dmeVmwareRalationDao.getVmfsNameByVolumeId("321");
    }

    @Test
    public void getDataStoreByName() throws DmeSqlException {
        dmeVmwareRalationDao.getDataStoreByName("321");
    }
}