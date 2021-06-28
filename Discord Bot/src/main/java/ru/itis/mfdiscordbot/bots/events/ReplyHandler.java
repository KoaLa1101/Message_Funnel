package ru.itis.mfdiscordbot.bots.events;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.bots.DiscordBot;

public class ReplyHandler extends ListenerAdapter {

    private DiscordBot bot;

    public ReplyHandler(DiscordBot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message referencedMessage = event.getMessage().getReferencedMessage();
        if (bot.isActive()) {
            if (referencedMessage != null) {
                String text = referencedMessage.getContentRaw();
                int startId = text.indexOf(':') + 3;
                int endId = startId + 2;
                while(text.charAt(endId) != ' ') {
                    endId++;
                }
                bot.sendReply(text.substring(startId, endId) , event.getMessage().getContentRaw());
            } else {
                if (!event.getMessage().getAuthor().isBot() && !event.getMessage().getContentRaw().startsWith("/")){
                    event.getMessage().getChannel().sendMessage("Не знаем кому отправлять").queue();
                }
            }
        }
    }

}
