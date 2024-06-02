package com.cs35.common.utils.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cs35.common.config.CS35Config;
import com.cs35.common.constant.Constants;
import com.cs35.common.utils.StringUtils;
import com.cs35.common.utils.http.HttpUtils;

/**
 * 
 * 
 * @author cs35
 */
public class AddressUtils
{
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip)
    {
        // 
        if (IpUtils.internalIp(ip))
        {
            return "IP";
        }
        if (CS35Config.isAddressEnabled())
        {
            try
            {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
                if (StringUtils.isEmpty(rspStr))
                {
                    log.error(" {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSON.parseObject(rspStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            }
            catch (Exception e)
            {
                log.error(" {}", ip);
            }
        }
        return UNKNOWN;
    }
}
