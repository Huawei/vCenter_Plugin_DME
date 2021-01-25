package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.BestPracticeUpResultResponse;
import com.huawei.dmestore.model.BestPracticeUpdateByTypeRequest;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.BestPracticeProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * BestPracticeController
 *
 * @author wangxiangyong
 * @since 2020-12-01
 **/
@RestController
@RequestMapping(value = "v1/bestpractice")
public class BestPracticeController extends BaseController {
    @Autowired
    private BestPracticeProcessService bestPracticeProcessService;

    public BestPracticeProcessService getBestPracticeProcessService() {
        return bestPracticeProcessService;
    }

    public void setBestPracticeProcessService(BestPracticeProcessService bestPracticeProcessService) {
        this.bestPracticeProcessService = bestPracticeProcessService;
    }

    /**
     * bestPractice
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResponseBodyBean bestPractice() {
        try {
            bestPracticeProcessService.check(null);
            return success();
        } catch (VcenterException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * getBestPracticeRecords
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/records/all", method = RequestMethod.GET)
    public ResponseBodyBean getBestPracticeRecords() {
        try {
            return success(bestPracticeProcessService.getCheckRecord());
        } catch (DmeSqlException ex) {
            return failure(ex.getMessage());
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getBySettingAndPage
     *
     * @param hostSetting hostSetting
     * @param pageNo      pageNo
     * @param pageSize    pageSize
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/records/bypage", method = RequestMethod.GET)
    public ResponseBodyBean getBySettingAndPage(@RequestParam String hostSetting, @RequestParam int pageNo,
                                                @RequestParam int pageSize) {
        try {
            return success(bestPracticeProcessService.getCheckRecordBy(hostSetting, pageNo, pageSize));
        } catch (DmeException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * bylist
     *
     * @param list list
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/bylist", method = RequestMethod.POST)
    public ResponseBodyBean bylist(@RequestBody List<BestPracticeUpdateByTypeRequest> list) {
        try {
            List<BestPracticeUpResultResponse> resultList = new ArrayList<>();
            for (int start = 0; start < list.size(); start++) {
                BestPracticeUpdateByTypeRequest request = list.get(start);
                List<BestPracticeUpResultResponse> listTemp = bestPracticeProcessService.update(
                    request.getHostObjectIds(), request.getHostSetting());
                if (null != listTemp && listTemp.size() > 0) {
                    resultList.addAll(listTemp);
                }
            }
            return success(resultList);
        } catch (DmeException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * upByHostSetting
     *
     * @param request request
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/bytype", method = RequestMethod.POST)
    public ResponseBodyBean upByHostSetting(@RequestBody BestPracticeUpdateByTypeRequest request) {
        try {
            return success(bestPracticeProcessService.update(request.getHostObjectIds(), request.getHostSetting()));
        } catch (DmeException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * upByHosts
     *
     * @param hostObjectIds hostObjectIds
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/byhosts", method = RequestMethod.POST)
    public ResponseBodyBean upByHosts(@RequestBody List<String> hostObjectIds) {
        try {
            return success(bestPracticeProcessService.update(hostObjectIds));
        } catch (DmeSqlException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * upAll
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/all", method = RequestMethod.POST)
    public ResponseBodyBean upAll() {
        try {
            return success(bestPracticeProcessService.update(null, null));
        } catch (DmeSqlException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * upByCluster
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/byCluster", method = RequestMethod.POST)
    public ResponseBodyBean upByCluster() {
        try {
            return success(bestPracticeProcessService.update(null, null));
        } catch (DmeSqlException e) {
            return failure(e.getMessage());
        }
    }
}
