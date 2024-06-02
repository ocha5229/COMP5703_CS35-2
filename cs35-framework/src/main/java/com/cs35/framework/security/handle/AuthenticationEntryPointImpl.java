package com.cs35.framework.security.handle;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.cs35.common.constant.HttpStatus;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.utils.ServletUtils;
import com.cs35.common.utils.StringUtils;

/**
 *
 * 
 * @author cs35
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable
{
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException
    {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StringUtils.format("：{}，，", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }
}
