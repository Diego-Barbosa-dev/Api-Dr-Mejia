package com.drmejia.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class GlobalConfig {
    private String country;
    private String version;
    private String name;




    @Override
    public String toString(){
        return "ConfigParameters{" +
        "name='" + name + '\'' +
        ", country='" + country + '\'' +
        ", version= '" + version + '\'' + 
        "}";
    }
}
