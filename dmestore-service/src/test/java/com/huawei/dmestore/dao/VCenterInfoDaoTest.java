package com.huawei.dmestore.dao;

import com.huawei.dmestore.DMEServiceApplication;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeSqlException;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lianq
 * @className VCenterInfoDaoTest
 * @description TODO
 * @date 2020/12/2 20:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DMEServiceApplication.class)
public class VCenterInfoDaoTest {

    @Autowired
    private VCenterInfoDao vCenterInfoDao;

    @Test
    public void getDbFileName() {
        vCenterInfoDao.getDbFileName();

    }

    @Test
    public void addVcenterInfo() throws DmeSqlException {
        VCenterInfo vCenterInfo = new VCenterInfo();
        vCenterInfo.setPushEventLevel(321);
        vCenterInfo.setId(321);
        vCenterInfo.setHostIp("321");
        vCenterInfo.setUserName("321");
        vCenterInfo.setPassword("321");
        vCenterInfo.setCreateTime(new Date());
        vCenterInfo.setState(true);
        vCenterInfo.setPushEvent(true);
        vCenterInfo.setHostPort(321);
        vCenterInfoDao.addVcenterInfo(vCenterInfo);
    }

    @Test
    public void updateVcenterInfo() throws DmeSqlException {
        VCenterInfo vCenterInfo = new VCenterInfo();
        vCenterInfo.setPushEventLevel(321);
        vCenterInfo.setId(321);
        vCenterInfo.setHostIp("321");
        vCenterInfo.setUserName("321");
        vCenterInfo.setPassword("321");
        vCenterInfo.setCreateTime(new Date());
        vCenterInfo.setState(true);
        vCenterInfo.setPushEvent(true);
        vCenterInfo.setHostPort(321);
        vCenterInfoDao.updateVcenterInfo(vCenterInfo);
    }

    @Test
    public void getVcenterInfo() throws DmeSqlException {
        System.out.println(vCenterInfoDao.getVcenterInfo());
    }
}