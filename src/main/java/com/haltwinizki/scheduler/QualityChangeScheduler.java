package com.haltwinizki.scheduler;

import com.haltwinizki.service.ProductService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QualityChangeScheduler {

    public void start(ProductService productService) throws SchedulerException {

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        org.quartz.Scheduler scheduler = schedulerFactory.getScheduler();

        // Definition von Details
        JobDetail jobDetail = JobBuilder.newJob(DailyJob.class)
                .withIdentity("dailyJob", "group1")
                .build();

        // Bestimmung der Aufgabenausführungszeit täglich um 7:00 Uhr
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dailyTrigger", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(7, 0))
                .build();

        //Planierung
        scheduler.scheduleJob(jobDetail, trigger);

        // Запустить планировщик
        scheduler.start();
    }
}
