package com.cs35.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author cs35
 */
@Component
@ConfigurationProperties(prefix = "cs35")
public class CS35Config
{
    /**  */
    private String name;

    /**  */
    private String version;

    /**  */
    private String copyrightYear;

    /**  */
    private static String profile;

    /**  */
    private static boolean addressEnabled;

    /**  */
    private static String captchaType;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        CS35Config.profile = profile;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        CS35Config.addressEnabled = addressEnabled;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        CS35Config.captchaType = captchaType;
    }

    /**
     * 
     */
    public static String getImportPath()
    {
        return getProfile() + "/import";
    }

    /**
     * 
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
