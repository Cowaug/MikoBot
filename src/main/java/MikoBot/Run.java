package MikoBot;

import MikoBot.GUI.Console;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Run extends Application {
    public static String PROGRAM_PATH;

    static {
        try {
            PROGRAM_PATH = URLDecoder.decode(Run.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            PROGRAM_PATH = PROGRAM_PATH.substring(1, PROGRAM_PATH.lastIndexOf("/"));
            //System.out.println(PATH);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Run.class.getResource("/fxml/StartUi.fxml"));
        Parent content = loader.load();

        Scene scene = new Scene(content);

        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UTILITY);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Program entry point
     *
     * @param args Token, Functionality of the Bot
     */
    public static void main(String[] args) {
        new Console(args);
        //launch(args);
    }
}