package ru.itis.mftelegrambot;

import lombok.extern.slf4j.Slf4j;
import ru.itis.mfbotsapi.bots.Bot;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mftelegrambot.utils.PropertiesConstants;
import ru.itis.mftelegrambot.utils.PropertiesLoader;
import ru.itis.mftelegrambot.bots.TelegramBot;

@Slf4j
public class TelegramBotApp {
    public static void main(String[] args) {

        PropertiesLoader propertiesLoader = PropertiesLoader.getInstance();

        try {
            Bot telegramBot = new TelegramBot(propertiesLoader.getProperty(PropertiesConstants.TELEGRAM_BOT_USERNAME),propertiesLoader.getProperty(PropertiesConstants.TELEGRAM_BOT_TOKEN));
            telegramBot.start();
        } catch (StartBotException e) {
            log.error(e.getMessage());
        }
    }
}
