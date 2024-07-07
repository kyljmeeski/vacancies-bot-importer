package com.kyljmeeski.vacanciesbot.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    Properties properties = new Properties();
    private final String mode;

    public ApplicationProperties() {
        try {
            InputStream inputStream = ApplicationProperties.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(inputStream);
            if (properties.getProperty("mode") != null && properties.getProperty("mode").equals("component")) {
                mode = "component";
            } else {
                mode = "standalone";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String rabbitMQHost() {
        if (mode.equals("component")) {
            return System.getenv("RABBITMQ_HOST");
        }
        return properties.getProperty("rabbitmq.host");
    }

    public int rabbitMQPort() {
        if (mode.equals("component")) {
            return Integer.parseInt(System.getenv("RABBITMQ_PORT"));
        }
        return Integer.parseInt(properties.getProperty("rabbitmq.port"));
    }

}
