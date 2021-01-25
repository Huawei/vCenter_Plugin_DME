package com.huawei.dmestore.utils;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.services.ServiceLevelServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author lianq
 * @className JsonUtilTest
 * @description TODO
 * @date 2020/12/1 20:15
 */
public class JsonUtilTest {

    @InjectMocks
    JsonUtil jsonUtil = new JsonUtil();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void readAsMap() throws IOException {
        String data = " {\n" +
            "    \"id\": \"b94bff9d-0dfb-11eb-bd3d-0050568491c9\",\n" +
            "    \"name\": \"Huawei.Storage\",\n" +
            "    \"ip\": \"10.143.133.201\",\n" +
            "    \"status\": \"1\",\n" +
            "    \"synStatus\": \"2\",\n" +
            "    \"sn\": \"2102351QLH9WK5800028\",\n" +
            "    \"vendor\": \"Huawei\",\n" +
            "    \"model\": \"5300 V5\",\n" +
            "    \"usedCapacity\": 297.5625,\n" +
            "    \"totalCapacity\": 6542.693389892578,\n" +
            "    \"totalEffectiveCapacity\": 3181,\n" +
            "    \"freeEffectiveCapacity\": 2883.44,\n" +
            "    \"subscriptionCapacity\": 309.57,\n" +
            "    \"protectionCapacity\": 0,\n" +
            "    \"fileCapacity\": 77.5,\n" +
            "    \"blockCapacity\": 220.06,\n" +
            "    \"dedupedCapacity\": 0,\n" +
            "    \"compressedCapacity\": 0,\n" +
            "    \"optimizeCapacity\": 297.5625,\n" +
            "    \"azIds\": [],\n" +
            "    \"storagePool\": null,\n" +
            "    \"volume\": null,\n" +
            "    \"fileSystem\": null,\n" +
            "    \"dTrees\": null,\n" +
            "    \"nfsShares\": null,\n" +
            "    \"bandPorts\": null,\n" +
            "    \"logicPorts\": null,\n" +
            "    \"storageControllers\": null,\n" +
            "    \"storageDisks\": null,\n" +
            "    \"productVersion\": \"V500R007C10\",\n" +
            "    \"warning\": null,\n" +
            "    \"event\": null,\n" +
            "    \"location\": \"\",\n" +
            "    \"patchVersion\": \"SPH013\",\n" +
            "    \"maintenanceStart\": null,\n" +
            "    \"maintenanceOvertime\": null\n" +
            "  }";
        jsonUtil.readAsMap(data);

    }

    @Test
    public void javaTest() {
        long nowTime = System.currentTimeMillis();
        System.out.println(nowTime);
        // 开始结束时间计算
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(System.currentTimeMillis());
        // 日期减1年
        //rightNow.add(Calendar.YEAR,-1);
        // 日期加3个月
        rightNow.add(Calendar. DAY_OF_MONTH,-7);
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        System.out.println(dateFormat.format(rightNow.getTimeInMillis()));
    }


    @Test
   public void test() throws DmeException {
        ServiceLevelServiceImpl serviceLevelService = new ServiceLevelServiceImpl();
        System.out.println(serviceLevelService.statLunDatasetsQuery("ace09660-de46-4b24-adc6-18a2618e3f6e",
            "LAST_1_DAY").toString());
    }
}