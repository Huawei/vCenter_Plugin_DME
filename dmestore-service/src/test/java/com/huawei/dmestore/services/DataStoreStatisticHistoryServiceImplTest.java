package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className DataStoreStatisticHistoryServiceImplTest
 * @description TODO
 * @date 2020/11/18 10:43
 */
public class DataStoreStatisticHistoryServiceImplTest {

    @Mock
    private DmeAccessService dmeAccessService;
    @Mock
    private DmeRelationInstanceService dmeRelationInstanceService;
    private Gson gson = new Gson();
    private List<String> list = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private List<String> list3 = new ArrayList<>();
    private Map<String, Object> map = new HashMap<>();
    private Map<String, Object> map1 = new HashMap<>();
    Map<String, Map<String, Object>> map2 = new HashMap<>();
    Map<String, Object> map3 = new HashMap<>();
    Map<String, Object> rangeMap = new HashMap<>();
    private JsonObject jsonObject;
    private String param2;
    String url = "/rest/metrics/v1/data-svc/history-data/action/query";
    String[] rangs = {"LAST_5_MINUTE","LAST_1_HOUR","LAST_1_DAY","LAST_1_WEEK","LAST_1_MONTH","LAST_1_QUARTER","HALF_1_YEAR","LAST_1_YEAR"};


