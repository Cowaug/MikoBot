package com.ebot.MikoBot;

import com.ebot.MikoBot.Ultils.JawMySQL;

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
                    ttsBot = new BotInstance(args[0], BotInstance.TTS, "VN");

                } catch (Exception ex) {
                    ttsBot = new BotInstance(System.getenv("TTS_BOT_TOKEN"), BotInstance.TTS, System.getenv("REGION"));
                }
            }).start();

        if (musicBot == null)
            new Thread(() -> {
                try {
                    musicBot = new BotInstance(args[1], BotInstance.MUSIC, "VN");
                } catch (Exception ex) {
                    musicBot = new BotInstance(System.getenv("MUSIC_BOT_TOKEN"), BotInstance.MUSIC, System.getenv("REGION"));
                }
            }).start();
    }
}