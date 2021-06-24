package ru.javalab.bots;

import org.apache.log4j.Level;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import org.apache.log4j.Logger;
import ru.javalab.exceptions.StartBotException;

public class TelegramBot extends TelegramLongPollingBot implements Bot {
    private Logger logger;

    private String name;
    private String token;

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
        String message = update.getMessage().getText();
        sendMsg(update.getMessage().getChatId().toString(), message);
    }

    @Override
    public void start() throws StartBotException{
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

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.log(Level.ERROR, e.toString());
        }
    }
}
