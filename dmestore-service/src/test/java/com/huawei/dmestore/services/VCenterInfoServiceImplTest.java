package com.huawei.dmestore.services;

import com.huawei.dmestore.dao.VCenterInfoDao;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeSqlException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className VCenterInfoServiceImplTest
 * @description TODO
 * @date 2020/11/20 10:05
 */
public class VCenterInfoServiceImplTest {

    @Mock
    private VCenterInfoDao vCenterInfoDao;

    @InjectMocks
    VCenterInfoService vCenterInfoService = new VCenterInfoServiceImpl();
    VCenterInfo vCenterInfo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vCenterInfo = new VCenterInfo();
        vCenterInfo.setCreateTime(new Date());
        vCenterInfo.setHostIp("321");
        vCenterInfo.setId(321);
        vCenterInfo.setPassword("321");
        vCenterInfo.setPushEvent(true);
        vCenterInfo.setPushEventLevel(2);
        vCenterInfo.setUserName("321");
        vCenterInfo.setState(true);
    }

    @Test
    public void addVcenterInfo() throws DmeSqlException {
        when(vCenterInfoDao.addVcenterInfo(vCenterInfo)).thenReturn(1);
        vCenterInfoService.addVcenterInfo(new VCenterInfo());
    }

    @Test
    public void saveVcenterInfo() throws DmeSqlException {
        when(vCenterInfoDao.getVcenterInfo()).thenReturn(vCenterInfo);
        when(vCenterInfoDao.updateVcenterInfo(vCenterInfo)).thenReturn(1);
        vCenterInfoService.saveVcenterInfo(vCenterInfo);
    }

    @Test
    public void findVcenterInfo() throws DmeSqlException {
        when(vCenterInfoDao.getVcenterInfo()).thenReturn(vCenterInfo);
        vCenterInfoService.findVcenterInfo();
    }

    @Test
    public void getVcenterInfo() throws DmeSqlException {
        when(vCenterInfoDao.getVcenterInfo()).thenReturn(vCenterInfo);

        vCenterInfoService.getVcenterInfo();
    }
}