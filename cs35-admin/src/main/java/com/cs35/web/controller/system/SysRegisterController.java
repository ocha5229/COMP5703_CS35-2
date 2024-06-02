package com.cs35.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.domain.model.RegisterBody;
import com.cs35.common.utils.StringUtils;
import com.cs35.framework.web.service.SysRegisterService;

/**
 * Registration verification
 *
 * @author cs35
 */
@RestController
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;


    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user) {
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
