package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className OverviewServiceImplTest
 * @description TODO
 * @date 2020/11/19 9:41
 */
public class OverviewServiceImplTest {

    @Mock
    private VmfsAccessService vmfsAccessService;
    @Mock
    private DmeNFSAccessService dmeNfsAccessService;
    @Mock
    private DmeStorageService dmeStorageService;
    @Mock
    private BestPracticeProcessService bestPracticeProcessService;
    @InjectMocks
    OverviewService overviewService = new OverviewServiceImpl();
    NfsDataInfo nfsDataInfo;
    VmfsDataInfo vmfsDataInfo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        nfsDataInfo = new NfsDataInfo();
        nfsDataInfo.setFreeSpace(20.0);
        nfsDataInfo.setCapacity(30.0);
        nfsDataInfo.setBandwidth(10F);
        nfsDataInfo.setDevice("321");
        nfsDataInfo.setFsId("321");
        nfsDataInfo.setLogicPort("321");
        nfsDataInfo.setName("321");
        nfsDataInfo.setLogicPortId("321");
        nfsDataInfo.setObjectid("321");
        nfsDataInfo.setOps(2f);
        nfsDataInfo.setShareId("321");
        vmfsDataInfo = new VmfsDataInfo();
        vmfsDataInfo.setCapacity(50.0);
        vmfsDataInfo.setFreeSpace(20.0);
        vmfsDataInfo.setBandwidth(20f);
        vmfsDataInfo.setDevice("321");
        vmfsDataInfo.setLatency(20f);
        vmfsDataInfo.setName("321");
        vmfsDataInfo.setMaxBandwidth(20);
        vmfsDataInfo.setObjectid("321");
        vmfsDataInfo.setVolumeId("321");
    }

    @Test
    public void getStorageNum() throws DmeException {
        Storage storage = new Storage();
        List<Storage> storages = new ArrayList<>();
        storages.add(storage);
        when(dmeStorageService.getStorages()).thenReturn(storages);
        overviewService.getStorageNum();
    }

    @Test
    public void getDataStoreCapacitySummary() throws DmeException {
        List<VmfsDataInfo> vmfsDataInfos = new ArrayList<>();
        vmfsDataInfos.add(vmfsDataInfo);
        when(vmfsAccessService.listVmfs()).thenReturn(vmfsDataInfos);
        List<NfsDataInfo> nfsDataInfos = new ArrayList<>();
        nfsDataInfos.add(nfsDataInfo);
        when(dmeNfsAccessService.listNfs()).thenReturn(nfsDataInfos);
        overviewService.getDataStoreCapacitySummary("VMFS");//

    }

    @Test
    public void getDataStoreCapacityTopN() throws DmeException {
        List<VmfsDataInfo> vmfsDataInfos = new ArrayList<>();
        vmfsDataInfos.add(vmfsDataInfo);
        when(vmfsAccessService.listVmfs()).thenReturn(vmfsDataInfos);
        List<NfsDataInfo> nfsDataInfos = new ArrayList<>();
        nfsDataInfos.add(nfsDataInfo);
        when(dmeNfsAccessService.listNfs()).thenReturn(nfsDataInfos);
        overviewService.getDataStoreCapacityTopN("NFS", 5);

    }

    @Test
    public void getBestPracticeViolations() throws DmeException {
        BestPracticeBean bestPracticeBean = new BestPracticeBean();
        bestPracticeBean.setActualValue("321");
        bestPracticeBean.setAutoRepair("321");
        bestPracticeBean.setHostName("321");
        bestPracticeBean.setHostObjectId("321");
        bestPracticeBean.setHostSetting("321");
        bestPracticeBean.setLevel("321");
        bestPracticeBean.setNeedReboot("321");
        bestPracticeBean.setRecommendValue("321");
        List<BestPracticeBean> bestPracticeBeans = new ArrayList<>();
        bestPracticeBeans.add(bestPracticeBean);
        BestPracticeCheckRecordBean bestPracticeCheckRecordBean = new BestPracticeCheckRecordBean();
        bestPracticeCheckRecordBean.setCount(20);
        bestPracticeCheckRecordBean.setHostList(bestPracticeBeans);
        bestPracticeCheckRecordBean.setHostSetting("321");
        bestPracticeCheckRecordBean.setLevel("321");
        bestPracticeCheckRecordBean.setRecommendValue("321");
        List<BestPracticeCheckRecordBean> bestPracticeCheckRecordBeans = new ArrayList<>();
        bestPracticeCheckRecordBeans.add(bestPracticeCheckRecordBean);
        when(bestPracticeProcessService.getCheckRecord()).thenReturn(bestPracticeCheckRecordBeans);
        overviewService.getBestPracticeViolations();

    }
}