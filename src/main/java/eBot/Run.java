package eBot;

import eBot.GUI.Console;
import eBot.GUI.StartUi;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;

import javax.security.auth.login.LoginException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Run {
    public static String PATH;
    public static final String MUSIC = "Music";
    public static final String TTS = "TTS";

    public static final String TTS_PREFIX = ".";
    public static final String MEDIA_PREFIX = "/";

    public static String MODE = TTS;


    public static Console console;

    static {
        try {
            PATH = URLDecoder.decode(Run.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            PATH = PATH.substring(1, PATH.lastIndexOf("/"));
            //System.out.println(PATH);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(Run.class.getResource("/fxml/StartUi.fxml"));
//        Parent content = loader.load();
//
//        Scene scene = new Scene(content);
//
//        primaryStage.setResizable(false);
//        primaryStage.initStyle(StageStyle.UTILITY);
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }

    /**
     * Program entry point
     *
     * @param args Token, Functionality of the Bot
     */
    public static void main(String[] args) {
        //        launch(args);
        if (args.length == 0) return;
        if (args.length == 2 && args[1].equals("music")) MODE = MUSIC;
        try {
            JDA jda = new JDABuilder(AccountType.BOT).setToken(args[0])
                    .setBulkDeleteSplittingEnabled(false)
                    .setCompression(Compression.NONE)
                    .setActivity(Activity.playing(MODE.equals(MUSIC) ? "Music" : "TTS"))
                    .build();
            jda.addEventListener(new MessageListener());
            if (args.length == 2 && args[1].equals("music")) MODE = MUSIC;
        } catch (LoginException e) {
            e.printStackTrace();
        } finally {
            console = new Console(MODE);
        }
    }

}


