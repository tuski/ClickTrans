package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.CaptureWindow;
import sample.Main;

import java.net.URL;
import java.util.ResourceBundle;


public class HomePageController implements Initializable {
    @FXML
    private AnchorPane rootStage;

    @FXML
    private AnchorPane topbar;

    @FXML
    private ImageView btnClick,btnTranslate, btnHistory, btnSettings;

    @FXML
    private AnchorPane clickView,translateView, historyView, settingsView;

    @FXML
    private ChoiceBox fromLanguage, toLanguage;

    @FXML
    private AnchorPane history;

    @FXML
    private AnchorPane settings;

    @FXML
    private TextArea sourceText;

    @FXML
    private TextArea translatedText;

    @FXML
    private ImageView btnCopySourceText;

    @FXML
    private ImageView btnCopyTranslatedText;




    public void captureImage(){
            String languageFrom = fromLanguage.getSelectionModel().getSelectedItem().toString();
        String languageTo = toLanguage.getSelectionModel().getSelectedItem().toString();
        //https://stackoverflow.com/questions/41287372/how-to-take-snapshot-of-selected-area-of-screen-in-javafx
        Stage stage = Main.pStage;
        CaptureWindow window = new CaptureWindow(Screen.getPrimary().getBounds().getWidth(),Screen.getPrimary().getBounds().getHeight(), stage, sourceText, translatedText,languageFrom,languageTo);
        window.show();
    }

    @FXML
    public void closeWindow(){
        Stage stage = Main.pStage;
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Load initial language
        ObservableList<String> sourceLangOptn = FXCollections.observableArrayList("Japanese","English");
        fromLanguage.setValue("Japanese"); // this statement shows default value
        fromLanguage.setItems(sourceLangOptn);
        ObservableList<String> outputLangOptn = FXCollections.observableArrayList("Japanese","English");
        toLanguage.setValue("English"); // this statement shows default value
        toLanguage.setItems(outputLangOptn);

        closeShutter();

    }


    @FXML
    private void handleButtonAction(MouseEvent event){
        if (event.getTarget()== btnClick){
            clickView.setVisible(true);
            translateView.setVisible(false);
            captureImage();
        }
        if (event.getTarget()== btnTranslate){
            clickView.setVisible(false);
            translateView.setVisible(true);

        }
    }

    @FXML
    private void closeShutter(){
        clickView.setVisible(false);
        translateView.setVisible(false);
        historyView.setVisible(false);
        settingsView.setVisible(false);
    }



}
