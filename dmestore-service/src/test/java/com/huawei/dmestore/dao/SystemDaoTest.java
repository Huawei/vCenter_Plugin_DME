package com.huawei.dmestore.dao;

import com.huawei.dmestore.DMEServiceApplication;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lianq
 * @className SystemDaoTest
 * @description TODO
 * @date 2020/12/3 9:57
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DMEServiceApplication.class)
public class SystemDaoTest {

    @Autowired
    SystemDao systemDao;

    @Test
    public void getDbFileName() {
        systemDao.getDbFileName();
    }

    @Test
    public void setUrl() {
        systemDao.setUrl("321");
    }

    @Test
    public void checkTable() throws SQLException {
        systemDao.checkTable("321");

    }

    @Test
    public void checkExistAndCreateTable() throws Exception {
        systemDao.checkExistAndCreateTable("321", "321");
    }

    @Test
    public void initData() throws Exception {
        systemDao.initData("321");

    }

    @Test
    public void isColumnExists() throws SQLException {
        systemDao.isColumnExists("321", "321");
    }

    @Test
    public void cleanAllData() {
        systemDao.cleanAllData();
    }
}