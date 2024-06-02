package com.cs35.framework.datasource;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *
 * 
 * @author cs35
 */
public class DynamicDataSource extends AbstractRoutingDataSource
{
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources)
    {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey()
    {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}