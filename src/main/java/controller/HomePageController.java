package controller;

import API.GoogleTranslatorAPI;
import database.DBConnectionProvider;
import entity.TranslatedText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.SortOrder;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import main.CaptureWindow;
import main.Main;
import utility.PropertiesFile;
import utility.Toast;
import utility.Util;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class HomePageController implements Initializable {
    @FXML
    private AnchorPane rootStage, topbar;

    @FXML
    private ImageView btnClick, btnTranslate, btnHistory, btnSettings, btnCloseShutter;

    @FXML
    private AnchorPane clickView, translateView, historyView, settingsView;

    @FXML
    private ChoiceBox fromLanguage, toLanguage;

    @FXML
    private AnchorPane history;

    @FXML
    private AnchorPane settings;

    @FXML
    private TextArea sourceText, sourceTextTransView, translatedText, translatedTextTransView;

    @FXML
    private ImageView btnCopySourceText, btnCopyTranslatedText;

    @FXML
    private TableView<TranslatedText> historyTable;

    @FXML
    private final TableColumn<TranslatedText, String> fromText = new TableColumn<>("Source Text");

    @FXML
    private final TableColumn<TranslatedText, String> toText = new TableColumn<>("Translated Text");


    public void captureImage(AnchorPane clickView) {
        String languageFrom = fromLanguage.getSelectionModel().getSelectedItem().toString();
        String languageTo = toLanguage.getSelectionModel().getSelectedItem().toString();

        int xNow = 0, yNow = 0;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        for (GraphicsDevice curGs : gs) {
            DisplayMode mode = curGs.getDisplayMode();
            xNow += mode.getWidth();
            yNow = mode.getHeight();
        }

        Stage stage = Main.pStage;
        CaptureWindow window = new CaptureWindow(xNow, yNow, stage, sourceText, translatedText, languageFrom, languageTo, clickView);
        window.show();
    }

    @FXML
    public void closeWindow() {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PropertiesFile prop = new PropertiesFile();
        List<String> languageSupport = Arrays.asList("Arabic","Chinese","English", "French", "German", "Japanese",
                                                        "korean","Russian","Spanish","Swedish", "Turkish");
        //Load initial language
        ObservableList<String> sourceLangOptn = FXCollections.observableArrayList(languageSupport);
        fromLanguage.setValue(prop.getProperty("fromLanguage"));
        fromLanguage.setItems(sourceLangOptn);
        ObservableList<String> outputLangOptn = FXCollections.observableArrayList(languageSupport);
        toLanguage.setValue(prop.getProperty("toLanguage"));
        toLanguage.setItems(outputLangOptn);

        fromLanguage.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    prop.setProperty("fromLanguage", newValue.toString());
                    System.out.println(newValue);
                });

        toLanguage.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    prop.setProperty("toLanguage", newValue.toString());
                    System.out.println(newValue);
                });

        fromText.setCellValueFactory(new PropertyValueFactory<>("fromText"));
        toText.setCellValueFactory(new PropertyValueFactory<>("toText"));
        fromText.setCellFactory(TextFieldTableCell.forTableColumn());
        toText.setCellFactory(TextFieldTableCell.forTableColumn());
        fromText.prefWidthProperty().bind(historyTable.widthProperty().divide(2));
        fromText.textProperty();
        toText.prefWidthProperty().bind(historyTable.widthProperty().divide(2));
        historyTable.getColumns().addAll(fromText, toText);

        Tooltip tip = new Tooltip("Close Shutter");
        Tooltip.install(btnCloseShutter, tip);
        closeShutter();
    }


    @FXML
    private void handleButtonAction(MouseEvent event) {
        if (event.getTarget() == btnClick) {
            captureImage(clickView);
            clickView.setVisible(false);
            translateView.setVisible(false);
            historyView.setVisible(false);
            settingsView.setVisible(false);
        }
        if (event.getTarget() == btnTranslate) {
            clickView.setVisible(false);
            historyView.setVisible(false);
            translateView.setVisible(true);
            settingsView.setVisible(false);
        }
        if (event.getTarget() == btnHistory) {
            populateTable();
            clickView.setVisible(false);
            translateView.setVisible(false);
            settingsView.setVisible(false);
            historyView.setVisible(true);

        }
        if (event.getTarget() == btnSettings) {
            clickView.setVisible(false);
            translateView.setVisible(false);
            historyView.setVisible(false);
            settingsView.setVisible(true);
        }
    }


    @FXML
    private void translate() {
        String languageFrom = fromLanguage.getSelectionModel().getSelectedItem().toString();
        String languageTo = toLanguage.getSelectionModel().getSelectedItem().toString();
        try {
            GoogleTranslatorAPI googleTranslatorAPI = new GoogleTranslatorAPI();
            translatedTextTransView.setText(googleTranslatorAPI.callUrlAndParseResult(languageFrom, languageTo, sourceTextTransView.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateTable() {
        ObservableList<TranslatedText> list = FXCollections.observableArrayList();
        Nitrite db = DBConnectionProvider.getConnection();
        ObjectRepository<TranslatedText> repository = db.getRepository(TranslatedText.class);

        Cursor<TranslatedText> cursor = repository.find(FindOptions.sort("_id", SortOrder.Descending));
        for (TranslatedText translatedText : cursor) {
            list.add(translatedText);
        }

        historyTable.setItems(list);
    }

    @FXML
    private void closeShutter() {
        clickView.setVisible(false);
        translateView.setVisible(false);
        historyView.setVisible(false);
        settingsView.setVisible(false);
    }

    @FXML
    private void exchangeLanguage() {
        String languageFrom = fromLanguage.getSelectionModel().getSelectedItem().toString();
        String languageTo = toLanguage.getSelectionModel().getSelectedItem().toString();
        fromLanguage.setValue(languageTo);
        toLanguage.setValue(languageFrom);
    }

    @FXML
    private void clickSrcTxtCopy() {
        String txt = sourceText.getText();
        if (!txt.isEmpty()) {
            Util.copyToClipboard(txt);
            Toast.makeText(Main.pStage, "Text Copied", 1000, 500, 500);
        }

    }

    @FXML
    private void clickTransTxtCopy() {
        String txt = translatedText.getText();
        if (!txt.isEmpty()) {
            Util.copyToClipboard(translatedText.getText());
            Toast.makeText(Main.pStage, "Text Copied", 1000, 500, 500);
        }
    }

    @FXML
    private void transSrcTxtCopy() {
        String txt = sourceTextTransView.getText();
        if (!txt.isEmpty()) {
            Util.copyToClipboard(sourceTextTransView.getText());
            Toast.makeText(Main.pStage, "Text Copied", 1000, 500, 500);
        }
    }

    @FXML
    private void transTransTxtCopy() {
        String txt = translatedTextTransView.getText();
        if (!txt.isEmpty()) {
            Util.copyToClipboard(translatedTextTransView.getText());
            Toast.makeText(Main.pStage, "Text Copied", 1000, 500, 500);
        }
    }


}
