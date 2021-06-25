package ru.itis.mfdiscordbot;

import lombok.extern.slf4j.Slf4j;
import ru.itis.mfbotsapi.bots.Bot;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mfdiscordbot.utils.PropertiesConstants;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

@Slf4j
public class DiscordBotApp {

    protected static DiscordBot discordBot;

    public static void main(String[] args) {

        PropertiesLoader propertiesLoader = PropertiesLoader.getInstance();

        try {
            discordBot = new DiscordBot(propertiesLoader.getProperty(PropertiesConstants.DISCORD_BOT_TOKEN));
            discordBot.init();
        } catch (StartBotException e) {
            log.error(e.getMessage());
        }
    }

    public static boolean isBotActive(){
        return discordBot.isActive();
    }

    public static void startBot(){
        discordBot.start();
    }

    public static void stopBot(){
        discordBot.stop();
    }
}