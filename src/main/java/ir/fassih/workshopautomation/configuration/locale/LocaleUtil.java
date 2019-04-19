package ir.fassih.workshopautomation.configuration.locale;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class LocaleUtil {

    private Properties properties;

    @Autowired
    public LocaleUtil(LocaleProperties properties) {
        log.info("initializing locale util");
        TimeZone.setDefault(TimeZone.getTimeZone(properties.getAppTimeZone()));
        this.properties = new Properties();
        loadAllResources(properties.getPropertiesName()).forEach(this::internalLoad);

    }


    private void internalLoad(InputStreamReader stream) {
        try {
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            log.error("cannot initialized locale", e);
            throw new IllegalStateException("cannot initialized locale", e);
        }
    }


    private List<InputStreamReader> loadAllResources(String path) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
        Resource[] resources;
        try {
            resources = resolver.getResources(path);
        } catch (IOException e) {
            log.error("cannot load resources", e);
            throw new IllegalStateException("cannot load resource", e);
        }
        return Stream.of(resources)
                .map(r -> {
                    try {
                        return new InputStreamReader(r.getInputStream(), Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        log.debug("cannot get inputstream of {}", r.getFilename(), e);
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }


    public String getString(String key) {
        return Optional.ofNullable(this.properties.getProperty(key))
                .orElse(key);
    }

    public String getString(String key , Object ...objects) {
        return String.format(getString(key), objects);
    }

}