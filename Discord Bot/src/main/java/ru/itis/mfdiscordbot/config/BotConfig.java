package ru.itis.mfdiscordbot.config;

public class BotConfig {
    private String token;
    private int port;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "BotConfig{" +
          "token='" + token + '\'' +
          ", port=" + port +
          '}';
    }
}
