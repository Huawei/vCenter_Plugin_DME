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
 * @className TaskServiceImplTest
 * @description TODO
 * @date 2020/11/19 10:30
 */
public class TaskServiceImplTest {

    @Mock
    private DmeAccessService dmeAccessService;
    @InjectMocks
    TaskService taskService = new TaskServiceImpl();
    private Gson gson = new Gson();
    ResponseEntity<String> responseEntity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void listTasks() throws DmeException {
        String resp = "{\n" +
                "    \"id\": \"564537e8-295b-4cb6-8484-171ea552cb40\", \n" +
                "    \"name_en\": \"Create volume\", \n" +
                "    \"name_cn\": \"创建卷\", \n" +
                "    \"description\": \"create a volume.\", \n" +
                "    \"parent_id\": \"564537e8-295b-4cb6-8484-171ea552cb40\", \n" +
                "    \"seq_no\": 1, \n" +
                "    \"status\": 3, \n" +
                "    \"progress\": 100, \n" +
                "    \"owner_name\": \"admin\", \n" +
                "    \"owner_id\": \"1\", \n" +
                "    \"create_time\": 1580953613057, \n" +
                "    \"start_time\": 1580953613091, \n" +
                "    \"end_time\": 1580953615050, \n" +
                "    \"detail_en\": null, \n" +
                "    \"detail_cn\": null, \n" +
                "    \"resources\": [ ]\n" +
                "}\n";
        JsonObject jsonObject = gson.fromJson(resp, JsonObject.class);
        List<JsonObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("tasks", jsonObjects);
        map.put("total", 1);
        responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/taskmgmt/v1/tasks", HttpMethod.GET, null)).thenReturn(responseEntity);
        taskService.listTasks();
    }

