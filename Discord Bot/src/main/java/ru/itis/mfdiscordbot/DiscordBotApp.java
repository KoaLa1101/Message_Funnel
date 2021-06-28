package ru.itis.mfdiscordbot;

import lombok.extern.slf4j.Slf4j;
import ru.itis.mfbotsapi.api.client.ManagementClient;
import ru.itis.mfbotsapi.api.protocol.TCPFrameFactoryImpl;
import ru.itis.mfbotsapi.api.utils.ManagementClientKeyManager;
import ru.itis.mfbotsapi.api.utils.Reply;
import ru.itis.mfbotsapi.api.utils.SlaveBotEntry;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class DiscordBotApp {

    protected static DiscordBot discordBot;
    protected static ManagementClient managementClient;

    public static void main(String[] args) {

        PropertiesLoader propertiesLoader = PropertiesLoader.getInstance();

        try {
            discordBot = new DiscordBot(propertiesLoader.getProperty("discord.bot.token"));
            managementClient = ManagementClient.init(new ManagementClientKeyManager(),
                    new TCPFrameFactoryImpl((byte) 0XCC, (byte) 0xDD, 2048, 64, 0),
                    discordBot
            );
            discordBot.init();
        } catch (StartBotException e) {
            log.error(e.getMessage());
        }
    }

    public static void replyOnMessage(String token, Reply reply){
        managementClient.sendTCPFrame(token, managementClient.getTcpFrameFactory()
                .createTCPFrame(4, UUID.randomUUID().toString(),
                        reply.getUserId(), reply.getMessage()));
    }

    public static void handleNewConfig(List<String> tokens){
        for (SlaveBotEntry entry : managementClient.getSlavesSet()){
            if (!tokens.contains(entry.getToken())){
                managementClient.disconnect(entry.getToken());
            }
        }
        List<String> currentTokens = managementClient.getSlavesSet().stream()
                .map(SlaveBotEntry::getToken)
                .collect(Collectors.toList());
        for (String token : tokens){
            if (!currentTokens.contains(token)){
                managementClient.connect(getAddressByToken(token), token);
            }
        }
    }

    protected static InetSocketAddress getAddressByToken(String token){
        return new InetSocketAddress("localhost", Integer.parseInt(token.substring(token.length() - 5)));
    }
}