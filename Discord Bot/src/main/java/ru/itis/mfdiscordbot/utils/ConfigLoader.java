package ru.itis.mfdiscordbot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import ru.itis.mfdiscordbot.config.BotConfig;

@Slf4j
public class ConfigLoader {

    public static BotConfig[] getBotConfigs(Message.Attachment attachment) {
        try {
            return getBotConfigs(attachment.downloadToFile(createFile(false).toString()).get());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Cannot get config. " + e.getMessage());
        }
        return new BotConfig[0];
    }

    public static BotConfig[] getBotConfigs() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(new File(createFile(true).toString()), BotConfig[].class);
        } catch (MismatchedInputException e) {
            //ignore
        } catch (IOException e) {
            log.error("Cannot get config. " + e.getMessage());
        }
        return new BotConfig[0];
    }

    private static BotConfig[] getBotConfigs(File configFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(configFile, BotConfig[].class);
        } catch (IOException e) {
            log.error("Cannot get config. " + e.getMessage());
        }
        return new BotConfig[0];
    }

    private static Path createFile(boolean isBotStarting) {
        String directory = PropertiesLoader.getInstance().getProperty("directory.default");
        String fileName = PropertiesLoader.getInstance().getProperty("file.config.name");
        Path filePath = Paths.get(directory + fileName);
        try {
            try {
                Files.createDirectory(Paths.get(directory));
            } catch (FileAlreadyExistsException e) {
                //ignore
            }
            if (Files.exists(filePath) && !isBotStarting) {
                Files.delete(filePath);
            }
            Files.createFile(filePath);
        }  catch (IOException e) {
            log.error("Cannot create " + fileName + " file. " + e.getMessage());
            //TODO throw exception
        }

        return filePath;
    }
}
