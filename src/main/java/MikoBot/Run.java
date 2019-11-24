package MikoBot;

import MikoBot.GUI.Console;
import MikoBot.MediaPlayer.AudioPlayerSendHandler;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;

import javax.security.auth.login.LoginException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Run {
    public static String PATH;

    public static final String TTS = "TTS";

    public static final String TTS_PREFIX = ".";
    public static final String MEDIA_PREFIX = "/";

    public static JDA jda;


    public static Console console;

    public static String[] arg;

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
        Console console = new Console();
    }
}


