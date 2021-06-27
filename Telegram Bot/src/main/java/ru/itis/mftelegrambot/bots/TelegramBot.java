package ru.itis.mftelegrambot.bots;

import org.apache.log4j.Level;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import org.apache.log4j.Logger;
import ru.itis.mfbotsapi.bots.Bot;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;

public class TelegramBot extends TelegramLongPollingBot implements Bot {
    private Logger logger;

    private String name;
    private String token;

    private String usernameTG;

    public TelegramBot(String name, String token) {
        this.name = name;
        this.token = token;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.getMessage().isReply()){
            String message = update.getMessage().getText();
            usernameTG = update.getMessage().getFrom().getUserName();
            sendMsg(update.getMessage().getChatId().toString(), message, update.getMessage().getMessageId(), usernameTG, update.getMessage().getReplyToMessage().getMessageId());
        } else {
            String message = update.getMessage().getText();
            usernameTG = update.getMessage().getFrom().getUserName();
            sendMsg(update.getMessage().getChatId().toString(), message, update.getMessage().getMessageId(), usernameTG, -1);
        }
    }

    @Override
    public void start() throws StartBotException {
        logger = Logger.getLogger(this.getClass().getName());
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            logger.log(Level.INFO ,"...Telegram bot was started...^_^...");

        } catch (TelegramApiException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new StartBotException("Telegram bot cannot starts. " + e.getMessage());
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void init() {

    }

    public synchronized void sendMsg(String chatId, String s, int mesId, String usernameTG, int repMesId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(mesId);
        sendMessage.setText("Message from tg " + "\n"+ s + "\n" + "by " + usernameTG + "\n repMesId: " + repMesId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.log(Level.ERROR, e.toString());
        }
    }
}
