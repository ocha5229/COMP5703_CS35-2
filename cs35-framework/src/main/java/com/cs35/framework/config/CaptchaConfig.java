package com.cs35.framework.config;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import static com.google.code.kaptcha.Constants.*;

/**
 * 
 * 
 * @author cs35
 */
@Configuration
public class CaptchaConfig
{
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean()
    {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        //  true yes，no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        //  Color.BLACK
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        //  200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        //  50
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        //  40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "38");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");
        //  5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        //  new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        //  com.google.code.kaptcha.impl.WaterRipple com.google.code.kaptcha.impl.FishEyeGimpy com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath()
    {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        //  true yes，no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        //  Color.BLACK
        properties.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");
        //  Color.BLACK
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        //  200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        //  50
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        //  40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // 
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "com.cs35.framework.config.KaptchaTextCreator");
        //  2
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        //  5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        //  new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        //  Color.BLACK
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // 
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        //  com.google.code.kaptcha.impl.WaterRipple com.google.code.kaptcha.impl.FishEyeGimpy com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
