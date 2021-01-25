package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VolumeUpdateTest
 * @description TODO
 * @date 2020/12/3 11:33
 */
public class VolumeUpdateTest {

    @InjectMocks
    VolumeUpdate volumeUpdate = new VolumeUpdate();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        VolumeUpdate volumeUpdate = new VolumeUpdate();
        volumeUpdate.setName("321");
        volumeUpdate.setOwnerController("321");
        volumeUpdate.setPrefetchPolicy("321");
        volumeUpdate.setPrefetchValue("321");
        CustomizeVolumeTuning customizeVolumeTuning = new CustomizeVolumeTuning();
        SmartQos smartQos = new SmartQos();
        smartQos.setLatencyUnit("321");
        smartQos.setEnabled(true);
        smartQos.setControlPolicy("321");
        smartQos.setName("321");
        smartQos.setLatency(21);
        smartQos.setMaxbandwidth(21);
        smartQos.setMaxiops(21);
        smartQos.setMinbandwidth(21);
        smartQos.setMiniops(21);
        customizeVolumeTuning.setSmartQos(smartQos);
        customizeVolumeTuning.setCompressionEnabled(true);
        customizeVolumeTuning.setDedupeEnabled(true);
        customizeVolumeTuning.setSmarttier(1);
        customizeVolumeTuning.getSmartQos();
        customizeVolumeTuning.getCompressionEnabled();
        customizeVolumeTuning.getDedupeEnabled();
        customizeVolumeTuning.getSmarttier();
        volumeUpdate.setTuning(customizeVolumeTuning);

        volumeUpdate.getName();
        volumeUpdate.getOwnerController();
        volumeUpdate.getPrefetchPolicy();
        volumeUpdate.getPrefetchValue();
        volumeUpdate.getTuning();


    }

    @Test
    public void testToString() {
        volumeUpdate.toString();
    }
}