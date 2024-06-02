package com.cs35.framework.web.domain.server;

import java.lang.management.ManagementFactory;
import com.cs35.common.utils.Arith;
import com.cs35.common.utils.DateUtils;

/**
 * JVM
 * 
 * @author cs35
 */
public class Jvm
{
    /**
     * JVM(M)
     */
    private double total;

    /**
     * JVM(M)
     */
    private double max;

    /**
     * JVM(M)
     */
    private double free;

    /**
     * JDK
     */
    private String version;

    /**
     * JDK
     */
    private String home;

    public double getTotal()
    {
        return Arith.div(total, (1024 * 1024), 2);
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public double getMax()
    {
        return Arith.div(max, (1024 * 1024), 2);
    }

    public void setMax(double max)
    {
        this.max = max;
    }

    public double getFree()
    {
        return Arith.div(free, (1024 * 1024), 2);
    }

    public void setFree(double free)
    {
        this.free = free;
    }

    public double getUsed()
    {
        return Arith.div(total - free, (1024 * 1024), 2);
    }

    public double getUsage()
    {
        return Arith.mul(Arith.div(total - free, total, 4), 100);
    }

    /**
     * JDK
     */
    public String getName()
    {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getHome()
    {
        return home;
    }

    public void setHome(String home)
    {
        this.home = home;
    }

    /**
     * JDK
     */
    public String getStartTime()
    {
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.getServerStartDate());
    }

    /**
     * JDK
     */
    public String getRunTime()
    {
        return DateUtils.timeDistance(DateUtils.getNowDate(), DateUtils.getServerStartDate());
    }

    /**
     *
     */
    public String getInputArgs()
    {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }
}
