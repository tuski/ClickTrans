package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.CaptureWindow;
import sample.Main;
import translate.Translator;

import java.net.URL;
import java.util.ResourceBundle;


public class HomePageController implements Initializable {
    @FXML
    private AnchorPane rootStage;

    @FXML
    private AnchorPane topbar;

    @FXML
    private ImageView btnClick,btnTranslate, btnHistory, btnSettings, btnCloseShutter;

    @FXML
    private AnchorPane clickView,translateView, historyView, settingsView;

    @FXML
    private ChoiceBox fromLanguage, toLanguage;

    @FXML
    private AnchorPane history;

    @FXML
    private AnchorPane settings;

    @FXML
    private TextArea sourceText,sourceTextTransView, translatedText, translatedTextTransView;

    @FXML
    private ImageView btnCopySourceText,btnCopyTranslatedText;



    public void captureImage(AnchorPane clickView){
        String languageFrom = fromLanguage.getSelectionModel().getSelectedItem().toString();
        String languageTo = toLanguage.getSelectionModel().getSelectedItem().toString();
        //https://stackoverflow.com/questions/41287372/how-to-take-snapshot-of-selected-area-of-screen-in-javafx
        Stage stage = Main.pStage;
        CaptureWindow window = new CaptureWindow(Screen.getPrimary().getBounds().getWidth(),Screen.getPrimary().getBounds().getHeight(), stage, sourceText, translatedText,languageFrom,languageTo,clickView);
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
        fromLanguage.setValue("Japanese");
        fromLanguage.setItems(sourceLangOptn);
        ObservableList<String> outputLangOptn = FXCollections.observableArrayList("Japanese","English");
        toLanguage.setValue("English");
        toLanguage.setItems(outputLangOptn);



        closeShutter();
    }


    @FXML
    private void handleButtonAction(MouseEvent event){
        if (event.getTarget()== btnClick){
            captureImage(clickView);
            Tooltip t = new Tooltip("Close Shutter");
            Tooltip.install(btnCloseShutter,t);
        }
        if (event.getTarget()== btnTranslate){
            clickView.setVisible(false);
            translateView.setVisible(true);
        }
    }


    @FXML
    private void translate(){
        Translator translator=new Translator();
        try {
            translatedTextTransView.setText(translator.callUrlAndParseResult("ja", "en", sourceTextTransView.getText()));
        } catch (Exception e) {
            e.printStackTrace();
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