    @InjectMocks
    DataStoreStatisticHistoryService dataStoreStatisticHistoryService = new DataStoreStatisticHistoryServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        list.add("321");
        list2.add("321");
        list3.add("321");
        map.put("obj_ids", list);
        //map.put("indicator_ids", "");
        map.put("range", list3);
        //map.put("interval", "");
        map1.put("resId", "321");
        map2.put("321", map1);
        param2 = "{\n" +
                "    \"47FEBD5002AB344D90EC6CFCD6127BA3\": {\n" +
                "        \"1407379178651656\": {\n" +
                "            \"series\": [\n" +
                "                {\n" +
                "                    \"1552480500000\": \"80.0\"\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"1552480800000\": \"80.0\"\n" +
                "                }\n" +
                "            ], \n" +
                "            \"min\": {\n" +
                "                \"1552480740000\": \"80.0\"\n" +
                "            }, \n" +
                "            \"max\": {\n" +
                "                \"1552480740000\": \"80.0\"\n" +
                "            }, \n" +
                "            \"avg\": {\n" +
                "                \"1552480943834\": \"80.0\"\n" +
                "            }\n" +
                "        }, \n" +
                "        \"1407379178586113\": {\n" +
                "            \"series\": [\n" +
                "                {\n" +
                "                    \"1552480500000\": \"49.0\"\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"1552480800000\": \"48.42857142857143\"\n" +
                "                }\n" +
                "            ], \n" +
                "            \"min\": {\n" +
                "                \"1552480890000\": \"48.0\"\n" +
                "            }, \n" +
                "            \"max\": {\n" +
                "                \"1552480740000\": \"49.0\"\n" +
                "            }, \n" +
                "            \"avg\": {\n" +
                "                \"1552480943834\": \"48.55555555555556\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        jsonObject = gson.fromJson(param2, JsonObject.class);
        map3.put("data", jsonObject);
        rangeMap.put("LAST_5_MINUTE","ONE_MINUTE");
        rangeMap.put("LAST_1_HOUR","ONE_MINUTE");
        rangeMap.put("LAST_1_DAY","ONE_MINUTE");
        rangeMap.put("LAST_1_WEEK","HALF_HOUR");
        rangeMap.put("LAST_1_MONTH","HALF_HOUR");
        rangeMap.put("LAST_1_QUARTER","DAY");
        rangeMap.put("HALF_1_YEAR","DAY");
        rangeMap.put("LAST_1_YEAR","DAY");

    }

    @Test
    public void queryVmfsStatistic() throws DmeException {

        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125921381744648\",\"1125921381744649\",\"1125921381744646\",\"1125921381744647\"," +
            "\"1125921381744656\",\"1125921381744657\",\"1125921381744642\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125921381679104\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryVmfsStatistic(map);
        }
    }

    @Test
    public void queryVmfsStatisticCurrent() throws DmeException {

        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125921381744641\",\"1125921381744643\",\"1125921381744656\"," +
            "\"1125921381744657\",\"1125921381744642\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125921381679104\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryVmfsStatisticCurrent(map);
        }

    }

    @Test
    public void queryNfsStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1126179079716867\",\"1126179079716870\",\"1126179079716868\"," +
            "\"1126179079716871\",\"1126179079716869\",\"1126179079716872\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1126179079716864\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryNfsStatistic(map);
        }
    }

    @Test
    public void queryNfsStatisticCurrent() throws DmeException {
        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1126179079716865\",\"1126179079716878\",\"1126179079716869\"," +
            "\"1126179079716872\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1126179079716864\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryNfsStatisticCurrent(map);
        }
    }

    @Test
    public void queryVolumeStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125921381744648\",\"1125921381744649\",\"1125921381744646\",\"1125921381744647\"," +
            "\"1125921381744656\",\"1125921381744657\",\"1125921381744642\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125921381679104\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryVolumeStatistic(map);
        }
    }

    @Test
    public void queryControllerStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125908496842758\",\"1125908496842759\",\"1125908496842756\"," +
            "\"1125908496842757\",\"1125908496842770\",\"1125908496842771\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125908496777216\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryControllerStatistic(map);
        }
    }

    @Test
    public void queryStoragePortStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125925676711943\",\"1125925676711944\",\"1125925676711939\"," +
            "\"1125925676711940\",\"1125925676711952\",\"1125925676711953\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125925676646400\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryStoragePortStatistic(map);
        }
    }

    @Test
    public void queryStorageDiskStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125917086777347\",\"1125917086777348\",\"1125917086777346\"," +
            "\"1125917086777351\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125917086711808\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryStorageDiskStatistic(map);
        }
    }

    @Test
    public void queryFsStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1126179079716867\",\"1126179079716870\",\"1126179079716868\"," +
            "\"1126179079716871\",\"1126179079716869\",\"1126179079716872\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1126179079716864\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryFsStatistic(map);
        }
    }

    @Test
    public void queryServiceLevelStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1126174784815111\",\"1126174784815118\",\"1126174784815117\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1126174784749568\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryServiceLevelStatistic(map);
        }
    }

    @Test
    public void queryServiceLevelLunStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125921381744642\",\"1125921381744643\",\"1125921381744641\"]," +
            "\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125921381679104\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryServiceLevelLunStatistic(map);
        }
    }

    @Test
    public void queryServiceLevelStoragePoolStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125912791810049\",\"1125912791810051\"," +
            "\"1125912791810050\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125912791744512\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryServiceLevelStoragePoolStatistic(map);
        }
    }

    @Test
    public void queryStoragePoolStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125912791810049\",\"1125912791810050\",\"1125912791810051\"]," +
            "\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125912791744512\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryStoragePoolStatistic(map);
        }
    }

    @Test
    public void queryStorageDevcieStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125904201875457\",\"1125904201875464\",\"1125904201875458\",\"1125904201875461\"," +
            "\"1125904201875462\",\"1125904201875459\",\"1125904201875460\",\"1125904201875465\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125904201809920\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryStorageDevcieStatistic(map);
        }

    }

    @Test
    public void queryStorageDevcieCurrentStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125904201875457\",\"1125904201875464\",\"1125904201875458\",\"1125904201875461\"," +
            "\"1125904201875462\",\"1125904201875459\",\"1125904201875460\",\"1125904201875465\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125904201809920\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryStorageDevcieCurrentStatistic(map);
        }
    }

    @Test
    public void queryStoragePoolCurrentStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125912791810049\",\"1125912791810050\",\"1125912791810051\"]," +
            "\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125912791744512\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryStoragePoolCurrentStatistic(map);
        }
    }

    @Test
    public void queryControllerCurrentStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125908496842763\",\"1125908496842755\",\"1125908496842753\"," +
            "\"1125908496842762\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125908496777216\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryControllerCurrentStatistic(map);
        }
    }

    @Test
    public void queryStoragePortCurrentStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125925676711946\",\"1125925676711938\",\"1125925676711951\"," +
            "\"1125925676711945\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125925676646400\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryStoragePortCurrentStatistic(map);
        }
    }

    @Test
    public void queryStorageDiskCurrentStatistic() throws DmeException {

        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125917086777347\",\"1125917086777346\",\"1125917086777345\"," +
            "\"1125917086777351\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125917086711808\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryStorageDiskCurrentStatistic(map);
        }
    }

    @Test
    public void queryHistoryStatistic() throws DmeException {

        String relationOrInstance = "SYS_StoragePool";
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125912791810049\",\"1125912791810050\",\"1125912791810051\"]," +
            "\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125912791744512\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance,map);
        }
    }
    @Test
    public void queryHistoryStatistic2() throws DmeException {

        String relationOrInstance = "SYS_Lun";
        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125921381744648\",\"1125921381744649\",\"1125921381744646\",\"1125921381744647\"," +
            "\"1125921381744656\",\"1125921381744657\",\"1125921381744642\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125921381679104\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance,map);
        }
    }
    @Test
    public void queryHistoryStatistic3() throws DmeException {

        String relationOrInstance = "SYS_DjTier";
        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1126174784815111\",\"1126174784815118\",\"1126174784815117\"]," +
            "\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1126174784749568\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance, map);
        }
    }
    @Test
    public void queryHistoryStatistic4() throws DmeException {

        String relationOrInstance = "SYS_StorageFileSystem";
        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1126179079716867\",\"1126179079716870\",\"1126179079716868\"," +
            "\"1126179079716871\",\"1126179079716869\",\"1126179079716872\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1126179079716864\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance, map);
        }
    }
    @Test
    public void queryHistoryStatistic5() throws DmeException {

        String relationOrInstance = "SYS_Controller";
        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125908496842758\",\"1125908496842759\",\"1125908496842756\"," +
            "\"1125908496842757\",\"1125908496842770\",\"1125908496842771\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125908496777216\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance, map);
        }
    }
    @Test
    public void queryHistoryStatistic6() throws DmeException {

        String relationOrInstance = "SYS_StoragePort";
        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125925676711943\",\"1125925676711944\",\"1125925676711939\"," +
            "\"1125925676711940\",\"1125925676711952\",\"1125925676711953\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125925676646400\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance, map);
        }
    }
    @Test
    public void queryHistoryStatistic7() throws DmeException {

        String relationOrInstance = "SYS_StorDevice";
        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125904201875457\",\"1125904201875464\",\"1125904201875458\",\"1125904201875461\",\"1125904201875462\"," +
            "\"1125904201875459\",\"1125904201875460\",\"1125904201875465\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125904201809920\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance, map);
        }
    }
    @Test
    public void queryHistoryStatistic8() throws DmeException {

        String relationOrInstance = "SYS_StorageDisk";
        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"1125917086777347\",\"1125917086777348\",\"1125917086777346\"," +
            "\"1125917086777351\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125917086711808\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance, map);
        }
    }
    @Test
    public void queryHistoryStatistic9() throws DmeException {

        String relationOrInstance = "SYS";
        
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125917086711808\"}";
        Map<String,Object> map4 = gson.fromJson(param, Map.class);
        for (int i = 0; i < rangs.length; i++) {
            List<String> objects = new ArrayList<>();
            objects.add(rangs[i]);
            map4.put("range", rangs[i]);
            if (rangeMap.containsKey(rangs[i])) {
                map4.put("interval", rangeMap.get(rangs[i]));
                map.put("interval", "");
            }
            map.put("range", objects);
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance, map);
        }
    }

    @Test
    public void queryCurrentStatistic() throws DmeException {

        String[] relationOrInstances = {"SYS_StorageDisk","SYS_StoragePool","SYS_DjTier","SYS_Lun","SYS_StorageFileSystem","SYS_Controller","SYS_StoragePort","SYS_StorDevice"};
        for (int i = 0; i < 7; i++) {
            
            String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"\",\"obj_type_id\":\"1125904201809920\"}";
            Map<String,Object> map4 = gson.fromJson(param, Map.class);
            for (int j = 0; j < rangs.length; j++) {
                List<String> objects = new ArrayList<>();
                objects.add(rangs[j]);
                map4.put("range", rangs[j]);
                if (rangeMap.containsKey(rangs[i])) {
                    map4.put("interval", rangeMap.get(rangs[i]));
                    map.put("interval", "");
                }
                map.put("range", objects);
                when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
                ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
                when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(map4))).thenReturn(responseEntity);
                dataStoreStatisticHistoryService.queryCurrentStatistic(relationOrInstances[i], map);
            }
        }
    }
}