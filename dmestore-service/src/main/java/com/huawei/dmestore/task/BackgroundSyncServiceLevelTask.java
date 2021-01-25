package com.huawei.dmestore.task;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.services.ServiceLevelService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * BackgroundSyncServiceLevelTask
 *
 * @author wangxy
 * @since 2020-11-30
 **/
@Component
public class BackgroundSyncServiceLevelTask implements StatefulJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundSyncServiceLevelTask.class);

    @Override
    public void execute(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("updateVmwarePolicy start");
        try {
            Object obj = ApplicationContextHelper.getBean("ServiceLevelServiceImpl");
            ((ServiceLevelService) obj).updateVmwarePolicy();
        } catch (DmeException e) {
            LOGGER.error("updateVmwarePolicy error", e);
        }
        LOGGER.info("updateVmwarePolicy end");
    }
}
