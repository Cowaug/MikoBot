package com.ebot.MikoBot;

import com.ebot.MikoBot.NOGUI.Console;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 *  DO NOT EXTENDS "Application" as OpenJDK not support it
 *  Oracle run normally
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
        console = new Console(args);
    }
}