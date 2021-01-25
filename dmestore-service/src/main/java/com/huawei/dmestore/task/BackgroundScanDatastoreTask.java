package com.huawei.dmestore.task;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.services.DmeNFSAccessService;
import com.huawei.dmestore.services.VmfsAccessService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * BackgroundScanDatastoreTask
 *
 * @ClassName: BackgroundScanDatastoreTask
 * @Company: GH-CA
 * @author: yy
 * @since 2020-09-02
 **/
@Component
public class BackgroundScanDatastoreTask implements StatefulJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundScanDatastoreTask.class);

    /**
     * 后台定时扫描
     */
    public void scanDatastore() {
        LOGGER.info("scanDatastore start");

        // 扫描Vmfs
        try {
            Object obj = ApplicationContextHelper.getBean("VmfsAccessServiceImpl");
            ((VmfsAccessService) obj).scanVmfs();
        } catch (DmeException e) {
            LOGGER.error("scanDatastore vmfs error", e);
        }

        // 扫描nfs
        try {
            Object obj = ApplicationContextHelper.getBean("DmeNFSAccessServiceImpl");
            ((DmeNFSAccessService) obj).scanNfs();
        } catch (DmeException e) {
            LOGGER.error("scanDatastore nfs error", e);
        }
        LOGGER.info("scanDatastore end");
    }

    /**
     * execute.
     */
    public void execute() {
        LOGGER.info("scanDatastore rrr start");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        scanDatastore();
    }
}
