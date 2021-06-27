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

    //     (@Test_Bot)Телеграм:( @naserik)Erik - пошел оотсюдава
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message referencedMessage = event.getMessage().getReferencedMessage();
        if (referencedMessage != null) {
            String text = referencedMessage.getContentRaw();
            int first = text.indexOf(':') + 2;
            int second = first + 2;
            while(text.charAt(second) != ')') {
                second++;
            }
            bot.sendReply(text.substring(first+1, second) , event.getMessage().getContentRaw());
        } else {
            if (!event.getMessage().getAuthor().isBot() && !event.getMessage().getContentRaw().startsWith("/")){
                event.getMessage().getChannel().sendMessage("Не знаем кому отправлять").queue();
            }
        }

    }

}
