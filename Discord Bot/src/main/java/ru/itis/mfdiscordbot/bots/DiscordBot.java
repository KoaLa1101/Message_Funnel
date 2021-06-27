package ru.itis.mfdiscordbot.bots;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.security.auth.login.LoginException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import ru.itis.mfbotsapi.api.utils.Message;
import ru.itis.mfbotsapi.api.utils.Reply;
import ru.itis.mfbotsapi.bots.MasterBot;
import ru.itis.mfbotsapi.bots.exceptions.InitBotException;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mfbotsapi.bots.exceptions.StopBotException;
import ru.itis.mfdiscordbot.DiscordBotApp;
import ru.itis.mfdiscordbot.bots.events.*;
import ru.itis.mfdiscordbot.config.BotConfig;
import ru.itis.mfdiscordbot.config.IdentityConfig;
import ru.itis.mfdiscordbot.utils.ConfigLoader;
import ru.itis.mfdiscordbot.utils.IdentityLoader;

@Slf4j
public class DiscordBot implements MasterBot {

    private JDA bot;
    private boolean isActive;
    private String token;
    private List<IdentityConfig> identities;
    private MessageChannel mainMessageChannel;
    private IdentityLoader identityLoader;

    public DiscordBot(String token) {
        this.token = token;
    }

    @Override
    public void init() throws InitBotException {
        try {
            bot = JDABuilder.createDefault(token).build();
            identityLoader = new IdentityLoader();
            isActive = false;
        } catch (LoginException e) {
            log.error(e.getMessage());
            throw new StartBotException("Discord bot cannot starts. " + e.getMessage());
        }
        addEventListeners();
        log.info("Discord bot was initialized");
    }

    @Override
    public void start() throws StartBotException {
        connect();
        setActive(true);
    }

    @Override
    public void stop() throws StopBotException {
        setActive(false);
    }


    private void addEventListeners() {
        bot.addEventListener(
                new StartBotHandler(this),
                new StopBotHandler(this),
                new HelpBotHandler(this),
                new ReplyHandler(this),
                new ConfigBotHandler(this)
                );
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private void connect() {
        connect(ConfigLoader.getBotConfigs());
    }

    public void connect(BotConfig[] configs) {
        List<String> tokens = new ArrayList<>();

        for (int i = 0; i < configs.length; i++) {
            tokens.add(configs[i].getToken());
        }
        DiscordBotApp.handleNewConfig(tokens);

        identities = identityLoader.readAll();
    }

    public void sendReply(String userId, String message) {
        for (IdentityConfig config: identities) {
            if (config.getId().equals(userId)) {
                DiscordBotApp.replyOnMessage(config.getToken(), Reply.builder().userId(userId).message(message).build());
                return;
            }
        }
    }


    @Override
    public void sendMessage(Message message) {
        IdentityConfig newIdentity = new IdentityConfig(message.getUserId(), message.getToken());

        boolean isContains = false;
        for (IdentityConfig identity: identities) {
            if(identity.getId().equals(message.getUserId())) {
                isContains = true;
                break;
            }
        }

        if (!isContains) {
            identities.add(newIdentity);
            identityLoader.write(newIdentity);
        }
        //     (@Test_Bot)Телеграм: (@naserik)Erik - пошел отсюдава

        StringBuilder text = new StringBuilder("(" + message.getBotName() + ")");

        switch (message.getMessenger()) {
            case TELEGRAM:
                text.append("Телеграм: ");
        }
        text
                .append("(")
                .append(message.getUserId())
                .append(")")
                .append(message.getUserNickname())
                .append(" - ")
                .append(message.getText());
        mainMessageChannel.sendMessage(text).queue();
    }

    public void setMessageChannel(MessageChannel messageChannel) {
        this.mainMessageChannel = messageChannel;
    }

    public MessageChannel getMessageChannel() {
        return mainMessageChannel;
    }
}
