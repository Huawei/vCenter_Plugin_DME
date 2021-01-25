package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.DmeStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * DmeStorageController
 *
 * @author lianqiang
 * @since 2020-12-01
 **/
@RestController
@RequestMapping("/dmestorage")
public class DmeStorageController extends BaseController {
    /**
     * LOG
     */
    public static final Logger LOG = LoggerFactory.getLogger(DmeStorageController.class);

    @Autowired
    private DmeStorageService dmeStorageService;

    /**
     * getStorages
     *
     * @return ResponseBodyBean
     */
    @GetMapping("/storages")
    public ResponseBodyBean getStorages() {
        try {
            return success(dmeStorageService.getStorages());
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getStorageDetail
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @GetMapping("/storage")
    public ResponseBodyBean getStorageDetail(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmeStorageService.getStorageDetail(storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getStoragePools
     *
     * @param storageId storageId
     * @param mediaType mediaType
     * @return ResponseBodyBean
     */
    @GetMapping("/storagepools")
    public ResponseBodyBean getStoragePools(@RequestParam(name = "storageId") String storageId,
                                            @RequestParam(name = "mediaType", defaultValue = "all", required = false)
                                                String mediaType) {
        try {
            return success(dmeStorageService.getStoragePools(storageId, mediaType));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getLogicPorts
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @GetMapping("/logicports")
    @ResponseBody
    public ResponseBodyBean getLogicPorts(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmeStorageService.getLogicPorts(storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getVolumesByPage
     *
     * @param storageId storageId
     * @param pageSize  pageSize
     * @param pageNo    pageNo
     * @return ResponseBodyBean
     */
    @GetMapping("/volumes/byPage")
    @ResponseBody
    public ResponseBodyBean getVolumesByPage(
        @RequestParam(name = "storageId", required = false, defaultValue = "") String storageId,
        @RequestParam(name = "pageSize", required = false, defaultValue = "20") String pageSize,
        @RequestParam(name = "pageNo", required = false, defaultValue = "0") String pageNo) {
        try {
            return success(dmeStorageService.getVolumesByPage(storageId, pageSize, pageNo));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getFileSystems
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @GetMapping("/filesystems")
    @ResponseBody
    public ResponseBodyBean getFileSystems(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmeStorageService.getFileSystems(storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getDtrees
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @GetMapping("/dtrees")
    @ResponseBody
    public ResponseBodyBean getDtrees(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmeStorageService.getDtrees(storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getNfsShares
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @GetMapping("/nfsshares")
    public ResponseBodyBean getNfsShares(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmeStorageService.getNfsShares(storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getBandPorts
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @GetMapping("/bandports")
    @ResponseBody
    public ResponseBodyBean getBandPorts(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmeStorageService.getBandPorts(storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getStorageControllers
     *
     * @param storageDeviceId storageDeviceId
     * @return ResponseBodyBean
     */
    @GetMapping("/storagecontrollers")
    public ResponseBodyBean getStorageControllers(@RequestParam(name = "storageDeviceId") String storageDeviceId) {
        try {
            return success(dmeStorageService.getStorageControllers(storageDeviceId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getStorageDisks
     *
     * @param storageDeviceId storageDeviceId
     * @return ResponseBodyBean
     */
    @GetMapping("/storagedisks")
    public ResponseBodyBean getStorageDisks(@RequestParam(name = "storageDeviceId") String storageDeviceId) {
        try {
            return success(dmeStorageService.getStorageDisks(storageDeviceId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getStorageEthPorts
     *
     * @param storageSn storageSn
     * @return ResponseBodyBean
     * @throws Exception Exception
     */
    @GetMapping("/getstorageethports")
    public ResponseBodyBean getStorageEthPorts(@RequestParam(name = "storageSn") String storageSn) throws Exception {
        try {
            return success(dmeStorageService.getStorageEthPorts(storageSn));
        } catch (DmeException e) {
            return failure("get Storage Eth Ports failure:" + e.toString());
        }
    }

    /**
     * getStoragePort
     *
     * @param storageDeviceId storageDeviceId
     * @param portType        portType
     * @return ResponseBodyBean
     */
    @GetMapping("/storageport")
    public ResponseBodyBean getStoragePort(@RequestParam(name = "storageDeviceId") String storageDeviceId,
                                           @RequestParam(name = "portType", defaultValue = "ALL", required = false)
                                               String portType) {
        try {
            return success(dmeStorageService.getStoragePort(storageDeviceId, portType));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getFailoverGroups
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @GetMapping("/failovergroups")
    @ResponseBody
    public ResponseBodyBean getFailoverGroups(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmeStorageService.getFailoverGroups(storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * listStoragePerformance
     *
     * @param storageIds storageIds
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/liststorageperformance", method = RequestMethod.GET)
    public ResponseBodyBean listStoragePerformance(@RequestParam("storageIds") List<String> storageIds) {
        try {
            return success(dmeStorageService.listStoragePerformance(storageIds));
        } catch (DmeException e) {
            LOG.error("get Storage performance failure:", e);
            return failure("get Storage performance failure:" + e.toString());
        }
    }

    /**
     * listStoragePoolPerformance
     *
     * @param storagePoolIds storagePoolIds
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/liststoragepoolperformance", method = RequestMethod.GET)
    public ResponseBodyBean listStoragePoolPerformance(@RequestParam("storagePoolIds") List<String> storagePoolIds) {
        try {
            return success(dmeStorageService.listStoragePoolPerformance(storagePoolIds));
        } catch (DmeException e) {
            LOG.error("get Storage Pool performance failure:", e);
            return failure("get Storage Pool performance failure:" + e.toString());
        }
    }

    /**
     * listStorageControllerPerformance
     *
     * @param storageControllerIds storageControllerIds
     * @return ResponseBodyBean
     * @throws Exception Exception
     */
    @RequestMapping(value = "/listStorageControllerPerformance", method = RequestMethod.GET)
    public ResponseBodyBean listStorageControllerPerformance(
        @RequestParam("storageControllerIds") List<String> storageControllerIds) throws Exception {
        try {
            return success(dmeStorageService.listStorageControllerPerformance(storageControllerIds));
        } catch (DmeException e) {
            LOG.error("get Storage controller performance failure:", e);
            return failure("get Storage controller performance failure:" + e.toString());
        }
    }

    /**
     * listStorageDiskPerformance
     *
     * @param storageDiskIds storageDiskIds
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listStorageDiskPerformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listStorageDiskPerformance(@RequestParam("storageDiskIds") List<String> storageDiskIds) {
        try {
            return success(dmeStorageService.listStorageDiskPerformance(storageDiskIds));
        } catch (DmeException e) {
            LOG.error("get Storage disk performance failure:", e);
            return failure("get Storage disk performance failure:" + e.toString());
        }
    }

    /**
     * listStoragePortPerformance
     *
     * @param storagePortIds storagePortIds
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listStoragePortPerformance", method = RequestMethod.POST)
    public ResponseBodyBean listStoragePortPerformance(@RequestParam("storagePortIds") List<String> storagePortIds) {
        try {
            return success(dmeStorageService.listStoragePortPerformance(storagePortIds));
        } catch (DmeException e) {
            LOG.error("get Storage port performance failure:", e);
            return failure("get Storage port performance failure:" + e.toString());
        }
    }

    /**
     * listVolumesPerformance
     *
     * @param volumeId volumeId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listVolumesPerformance", method = RequestMethod.GET)
    public ResponseBodyBean listVolumesPerformance(@RequestParam("volumeId") List<String> volumeId) {
        try {
            return success(dmeStorageService.listVolumesPerformance(volumeId));
        } catch (DmeException e) {
            LOG.error("get Storage volume performance failure:", e);
            return failure("get Storage volume performance failure:" + e.toString());
        }
    }

    /**
     * queryVolumeByName
     *
     * @param name name
     * @return ResponseBodyBean
     */
    @GetMapping("/queryvolumebyname")
    public ResponseBodyBean queryVolumeByName(@RequestParam("name") String name) {
        try {
            return success(dmeStorageService.queryVolumeByName(name));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * queryFsByName
     *
     * @param name      name
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @GetMapping("/queryfsbyname")
    public ResponseBodyBean queryFsByName(@RequestParam("name") String name,
                                          @RequestParam("storageId") String storageId) {
        try {
            return success(dmeStorageService.queryFsByName(name, storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * queryShareByName
     *
     * @param name      name
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @GetMapping("/querysharebyname")
    public ResponseBodyBean queryShareByName(@RequestParam("name") String name,
                                             @RequestParam("storageId") String storageId) {
        try {
            return success(dmeStorageService.queryShareByName("/" + name, storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
}
