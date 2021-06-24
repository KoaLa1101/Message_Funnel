package ru.javalab.exceptions;

public class StartBotException extends Exception {
    public StartBotException() {
    }

    public StartBotException(String message) {
        super(message);
    }

    public StartBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public StartBotException(Throwable cause) {
        super(cause);
    }

    public StartBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
