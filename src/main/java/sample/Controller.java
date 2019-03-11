package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.event.MouseAdapter;

import static sample.CaptureWindow.parsedText;
import static sample.CaptureWindow.transText;

public class Controller  {
    @FXML
    private Text sourceText;

    @FXML
    private Text translatedText;

    @FXML
    private ChoiceBox<?> fromLanguage;

    @FXML
    private ChoiceBox<?> toLanguage;


    @FXML
    public void captureImage(){

        //https://stackoverflow.com/questions/41287372/how-to-take-snapshot-of-selected-area-of-screen-in-javafx
        Stage stage = Main.pStage;
        CaptureWindow window = new CaptureWindow(Screen.getPrimary().getBounds().getWidth(),Screen.getPrimary().getBounds().getHeight(), stage, sourceText, translatedText);
        window.show();
        System.out.println("value = "+parsedText+"trans= "+transText);
        sourceText.setText(parsedText);
        translatedText.setText(transText);
    }

    @FXML
    public void closeWindow(){
        Stage stage = Main.pStage;
        stage.close();
    }

}
