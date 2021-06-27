package ru.itis.mfbotsapi.api.client;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.itis.mfbotsapi.api.exceptions.*;
import ru.itis.mfbotsapi.api.protocol.TCPFrame;
import ru.itis.mfbotsapi.api.protocol.TCPFrameFactory;
import ru.itis.mfbotsapi.api.utils.ClientKeyManager;
import ru.itis.mfbotsapi.api.utils.SlaveBotEntry;
import ru.itis.mfbotsapi.bots.MasterBot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
@SuperBuilder
@Slf4j
public class ManagementClient extends AbstractClient{

    protected Set<SlaveBotEntry> slavesSet;
    protected MasterBot bot;

    public static ManagementClient init(ClientKeyManager keyManager, TCPFrameFactory tcpFrameFactory, MasterBot bot){
        return ManagementClient.builder()
                .keyManager(keyManager)
                .tcpFrameFactory(tcpFrameFactory)
                .slavesSet(Collections.newSetFromMap(new ConcurrentHashMap<>()))
                .bot(bot)
                .build();
    }

    @Override
    public void connect(InetSocketAddress serverAddress, String connectionToken) throws ClientException {
        Runnable connect = () -> {
            try {
                isWork = true;
                selector = Selector.open();

                SocketChannel slaveSocketChannel = SocketChannel.open(serverAddress);
                log.info("Установлено соединение с сервером.");

                UUID infoFrameId = UUID.randomUUID();
                TCPFrame clientInfoTCPFrame = tcpFrameFactory.createTCPFrame(1, infoFrameId, connectionToken);
                tcpFrameFactory.writeTCPFrame(slaveSocketChannel, clientInfoTCPFrame);

                TCPFrame serverResponseFrame = tcpFrameFactory.readTCPFrame(slaveSocketChannel);

                if ((serverResponseFrame != null) && (serverResponseFrame.getType() == 2)) {

                    slaveSocketChannel.configureBlocking(false);
                    slaveSocketChannel.register(selector, SelectionKey.OP_READ);

                    slavesSet.add(SlaveBotEntry.builder()
                            .token(connectionToken)
                            .messenger((SlaveBotEntry.Messenger) serverResponseFrame.getContent()[1])
                            .botName((String) serverResponseFrame.getContent()[2])
                            .socketChannel(slaveSocketChannel)
                            .build());

                    log.info("Успешно настроено соединение с сервером.");

                    while (isWork) {
                        selector.select();
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();

                            if (key.isReadable()) {
                                keyManager.read(this, key);
                            }
                            if (key.isWritable()) {
                                keyManager.write(this);
                            }
                            iterator.remove();
                        }
                    }
                } else {
                    slaveSocketChannel.close();
                    log.warn("Сервер отвечает неправильным протоколом");
                }
            } catch (UnresolvedAddressException ex) {
                log.warn("Неправильный адрес, привет");
                throw new ClientException("Unknown address", ex);
            } catch (IOException ex) {
                log.warn("Невозможно установить соединение с сервером: ошибка соединения.");
                throw new ClientException("Cannot connect client", ex);
            }
            catch (KeyManagerException | TCPFrameFactoryException ex){
                log.warn("Ошибка работы с сервером: ошибка при обмене данными.");
                throw new ClientException(ex.getMessage(), ex);
            } catch (IncorrectFCSException ex) {
                //TODO reaction
            } catch (ClientDisconnectException ex) {
                ex.getSelectionKey().cancel();
                try{
                    if (slavesSet!=null){
                        for (SlaveBotEntry slaveBotEntry: slavesSet) {
                            if (slaveBotEntry.getSocketChannel().equals(ex.getSelectionKey().channel())){
                                slavesSet.remove(slaveBotEntry);
                            }
                        }
                    }
                    log.info("Client " + ex.getSelectionKey().channel() + " was disconnected");
                    selector.close();
                    log.info("Разрыв соединения с сервером.");
                } catch (IOException exx){
                    //ignore
                }
                throw new ClientException(ex);
            } catch (ClosedSelectorException ex){
                log.info("Клиент был отключен.");
            }
        };
        Thread connectThread = new Thread(connect);
        connectThread.setDaemon(true);
        connectThread.start();
    }

    @Override
    public void sendTCPFrame(String token, TCPFrame tcpFrame) throws ClientException {
        try {
            for (SlaveBotEntry entry : slavesSet){
                if (entry.getToken().equals(token)){
                    tcpFrameFactory.writeTCPFrame(entry.getSocketChannel(), tcpFrame);
                }
            }
        } catch (TCPFrameFactoryException ex) {
            throw new ClientException(ex.getMessage(), ex);
        }
    }

    @Override
    public void disconnect(String targetToken) {
        isWork = false;
        try{
            for (SlaveBotEntry entry : slavesSet){
                if (entry.getToken().equals(targetToken)){
                    entry.getSocketChannel().close();
                }
            }
        } catch (IOException|NullPointerException ex){
            //ignore
        }
    }
}
