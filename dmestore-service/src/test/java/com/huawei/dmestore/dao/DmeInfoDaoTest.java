package com.huawei.dmestore.dao;

import com.huawei.dmestore.DMEServiceApplication;
import com.huawei.dmestore.entity.DmeInfo;
import com.huawei.dmestore.exception.DmeException;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lianq
 * @className DmeInfoDaoTest
 * @description TODO
 * @date 2020/12/3 10:01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DMEServiceApplication.class)
public class DmeInfoDaoTest {

    @Autowired
    DmeInfoDao dmeInfoDao;

    @Test
    public void getDbFileName() {
        dmeInfoDao.getDbFileName();
    }

    @Test
    public void setUrl() {
        dmeInfoDao.setUrl("321");

    }

    @Test
    public void addDmeInfo() throws Exception {
        DmeInfo dmeInfo = new DmeInfo();
        dmeInfo.setId(321);
        dmeInfo.setHostIp("321");
        dmeInfo.setHostPort(321);
        dmeInfo.setUserName("321");
        dmeInfo.setPassword("321");
        dmeInfo.setCreateTime(new Date());
        dmeInfo.setUpdateTime(new Date());
        dmeInfo.setState(1);
        dmeInfoDao.addDmeInfo(dmeInfo);

    }

    @Test
    public void getDmeInfo() throws DmeException {
        dmeInfoDao.getDmeInfo();

    }
}