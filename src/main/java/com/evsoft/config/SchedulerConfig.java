package com.evsoft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 该配置类可以使spring自带定时任务多线程并发执行
 * 不开启多线程，则单线程执行，即一个定时任务执行完了，下一个才会执行。所有的定时任务都是在同一个线程池用同一个线程来处理
 *
 */
@Configuration
@EnableScheduling  //开启定时器
public class SchedulerConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //多线程配置
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }

}
