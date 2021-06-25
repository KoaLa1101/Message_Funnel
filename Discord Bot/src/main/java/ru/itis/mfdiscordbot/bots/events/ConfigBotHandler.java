package ru.itis.mfdiscordbot.bots.events;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.DiscordBotApp;
import ru.itis.mfdiscordbot.bots.commands.ConfigCommand;
import ru.itis.mfdiscordbot.bots.commands.StartCommand;
import ru.itis.mfdiscordbot.bots.commands.StopCommand;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

public class ConfigBotHandler extends ListenerAdapter {

    protected List<String> configCommands;
    protected List<String> addFlags;
    protected List<String> removeFlags;

    public ConfigBotHandler(){
        configCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.config-commands").split(", "));
        addFlags = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.config-add-flags").split(", "));
        removeFlags = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.config-remove-flags").split(", "));
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String[] commandParts = event.getMessage().getContentRaw().split(" ");
        if (configCommands.contains(commandParts[0].toLowerCase())){
            if (DiscordBotApp.isBotActive()){
                configBot(event);
            } else {
                event.getChannel().sendMessage("Бот не запущен. Попробуйте /start, чтобы запустить его.").queue();
            }
        }

    }

    protected void configBot(MessageReceivedEvent event){
        String[] commandParts = event.getMessage().getContentRaw().split(" ");
        try{

            String commandFlag = commandParts[1].toLowerCase();
            String token = commandParts[2];

            if (token == null || token.equals("")){
                event.getChannel().sendMessage("Значение токена является обязательным.").queue();
            }

            if (addFlags.contains(commandFlag)){
                connectNewBot(event, token);
            } else if (removeFlags.contains(commandFlag)){
                disconnectBot(event, token);
            } else {
                event.getChannel().sendMessage("Неверный флаг конфигурации. Используйте /help, чтобы узнать все поддерживаемые.").queue();
            }

        } catch (IndexOutOfBoundsException ex){
            event.getChannel().sendMessage("Неверный формат команды. Используйте /help, чтобы узнать его.").queue();
        }
    }

    protected void connectNewBot(MessageReceivedEvent event, String token){
        System.out.println("Connect with bot with next token: " + token);
    }

    protected void disconnectBot(MessageReceivedEvent event, String token){
        System.out.println("Disconnect with bot with next token: " + token);
    }

}
