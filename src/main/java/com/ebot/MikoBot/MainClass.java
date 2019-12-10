package com.ebot.MikoBot;

import com.ebot.MikoBot.NOGUI.Console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * DO NOT EXTENDS "Application" as OpenJDK not support it
 * Oracle run normally
 */
public class MainClass {
    public static String PROGRAM_PATH;
    public static Console console;

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
        String[] defaultArgs;
        try {
            defaultArgs = new String[]{System.getenv("BOT_TOKEN"), System.getenv("BOT_MODE")};
            console = new Console(defaultArgs);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            console = new Console(args);
        }
    }
}