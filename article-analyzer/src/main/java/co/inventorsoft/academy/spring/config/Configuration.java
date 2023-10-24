package co.inventorsoft.academy.spring.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;

/**
 * Configuration file to explicitly specify configuration beans.
 */

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public Gson gson() {
        return new GsonBuilder().create();
    }


}
