package ru.itis.mfdiscordbot.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesLoader {
    private static final String FILE_NAME = "app.properties";

    private static PropertiesLoader instance;
    private Properties properties;


    private PropertiesLoader() {
        properties = new Properties();
        File file;
        try {
            file = new File(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(FILE_NAME),StandardCharsets.UTF_8);
            properties.load(inputStreamReader);
//            logger.log(Level.DEBUG, "Load properties file");
        } catch (IOException e) {
//            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public static PropertiesLoader getInstance() {
        if (instance == null) {
            instance = new PropertiesLoader();
        }
        return instance;
    }

    public String getProperty(String key) {
//        logger.log(Level.DEBUG, "Get value from key " + key);
        return properties.getProperty(key);
    }
}
