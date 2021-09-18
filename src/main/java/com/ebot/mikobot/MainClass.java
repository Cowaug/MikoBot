package com.ebot.mikobot;

import com.ebot.mikobot.bots.models.BotInstance;
import com.ebot.mikobot.ultils.JawMySQL;

import static com.ebot.mikobot.bots.models.BotType.*;

/**
 * DO NOT EXTENDS "Application" as OpenJDK not support it
 * Oracle run normally
 */
public class MainClass {
    private static BotInstance musicBot=null;
    private static BotInstance ttsBot=null;

    /**
     * Program's entry point
     *
     * @param args Token, Functionality of the Bot
     */
    public static void main(String[] args) {
        JawMySQL.init();
        if (ttsBot == null)
            new Thread(() -> {
                try {
                    ttsBot = new BotInstance(args[0], TTS, "VN");

                } catch (Exception ex) {
                    ttsBot = new BotInstance(System.getenv("TTS_BOT_TOKEN"), TTS, System.getenv("REGION"));
                }
            }).start();

        if (musicBot == null)
            new Thread(() -> {
                try {
                    musicBot = new BotInstance(args[1], MUSIC, "VN");
                } catch (Exception ex) {
                    musicBot = new BotInstance(System.getenv("MUSIC_BOT_TOKEN"), MUSIC, System.getenv("REGION"));
                }
            }).start();
    }
}