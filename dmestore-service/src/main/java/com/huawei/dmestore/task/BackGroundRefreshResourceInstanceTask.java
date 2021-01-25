package com.huawei.dmestore.task;

import com.huawei.dmestore.services.DmeRelationInstanceService;

import org.quartz.JobExecutionContext;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * BackGroundRefreshResourceInstanceTask
 *
 * @ClassName: BackGroundRefreshResourceInstanceTask
 * @Company: GH-CA
 * @author: liuxh
 * @since 2020-11-02
 **/
@Component
public class BackGroundRefreshResourceInstanceTask implements StatefulJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackGroundRefreshResourceInstanceTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LOGGER.info("refreshResourceInstance start");
        long consume = 0L;
        try {
            Object obj = ApplicationContextHelper.getBean("DmeRelationInstanceServiceImpl");
            long start = System.currentTimeMillis();
            ((DmeRelationInstanceService) obj).refreshResourceInstance();
            long end = System.currentTimeMillis();
            consume = end - start;
        } catch (Exception e) {
            LOGGER.error("refreshResourceInstance error", e);
        }
        LOGGER.info("refreshResourceInstance end comsum:{}ms.", consume);
    }
}

