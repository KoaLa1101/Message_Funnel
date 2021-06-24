package ru.javalab.bots;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.log4j.Level;
import ru.javalab.bots.events.ReplyEvent;

import org.apache.log4j.Logger;
import ru.javalab.exceptions.StartBotException;

public class DiscordBot implements Bot {
    private JDA bot;

    private String token;

    private Logger logger;

    public DiscordBot(String token) {
        this.token = token;
    }

    @Override
    public void start() throws StartBotException{
        logger = Logger.getLogger(this.getClass());
        try {
            bot = JDABuilder.createDefault(token).build();
        } catch (LoginException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new StartBotException("Discord bot cannot starts. " + e.getMessage());
        }
        addEventListeners();
        logger.log(Level.INFO, "Discord bot was started");
    }


    private void addEventListeners() {
        bot.addEventListener(new ReplyEvent());
    }



}
