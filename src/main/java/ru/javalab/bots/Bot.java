package ru.javalab.bots;

import ru.javalab.exceptions.StartBotException;

public interface Bot {
    void start() throws StartBotException;
}
