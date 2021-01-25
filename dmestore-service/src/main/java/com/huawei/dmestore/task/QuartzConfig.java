package com.huawei.dmestore.task;

import com.huawei.vmware.util.VmwarePluginContextFactory;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuartzConfig
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class QuartzConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzConfig.class);
    private Scheduler scheduler;

    /**
     * getScheduler
     *
     * @return Scheduler
     */
    public Scheduler getScheduler() {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        try {
            if (null == scheduler) {
                scheduler = stdSchedulerFactory.getScheduler();
            }
            scheduler.start();
        } catch (SchedulerException e) {
            LOGGER.error("get scheduler error", e);
        }

        return scheduler;
    }

    /**
     * destory
     */
    public void destory() {
        try {
            scheduler.shutdown();
            VmwarePluginContextFactory.closeAll();
        } catch (SchedulerException e) {
            LOGGER.error("shutdown scheduler error", e);
        }
    }
}
