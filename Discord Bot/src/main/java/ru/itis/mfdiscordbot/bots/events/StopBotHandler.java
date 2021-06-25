package ru.itis.mfdiscordbot.bots.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.DiscordBotApp;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class StopBotHandler extends ListenerAdapter {

    protected List<String> stopCommands;

    public StopBotHandler(){
        stopCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.stop-commands").split(", "));
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (stopCommands.contains(event.getMessage().getContentRaw().toLowerCase())){
            if (DiscordBotApp.isBotActive()){
                stop();
                event.getChannel().sendMessage("Бот приостановлен.").queue();
            } else {
                event.getChannel().sendMessage("Бот уже приостановлен.").queue();
            }
        }
    }

    protected void stop(){
        DiscordBotApp.stopBot();
    }

}
