package com.huawei.dmestore.task;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.huawei.dmestore.dao.ScheduleDao;
import com.huawei.dmestore.entity.ScheduleConfig;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.Scheduler;

/**
 * @author lianq
 * @className ScheduleSettingTest
 * @description TODO
 * @date 2020/12/2 19:27
 */
public class ScheduleSettingTest {

    @Mock
    private ScheduleDao scheduleDao;


    @Mock
    private QuartzConfig quartzConfig;
    private static List<ScheduleConfig> scheduleList;
    ScheduleConfig scheduleConfig;

    @InjectMocks
    ScheduleSetting scheduleSetting = new ScheduleSetting();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        scheduleList = new ArrayList<>();
        scheduleConfig = spy(ScheduleConfig.class);
        scheduleConfig.setId(21);
        scheduleConfig.setClassName("ScheduleSetting");
        scheduleList.add(scheduleConfig);
    }

    @Test
    public void reconfigureTasks() {
        when(scheduleDao.getScheduleList()).thenReturn(scheduleList);
        Scheduler scheduler = spy(Scheduler.class);
        when(quartzConfig.getScheduler()).thenReturn(scheduler);
        scheduleSetting.reconfigureTasks();
    }

    @Test
    public void refreshTasks() {
        scheduleSetting.refreshTasks(21, "321");
    }

    @Test
    public void lowerFirstCapse() {
        scheduleSetting.lowerFirstCapse("321");
    }
}