package ru.javalab;


import org.apache.log4j.Level;
import ru.javalab.bots.Bot;
import ru.javalab.bots.DiscordBot;
import ru.javalab.bots.TelegramBot;
import ru.javalab.exceptions.StartBotException;
import ru.javalab.utils.PropertiesConstants;
import ru.javalab.utils.PropertiesLoader;

import org.apache.log4j.Logger;

public class App {
    public static void main(String[] args) {
        PropertiesLoader propertiesLoader = PropertiesLoader.getInstance();
        Logger logger = Logger.getLogger(App.class);

        try {
            Bot discordBot = new DiscordBot(propertiesLoader.getProperty(PropertiesConstants.DISCORD_BOT_TOKEN));
            discordBot.start();
        } catch (StartBotException e) {
            logger.log(Level.ERROR, e.getMessage());
        }

        try {
            Bot telegramBot = new TelegramBot(propertiesLoader.getProperty(PropertiesConstants.TELEGRAM_BOT_USERNAME),propertiesLoader.getProperty(PropertiesConstants.TELEGRAM_BOT_TOKEN));
            telegramBot.start();
        } catch (StartBotException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }
}