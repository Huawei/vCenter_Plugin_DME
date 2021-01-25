package com.huawei.dmestore.task;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.huawei.dmestore.dao.ScheduleDao;
import com.huawei.dmestore.entity.ScheduleConfig;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ScheduleSetting
 *
 * @author Administrator
 * @since 2020-12-08
 */
@Component
public class ScheduleSetting {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleSetting.class);
    private static List<ScheduleConfig> scheduleList;
    private static final int CHARLENGTH = 32;
    @Autowired
    private ScheduleDao scheduleDao;
    @Autowired
    private QuartzConfig quartzConfig;

    /**
     * reconfigureTasks
     */
    public void reconfigureTasks() {
        // 获取所有任务
        scheduleList = scheduleDao.getScheduleList();
        LOGGER.info("schedule size:{}",scheduleList.size());
        for (ScheduleConfig scheduleConfig : scheduleList) {
            try {
                Class clazz;
                clazz = Class.forName(scheduleConfig.getClassName());
                JobDetail job = newJob(clazz)
                    .withIdentity(scheduleConfig.getClassName(), "group1")
                    .build();
                quartzConfig.getScheduler().scheduleJob(job, getTrigger(scheduleConfig));
            } catch (ClassNotFoundException | SchedulerException e) {
                LOGGER.error("job error", e);
            }
        }
    }

    /**
     * 刷新任务
     *
     * @param taskId taskId
     * @param cron cron
     */
    public void refreshTasks(Integer taskId, String cron) {
        // 获取所有任务
        if (scheduleList != null) {
            LOGGER.info("schedule size:{}",scheduleList.size());
            for (ScheduleConfig scheduleConfig : scheduleList) {
                if (scheduleConfig.getId() == taskId) {
                    scheduleConfig.setCron(cron);
                }
            }
        }
        try {
            quartzConfig.getScheduler().clear();
        } catch (SchedulerException e) {
            LOGGER.error("quartzConfig job error", e);
        }
        reconfigureTasks();
    }

    /**
     * 转换首字母小写
     *
     * @param str str
     * @return String
     */
    public static String lowerFirstCapse(String str) {
        char[] chars = str.toCharArray();
        chars[0] += CHARLENGTH;
        return String.valueOf(chars);
    }

    /**
     * Trigger
     *
     * @param scheduleConfig
     * @return
     */
    private CronTrigger getTrigger(ScheduleConfig scheduleConfig) {
        return newTrigger()
            .withIdentity(scheduleConfig.getClassName(), "group1")
            .startNow()
            .withSchedule(cronSchedule(scheduleConfig.getCron()))
            .build();
    }
}
