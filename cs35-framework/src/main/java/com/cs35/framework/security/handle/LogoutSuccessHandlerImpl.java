package com.cs35.framework.security.handle;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson2.JSON;
import com.cs35.common.constant.Constants;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.domain.model.LoginUser;
import com.cs35.common.utils.MessageUtils;
import com.cs35.common.utils.ServletUtils;
import com.cs35.common.utils.StringUtils;
import com.cs35.framework.manager.AsyncManager;
import com.cs35.framework.manager.factory.AsyncFactory;
import com.cs35.framework.web.service.TokenService;

/**
 *  
 * 
 * @author cs35
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 
            tokenService.delLoginUser(loginUser.getToken());
            // 
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}
