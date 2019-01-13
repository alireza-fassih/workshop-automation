package ir.fassih.workshopautomation.configuration.locale;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "workshop.locale")
public class LocaleProperties {


    private String propertiesName = "classpath*:/*-locale-msg.properties";

    private String appTimeZone = "Asia/Tehran";

}