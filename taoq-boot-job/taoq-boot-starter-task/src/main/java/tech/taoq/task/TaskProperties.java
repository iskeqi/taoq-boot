package tech.taoq.task;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WebProperties
 *
 * @author keqi
 */
@Component
@ConfigurationProperties(prefix = "taoq.task")
public class TaskProperties {

    /**
     * 线程池核心线程数
     */
    private Integer poolSize = 5;

    /**
     * 程序停止时，是否等待线程池内的任务执行完成再关闭
     */
    private Boolean waitForTasksToCompleteOnShutdown = true;

    /**
     * 最多等待 5 秒后，就关闭线程池
     */
    private Integer awaitTerminationMillis = 5000;

    /**
     * 取消任务后，是否在线程池中自动移除该任务
     */
    private Boolean removeOnCancelPolicy = true;

    /**
     * 线程池内线程名称前缀
     */
    private String threadNamePrefix = "spirng-task-";

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public Boolean getWaitForTasksToCompleteOnShutdown() {
        return waitForTasksToCompleteOnShutdown;
    }

    public void setWaitForTasksToCompleteOnShutdown(Boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    public Integer getAwaitTerminationMillis() {
        return awaitTerminationMillis;
    }

    public void setAwaitTerminationMillis(Integer awaitTerminationMillis) {
        this.awaitTerminationMillis = awaitTerminationMillis;
    }

    public Boolean getRemoveOnCancelPolicy() {
        return removeOnCancelPolicy;
    }

    public void setRemoveOnCancelPolicy(Boolean removeOnCancelPolicy) {
        this.removeOnCancelPolicy = removeOnCancelPolicy;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }
}
