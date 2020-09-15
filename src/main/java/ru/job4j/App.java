package ru.job4j;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.PsqlStore;
import ru.job4j.grabber.ScheduleGrubber;
import ru.job4j.grabber.SqlRuParse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        Properties cfg = new Properties();
        try (InputStream in = App.class.getClassLoader().getResourceAsStream("app.properties")) {
            cfg.load(in);
        } catch (IOException e) {
            logger.error("Properties file load error", e);
            System.exit(-1);
        }
        try (PsqlStore psqlStore = new PsqlStore(cfg)) {
            SqlRuParse sqlRuParse = new SqlRuParse(cfg);
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            ScheduleGrubber grubber = new ScheduleGrubber(cfg);
            grubber.init(sqlRuParse, psqlStore, scheduler);
            Thread.sleep(20000);
            scheduler.shutdown(true);
        } catch (SchedulerException e) {
            logger.error("Scheduler initialization error", e);
        } catch (Exception e) {
            logger.error("PostgreSQL storage initialization failed", e);
        }
    }
}
