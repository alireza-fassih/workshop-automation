package ir.fassih.workshopautomation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class WorkshopAutomationApplication {

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    public static void main(String[] args) {
        SpringApplication.run(WorkshopAutomationApplication.class, args);
    }
}