    @Test
    public void queryTaskById() throws DmeException {
        String resp = "{\n" +
                "    \"id\": \"564537e8-295b-4cb6-8484-171ea552cb40\", \n" +
                "    \"name_en\": \"Create volume\", \n" +
                "    \"name_cn\": \"创建卷\", \n" +
                "    \"description\": null, \n" +
                "    \"parent_id\": \"564537e8-295b-4cb6-8484-171ea552cb40\", \n" +
                "    \"seq_no\": 1, \n" +
                "    \"status\": 3, \n" +
                "    \"progress\": 100, \n" +
                "    \"owner_name\": \"admin\", \n" +
                "    \"owner_id\": \"1\", \n" +
                "    \"create_time\": 1580953613057, \n" +
                "    \"start_time\": 1580953613091, \n" +
                "    \"end_time\": 1580953615050, \n" +
                "    \"detail_en\": null, \n" +
                "    \"detail_cn\": null, \n" +
                "    \"resources\": [\n" +
                "        {\n" +
                "            \"operate\": \"MODIFY\", \n" +
                "            \"type\": \"Device\", \n" +
                "            \"id\": \"4e1db765-4882-11ea-95d0-00505691e086\", \n" +
                "            \"name\": \"Huawei.Storage\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n" +
                "";
        JsonObject jsonObject = gson.fromJson(resp, JsonObject.class);
        List<JsonObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        responseEntity = new ResponseEntity<>(gson.toJson(jsonObjects), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/taskmgmt/v1/tasks/321", HttpMethod.GET, null)).thenReturn(responseEntity);
        taskService.queryTaskById("321");
    }

    @Test
    public void queryTaskByIdUntilFinish() throws DmeException {
        String resp = "{\n" +
                "    \"id\": \"321\", \n" +
                "    \"name_en\": \"Create volume\", \n" +
                "    \"name_cn\": \"创建卷\", \n" +
                "    \"description\": null, \n" +
                "    \"parent_id\": \"564537e8-295b-4cb6-8484-171ea552cb40\", \n" +
                "    \"seq_no\": 1, \n" +
                "    \"status\": 3, \n" +
                "    \"progress\": 100, \n" +
                "    \"owner_name\": \"admin\", \n" +
                "    \"owner_id\": \"1\", \n" +
                "    \"create_time\": 1580953613057, \n" +
                "    \"start_time\": 1580953613091, \n" +
                "    \"end_time\": 1580953615050, \n" +
                "    \"detail_en\": null, \n" +
                "    \"detail_cn\": null, \n" +
                "    \"resources\": [\n" +
                "        {\n" +
                "            \"operate\": \"MODIFY\", \n" +
                "            \"type\": \"Device\", \n" +
                "            \"id\": \"4e1db765-4882-11ea-95d0-00505691e086\", \n" +
                "            \"name\": \"Huawei.Storage\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n" +
                "";
        JsonObject jsonObject = gson.fromJson(resp, JsonObject.class);
        List<JsonObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        responseEntity = new ResponseEntity<>(gson.toJson(jsonObjects), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/taskmgmt/v1/tasks/321", HttpMethod.GET, null)).thenReturn(responseEntity);
        taskService.queryTaskByIdUntilFinish("321");
    }

    @Test
    public void getTaskStatus() throws DmeException {
        List<String> list = new ArrayList<>();
        list.add("321");
        Map<String, Integer> map = new HashMap<>();
        map.put("ok", 200);
        String resp = "{\n" +
                "    \"id\": \"321\", \n" +
                "    \"name_en\": \"Create volume\", \n" +
                "    \"name_cn\": \"创建卷\", \n" +
                "    \"description\": null, \n" +
                "    \"parent_id\": \"564537e8-295b-4cb6-8484-171ea552cb40\", \n" +
                "    \"seq_no\": 1, \n" +
                "    \"status\": 3, \n" +
                "    \"progress\": 100, \n" +
                "    \"owner_name\": \"admin\", \n" +
                "    \"owner_id\": \"1\", \n" +
                "    \"create_time\": 1580953613057, \n" +
                "    \"start_time\": 1580953613091, \n" +
                "    \"end_time\": 1580953615050, \n" +
                "    \"detail_en\": null, \n" +
                "    \"detail_cn\": null, \n" +
                "    \"resources\": [\n" +
                "        {\n" +
                "            \"operate\": \"MODIFY\", \n" +
                "            \"type\": \"Device\", \n" +
                "            \"id\": \"4e1db765-4882-11ea-95d0-00505691e086\", \n" +
                "            \"name\": \"Huawei.Storage\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n" +
                "";
        JsonObject jsonObject = gson.fromJson(resp, JsonObject.class);
        List<JsonObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        responseEntity = new ResponseEntity<>(gson.toJson(jsonObjects), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/taskmgmt/v1/tasks/321", HttpMethod.GET, null)).thenReturn(responseEntity);
        taskService.getTaskStatus(list, map, 20000, 0);
    }

    @Test
    public void checkTaskStatus() throws DmeException {
        List<String> list = new ArrayList<>();
        list.add("321");
        String resp = "{\n" +
                "    \"id\": \"321\", \n" +
                "    \"name_en\": \"Create volume\", \n" +
                "    \"name_cn\": \"创建卷\", \n" +
                "    \"description\": null, \n" +
                "    \"parent_id\": \"564537e8-295b-4cb6-8484-171ea552cb40\", \n" +
                "    \"seq_no\": 1, \n" +
                "    \"status\": 3, \n" +
                "    \"progress\": 100, \n" +
                "    \"owner_name\": \"admin\", \n" +
                "    \"owner_id\": \"1\", \n" +
                "    \"create_time\": 1580953613057, \n" +
                "    \"start_time\": 1580953613091, \n" +
                "    \"end_time\": 1580953615050, \n" +
                "    \"detail_en\": null, \n" +
                "    \"detail_cn\": null, \n" +
                "    \"resources\": [\n" +
                "        {\n" +
                "            \"operate\": \"MODIFY\", \n" +
                "            \"type\": \"Device\", \n" +
                "            \"id\": \"4e1db765-4882-11ea-95d0-00505691e086\", \n" +
                "            \"name\": \"Huawei.Storage\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n" +
                "";
        JsonObject jsonObject = gson.fromJson(resp, JsonObject.class);
        List<JsonObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        responseEntity = new ResponseEntity<>(gson.toJson(jsonObjects), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/taskmgmt/v1/tasks/321", HttpMethod.GET, null)).thenReturn(responseEntity);
        taskService.checkTaskStatus(list);
    }
}