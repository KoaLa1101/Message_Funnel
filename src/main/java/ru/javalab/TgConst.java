package ru.javalab;

import com.github.badoualy.telegram.api.TelegramApp;

public class TgConst {
    // Get them from Telegram's console
    public static final int API_ID = 6354041;
    public static final String API_HASH = "b1dc931e13d688e863c82906ad55abbf";

    // What you want to appear in the "all sessions" screen
    public static final String APP_VERSION = "AppVersion";
    public static final String MODEL = "Model";
    public static final String SYSTEM_VERSION = "SysVer";
    public static final String LANG_CODE = "en";

    public static TelegramApp application = new TelegramApp(API_ID, API_HASH, MODEL, SYSTEM_VERSION, APP_VERSION, LANG_CODE);

    // Phone number used for tests
    public static final String PHONE_NUMBER = "+79656235531"; // International format
}
