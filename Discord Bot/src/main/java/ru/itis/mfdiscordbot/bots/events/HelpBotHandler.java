package ru.itis.mfdiscordbot.bots.events;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

public class HelpBotHandler extends ListenerAdapter {

    protected List<String> helpCommands;
    protected DiscordBot bot;

    public HelpBotHandler(DiscordBot bot){
        helpCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.help-commands").split(", "));
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (helpCommands.contains(event.getMessage().getContentRaw().toLowerCase())){
            event.getChannel().sendMessage("(/start | /старт) - запустить бота\n" +
                    "(/stop | /стоп) - приостановить работу бота\n" +
                    "(/config | /конфиг) [add | remove] TOKEN - сконфигурировать зависимых ботов\n" +
                    "\t*(add | добавить) - добавить нового зависимого бота\n" +
                    "\t*(remove | удалить) - удалить текущего зависимого бота" +
                    "\tTOKEN - уникальное значение токена, определяющего бота.")
                    .queue();
        }
    }

}
