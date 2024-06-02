package com.cs35.common.utils;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * 
 * @author cs35
 */
public class Threads
{
    private static final Logger logger = LoggerFactory.getLogger(Threads.class);

    /**
     * sleep,
     */
    public static void sleep(long milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e)
        {
            return;
        }
    }

    /**
     *
     * shutdown, .
     * , shutdownNow, workQueuePending,.
     * ，.
     * shutdown.
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool)
    {
        if (pool != null && !pool.isShutdown())
        {
            pool.shutdown();
            try
            {
                if (!pool.awaitTermination(120, TimeUnit.SECONDS))
                {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(120, TimeUnit.SECONDS))
                    {
                        logger.info("Pool did not terminate");
                    }
                }
            }
            catch (InterruptedException ie)
            {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     *
     */
    public static void printException(Runnable r, Throwable t)
    {
        if (t == null && r instanceof Future<?>)
        {
            try
            {
                Future<?> future = (Future<?>) r;
                if (future.isDone())
                {
                    future.get();
                }
            }
            catch (CancellationException ce)
            {
                t = ce;
            }
            catch (ExecutionException ee)
            {
                t = ee.getCause();
            }
            catch (InterruptedException ie)
            {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null)
        {
            logger.error(t.getMessage(), t);
        }
    }
}
