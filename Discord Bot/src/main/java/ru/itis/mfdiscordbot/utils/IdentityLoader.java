package ru.itis.mfdiscordbot.utils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import ru.itis.mfdiscordbot.config.IdentityConfig;

@Slf4j
public class IdentityLoader {
    private static final String FILE_PATH = "configs/";
    private static final String FILE_NAME = "IdentityConfig.csv";



    public IdentityLoader() {
        createIdentityFile();
    }

    public void write(IdentityConfig config) {
        try (FileWriter writer = new FileWriter(new File(FILE_PATH + FILE_NAME), true)) {
            writer.write(config.getId() + ";" + config.getToken() + "\n");
        } catch (IOException e) {
            log.error("Cannot write in IdentityFile. " + e.getMessage());
        }

    }

    public IdentityConfig read(String id) {
        try (FileReader reader = new FileReader(new File(FILE_PATH + FILE_NAME));
             BufferedReader br=new BufferedReader(reader)) {
            String line;
            while((line = br.readLine()) != null) {
                String[] arrayLine = line.split(";");
                if (line.split(";")[0].equals(id)) {
                    return new IdentityConfig(arrayLine[0], arrayLine[1]);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("File doesn't exists. " +  e.getMessage());
        } catch (IOException e){
            log.error("Cannot read Identity file. " + e.getMessage());
        }
        return null;
    }

    public List<IdentityConfig> readAll() {
        List<IdentityConfig> configs = new ArrayList<>();
        try (FileReader reader = new FileReader(new File(FILE_PATH + FILE_NAME));
             BufferedReader br=new BufferedReader(reader)) {
            String line;
            while((line = br.readLine()) != null) {
                String[] arrayLine = line.split(";");
                configs.add(new IdentityConfig(arrayLine[0], arrayLine[1]));
            }
        } catch (FileNotFoundException e) {
            log.error("File doesn't exists. " +  e.getMessage());
        } catch (IOException e){
            log.error("Cannot read Identity file. " + e.getMessage());
        }
        return configs;
    }

    private void createIdentityFile() {
        try {
            Files.createDirectory(Paths.get(FILE_PATH));

        }  catch (FileAlreadyExistsException e) {
            //ignore
        } catch (IOException e) {
            log.error("Cannot create folder. " + e.getMessage());
        }
        try {
            if (!Files.exists(Paths.get(FILE_PATH + FILE_NAME))) {
                Files.createFile(Paths.get(FILE_PATH + FILE_NAME));
            }
        } catch (IOException e) {
            log.error("Cannot create identity file. " + e.getMessage());
        }
    }

    public void open() {

    }

    public void close() {

    }
}
