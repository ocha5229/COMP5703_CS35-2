package com.cs35;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Initiating program
 * 
 * @author cs35
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class CS35Application
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(CS35Application.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Startup success   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
