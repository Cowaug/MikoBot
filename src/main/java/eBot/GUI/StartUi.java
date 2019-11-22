package eBot.GUI;

import com.jfoenix.controls.JFXTextArea;
import eBot.MediaManager;
import javafx.fxml.FXML;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartUi {
    @FXML
    static JFXTextArea token;

    public void initialize(){

    }

    public static void setToken(String message){
        token.setText(message);
    }
}
