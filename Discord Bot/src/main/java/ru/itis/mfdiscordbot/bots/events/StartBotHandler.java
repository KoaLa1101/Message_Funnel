package ru.itis.mfdiscordbot.bots.events;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.DiscordBotApp;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

import java.util.Arrays;
import java.util.List;

public class StartBotHandler extends ListenerAdapter {

    protected List<String> startCommands;
    protected String welcomeMessage;

    public StartBotHandler(){
        startCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.start-commands").split(", "));
        welcomeMessage = PropertiesLoader.getInstance().getProperty("discord.bot.welcome-message");
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (startCommands.contains(event.getMessage().getContentRaw().toLowerCase())){
            if (!DiscordBotApp.isBotActive()){
                start();
                event.getChannel().sendMessage(welcomeMessage).queue();
            } else {
                event.getChannel().sendMessage("Бот уже работает").queue();
            }
        }
    }

    protected void start(){
        DiscordBotApp.startBot();
    }
}
