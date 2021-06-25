package ru.itis.mfdiscordbot.bots;

import javax.security.auth.login.LoginException;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import ru.itis.mfbotsapi.bots.Bot;
import ru.itis.mfbotsapi.bots.exceptions.InitBotException;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mfbotsapi.bots.exceptions.StopBotException;
import ru.itis.mfdiscordbot.bots.events.*;

@Slf4j
public class DiscordBot implements Bot {

    private JDA bot;
    private boolean isActive;
    private String token;

    public DiscordBot(String token) {
        this.token = token;
    }

    @Override
    public void init() throws InitBotException {
        try {
            bot = JDABuilder.createDefault(token).build();
        } catch (LoginException e) {
            log.error(e.getMessage());
            throw new StartBotException("Discord bot cannot starts. " + e.getMessage());
        }
        addEventListeners();
        log.info("Discord bot was started");
    }

    @Override
    public void start() throws StartBotException {
        setActive(true);
    }

    @Override
    public void stop() throws StopBotException {
        setActive(false);
    }


    private void addEventListeners() {
        bot.addEventListener(
                new StartBotHandler(),
                new StopBotHandler(),
                new HelpBotHandler(),
                new ReplyEvent(),
                new ConfigBotHandler()
                );
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
