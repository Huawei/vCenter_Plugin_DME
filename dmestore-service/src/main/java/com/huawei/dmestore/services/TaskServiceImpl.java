package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.model.TaskDetailInfo;
import com.huawei.dmestore.model.TaskDetailResource;
import com.huawei.dmestore.utils.ToolUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TaskServiceImpl
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public class TaskServiceImpl implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static final int TASK_SUCCESS = 3;

    private static final int TASK_FLAG_2 = 2;

    private static final int TASK_FINISH = 100;

    private static final int ONE_SECEND = 1000;

    private static final int TWO_SECEND = 2 * ONE_SECEND;

    private static final int HTTP_STATUS_200 = 200;

    private static final String ID_FIELD = "id";

    private DmeAccessService dmeAccessService;

    /**
     * 轮询任务状态的超值时间
     */
    private final int taskTimeOut = 10 * 60 * 1000;

    private Gson gson = new Gson();

    @Override
    public List<TaskDetailInfo> listTasks() {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(DmeConstants.DME_TASK_BASE_URL, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
                LOG.error("get task list exception:{}", responseEntity.getBody());
                return Collections.EMPTY_LIST;
            }
        } catch (DmeException ex) {
            LOG.error("get task list error:{}", ex);
            return Collections.EMPTY_LIST;
        }

        // 解析responseEntity 转换为 TaskDetailInfo
        Object object = responseEntity.getBody();
        List<TaskDetailInfo> tasks = converBean(null, object);
        return tasks;
    }

    @Override
    public TaskDetailInfo queryTaskById(String taskId) {
        String url = DmeConstants.QUERY_TASK_URL.replace("{task_id}", taskId);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
                LOG.error("queryTaskById failed!taskId={},errorMsg:{}", taskId, responseEntity.getBody());
                return null;
            }
        } catch (DmeException ex) {
            LOG.error("queryTaskById error, errorMsg:{}", ex.getMessage());
            return null;
        }

        // 解析responseEntity 转换为 TaskDetailInfo
        Object object = responseEntity.getBody();
        List<TaskDetailInfo> tasks = converBean(taskId, object);
        if (tasks != null && tasks.size() > 0) {
            return tasks.get(0);
        } else {
            return null;
        }
    }

    @Override
    public JsonObject queryTaskByIdUntilFinish(String taskId) throws DmeException {
        String url = DmeConstants.QUERY_TASK_URL.replace("{task_id}", taskId);
        boolean isLoop = true;
        int waitTime = TWO_SECEND;
        int times = taskTimeOut / waitTime;
        while (isLoop) {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HTTP_STATUS_200) {
                JsonArray taskArray = gson.fromJson(responseEntity.getBody(), JsonArray.class);
                for (int index = 0; index < taskArray.size(); index++) {
                    JsonObject taskDetail = taskArray.get(index).getAsJsonObject();

                    // 只查主任务
                    if (taskDetail.get(ID_FIELD).getAsString().equals(taskId)) {
                        // 任务进度完成100%或者任务状态不正常直接结束查询，否则等待2s后再尝试
                        if (taskDetail.get("progress").getAsInt() == TASK_FINISH
                            || taskDetail.get("status").getAsInt() > TASK_FLAG_2) {
                            return taskDetail;
                        } else {
                            try {
                                Thread.sleep(waitTime);
                            } catch (InterruptedException e) {
                                throw new DmeException(e.getMessage());
                            }
                        }
                    }
                }
            } else {
                throw new DmeSqlException(responseEntity.getBody());
            }

            if ((times--) < 0) {
                throw new DmeSqlException("query task status timeout！taskId=" + taskId);
            }
        }
        return null;
    }

    private List<TaskDetailInfo> converBean(String origonTaskId, Object object) {
        List<TaskDetailInfo> taskDetailInfos = new ArrayList<>();

        JsonArray jsonArray;

        // task列表 则先取任务列表
        String msg = gson.toJson(object);
        if (msg.contains("total") && msg.contains("tasks")) {
            JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            jsonArray = jsonObject.get("tasks").getAsJsonArray();
        } else {
            jsonArray = new JsonParser().parse(object.toString()).getAsJsonArray();
        }
        for (JsonElement jsonElement : jsonArray) {
            TaskDetailInfo taskDetailInfo = new TaskDetailInfo();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(jsonObject.get(ID_FIELD));

            // 过滤子任务
            if (!StringUtils.isEmpty(origonTaskId) && !origonTaskId.equals(taskId)) {
                continue;
            }
            String taskName = ToolUtils.jsonToStr(jsonObject.get("name_en"));
            int status = ToolUtils.jsonToInt(jsonObject.get("status"));
            int progress = ToolUtils.jsonToInt(jsonObject.get("progress"));
            String ownerName = ToolUtils.jsonToStr(jsonObject.get("owner_name"));
            long createTime = ToolUtils.getLong(jsonObject.get("create_time"));
            long startTime = ToolUtils.getLong(jsonObject.get("start_time"));
            long endTime = ToolUtils.getLong(jsonObject.get("end_time"));
            String detail = ToolUtils.jsonToStr(jsonObject.get("detail_en"));
            taskDetailInfo.setId(taskId);
            taskDetailInfo.setTaskName(taskName);
            taskDetailInfo.setStatus(status);
            taskDetailInfo.setProgress(progress);
            taskDetailInfo.setOwnerName(ownerName);
            taskDetailInfo.setStartTime(startTime);
            taskDetailInfo.setCreateTiem(createTime);
            taskDetailInfo.setEndTime(endTime);
            taskDetailInfo.setDetail(detail);

            JsonArray resourcesArray = jsonObject.getAsJsonArray("resources");
            if (resourcesArray != null) {
                List<TaskDetailResource> resourceList = getTaskDetailResources(resourcesArray);
                taskDetailInfo.setResources(resourceList);
            }
            taskDetailInfos.add(taskDetailInfo);
        }
        return taskDetailInfos;
    }

    private List<TaskDetailResource> getTaskDetailResources(JsonArray resourcesArray) {
        List<TaskDetailResource> resourceList = new ArrayList<>();
        for (JsonElement jsonElement1 : resourcesArray) {
            TaskDetailResource tdr = new TaskDetailResource();
            JsonObject resourceObj = jsonElement1.getAsJsonObject();
            String operate = ToolUtils.jsonToStr(resourceObj.get("operate"));
            String type = ToolUtils.jsonToStr(resourceObj.get("type"));
            String id = ToolUtils.jsonToStr(resourceObj.get(ID_FIELD));
            String name = ToolUtils.jsonToStr(resourceObj.get("name"));

            tdr.setOperate(operate);
            tdr.setType(type);
            tdr.setId(id);
            tdr.setName(name);
            resourceList.add(tdr);
        }
        return resourceList;
    }

    /**
     * getTaskStatus
     *
     * @param taskIds 待查询的任务id集合
     * @param taskStatusMap 结束任务对应的状态集合
     * @param timeout 任务查询超时时间 单位ms
     * @param startTime 任务查询开始时间 单位ms
     */
    @Override
    public void getTaskStatus(List<String> taskIds, Map<String, Integer> taskStatusMap, int timeout, long startTime) {
        long start = startTime;

        // 没传开始时间或开始时间小于格林威治启用时间,初始话为当前时间
        if (0 == startTime) {
            start = System.currentTimeMillis();
        }
        List<TaskDetailInfo> detailInfos = new ArrayList<>();
        Set<String> queryTaskIds = new HashSet<>();
        for (String taskId : taskIds) {
            TaskDetailInfo taskDetailInfo = queryTaskById(taskId);
            if (taskDetailInfo != null) {
                detailInfos.add(taskDetailInfo);
            }
        }
        for (TaskDetailInfo taskInfo : detailInfos) {
            if (taskInfo != null) {
                String taskId = taskInfo.getId();

                // 过滤子任务
                if (!taskIds.contains(taskId)) {
                    continue;
                }
                int status = taskInfo.getStatus();
                int progress = taskInfo.getProgress();
                if (progress == TASK_FINISH || status > TASK_FLAG_2) {
                    taskStatusMap.put(taskId, status);
                } else {
                    queryTaskIds.add(taskId);
                }
            }
        }

        // 判断是否超时
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - start;
        if (delta > timeout) {
            LOG.info("query task timeout!taskIds={}", gson.toJson(taskIds));
            return;
        }

        // 未超时 判断是否还有任务未结束
        if (queryTaskIds.size() > 0) {
            try {
                Thread.sleep(ONE_SECEND);
            } catch (InterruptedException e) {
                LOG.info("===wait one secend error==={}", e.getMessage());
            }
            getTaskStatus(new ArrayList<>(queryTaskIds), taskStatusMap, timeout, start);
        } else {
            return;
        }
    }

    @Override
    public boolean checkTaskStatus(List<String> taskIds) {
        boolean isSuccess = false;
        if (taskIds != null && taskIds.size() > 0) {
            Map<String, Integer> taskStatusMap = new HashMap<>();
            getTaskStatus(taskIds, taskStatusMap, taskTimeOut, System.currentTimeMillis());
            for (Map.Entry<String, Integer> entry : taskStatusMap.entrySet()) {
                int status = entry.getValue();
                if (status == TASK_SUCCESS) {
                    isSuccess = true;
                    break;
                }
            }
        }
        return isSuccess;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }
}
