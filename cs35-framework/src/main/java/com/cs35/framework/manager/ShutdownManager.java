package com.cs35.framework.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;

/**
 * 
 *
 * @author cs35
 */
@Component
public class ShutdownManager
{
    private static final Logger logger = LoggerFactory.getLogger("sys-user");

    @PreDestroy
    public void destroy()
    {
        shutdownAsyncManager();
    }

    /**
     * 
     */
    private void shutdownAsyncManager()
    {
        try
        {
            logger.info("========");
            AsyncManager.me().shutdown();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
}
