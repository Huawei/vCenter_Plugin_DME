package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className QosParamTest
 * @description TODO
 * @date 2020/12/3 14:09
 */
public class QosParamTest {

    @InjectMocks
    QosParam qosParam = new QosParam();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        QosParam qosParam = new QosParam();
        qosParam.setLatency(21);
        qosParam.setLatencyUnit("21");
        qosParam.setMinBandWidth(21);
        qosParam.setMinIOPS(21);
        qosParam.setMaxBandWidth(21);
        qosParam.setMaxIOPS(21);
        qosParam.setEnabled(true);
        SmartQos smartQos = new SmartQos();
        qosParam.setSmartQos(smartQos);

        qosParam.getLatency();
        qosParam.getLatencyUnit();
        qosParam.getMinBandWidth();
        qosParam.getMinIOPS();
        qosParam.getMaxBandWidth();
        qosParam.getMaxIOPS();
        qosParam.getEnabled();
        qosParam.getSmartQos();


    }

    @Test
    public void testToString() {
        qosParam.toString();
    }
}