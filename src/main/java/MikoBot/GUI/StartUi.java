package MikoBot.GUI;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;

public class StartUi {
    @FXML
    static JFXTextArea token;

    public void initialize(){

    }

    public static void setToken(String message){
        token.setText(message);
    }
}
