package com.ebot.MikoBot;



import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * DO NOT EXTENDS "Application" as OpenJDK not support it
 * Oracle run normally
 */
public class MainClass {
    public static String PROGRAM_PATH;
    private static BotInstance musicBot;
    private static BotInstance ttsBot;

    static {
        try {
            PROGRAM_PATH = URLDecoder.decode(MainClass.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            PROGRAM_PATH = PROGRAM_PATH.substring(1, PROGRAM_PATH.lastIndexOf("/"));
            //System.out.println(PATH);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Program entry point
     *
     * @param args Token, Functionality of the Bot
     */
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                ttsBot = new BotInstance(args[0],BotInstance.TTS,"VN");
            }catch (Exception ex){
                ttsBot = new BotInstance(System.getenv("TTS_BOT_TOKEN"),BotInstance.TTS,System.getenv("REGION"));
            }
        }).start();

        new Thread(() -> {
            try {
                musicBot = new BotInstance(args[1],BotInstance.MUSIC,"VN");
            }catch (Exception ex){
                musicBot = new BotInstance(System.getenv("MUSIC_BOT_TOKEN"),BotInstance.MUSIC,System.getenv("REGION"));
            }
        }).start();
    }

    public static void reboot(String mode){
        if(mode.equals(BotInstance.TTS))
            ttsBot.restart();
        else if(mode.equals(BotInstance.MUSIC))
            musicBot.restart();
    }
}