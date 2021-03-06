package tech.taoq.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * ScheduleConfigurer
 *
 * @author keqi
 */
@Configuration
@EnableScheduling
public class ScheduleConfigurer implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(ScheduleConfigurer.class);

    @Autowired
    private TaskProperties taskProperties;

    @Bean
    public ThreadPoolTaskScheduler myThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(taskProperties.getPoolSize());
        // 如果有任务在执行，等到任务执行完毕再关闭线程池
        scheduler.setWaitForTasksToCompleteOnShutdown(taskProperties.getWaitForTasksToCompleteOnShutdown());
        // 如果 x 秒后还有未完成的任务，也强制关闭
        scheduler.setAwaitTerminationMillis(taskProperties.getAwaitTerminationMillis());
        // 任务取消后，任务将被删除
        scheduler.setRemoveOnCancelPolicy(taskProperties.getRemoveOnCancelPolicy());
        scheduler.setThreadNamePrefix(taskProperties.getThreadNamePrefix());
        scheduler.setErrorHandler(t -> log.error("an error occurred in the spirng task thread pool", t));
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(myThreadPoolTaskScheduler());
    }
}
