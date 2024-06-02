package com.cs35.framework.manager;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.cs35.common.utils.Threads;
import com.cs35.common.utils.spring.SpringUtils;

/**
 * 
 * 
 * @author cs35
 */
public class AsyncManager
{
    /**
     * 10
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * 
     */
    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    /**
     * 
     */
    private AsyncManager(){}

    private static AsyncManager me = new AsyncManager();

    public static AsyncManager me()
    {
        return me;
    }

    /**
     * 
     * 
     * @param task 
     */
    public void execute(TimerTask task)
    {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 
     */
    public void shutdown()
    {
        Threads.shutdownAndAwaitTermination(executor);
    }
}
