package com.cs35.web.controller.system;

import com.cs35.common.config.CS35Config;
import com.cs35.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Home page
 *
 * @author cs35
 */
@RestController
public class SysIndexController {
    /**
     * Basic system configuration
     */
    @Autowired
    private CS35Config cs35Config;

    /**
     * Visit home page, prompts
     */
    @RequestMapping("/")
    public String index() {
        return StringUtils.format("Welcome to the {} background management framework, the current version: v{}, please access through the front-end address.", cs35Config.getName(), cs35Config.getVersion());
    }
}
