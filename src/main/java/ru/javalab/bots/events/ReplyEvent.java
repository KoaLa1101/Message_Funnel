package ru.javalab.bots.events;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReplyEvent extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message message = event.getMessage().getReferencedMessage();

        if(message != null) {
            System.out.println(message.getContentRaw());
            //TODO send message to server
        } else {
            event.getMessage().getChannel().sendMessage("Не знаем кому отправлять").queue();
        }

    }
}
