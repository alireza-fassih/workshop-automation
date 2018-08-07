package ir.fassih.workshopautomation.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContext {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}