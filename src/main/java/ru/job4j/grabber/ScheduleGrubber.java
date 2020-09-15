package ru.job4j.grabber;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class ScheduleGrubber implements Grab {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleGrubber.class);
    private final Properties cfg;

    public ScheduleGrubber(Properties cfg) {
        this.cfg = cfg;
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        int interval = Integer.parseInt(cfg.getProperty("grab.interval-sec"));
        scheduler.start();
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("parse", parse);
        dataMap.put("store", store);
        JobDetail job = newJob(Grab.class)
                .usingJobData(dataMap)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(interval)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class Grab implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) {
            Parse parse = (Parse) jobExecutionContext.getJobDetail().getJobDataMap().get("parse");
            Store store = (Store) jobExecutionContext.getJobDetail().getJobDataMap().get("store");
            List<Post> parseResult = parse.list();
            parseResult.forEach(store::save);
            logger.info("Schedule parse form {} to {} success",
                    parse.getClass().getSimpleName(),
                    store.getClass().getSimpleName());
        }
    }
}
