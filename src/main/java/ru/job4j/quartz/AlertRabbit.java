package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        Properties config = new Properties();
        InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties");
        try {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Class.forName(config.getProperty("driver-class-name"));
            try (Connection conn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            )) {
                schedulerRun(config, conn);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private static void schedulerRun(Properties config, Connection conn) {
        try {
            int interval = Integer.parseInt(config.getProperty("rabbit.interval", "10"));
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", conn);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection conn = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            String query = "INSERT INTO rabbit(created) VALUES (?)";
            try {
                PreparedStatement stat = conn.prepareStatement(query);
                stat.setObject(1, LocalDate.now());
                stat.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}