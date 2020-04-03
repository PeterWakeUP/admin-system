package com.evsoft.modules.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpringScheduleTask {

    protected final Logger logger = LoggerFactory.getLogger(SpringScheduleTask.class);

    @Scheduled(cron = "*/3 * * * * ?")
    public void runfirst(){
        logger.info("SpringScheduleTask：cron = */3 * * * * ?");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("cron end");
    }

    @Scheduled(fixedRate = 1000 * 3) //上一次执行完10s后再执行
    public void runsecend(){
        logger.info("SpringScheduleTask：fixedRate = 1000 * 3");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("fixedRate end");
    }
}
