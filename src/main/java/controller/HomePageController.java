package controller;

import database.ConnectionProvider;
import entity.TranslatedText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.dizitart.no2.*;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import sample.CaptureWindow;
import sample.Main;
import translate.Translator;
import utility.PropertiesFile;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    private TableView<TranslatedText> historyTable;

    @FXML
    private TableColumn<TranslatedText, String> fromText = new TableColumn<>("Source Text");

    @FXML
    private TableColumn<TranslatedText, String> toText = new TableColumn<>("Translated Text");





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
        PropertiesFile prop= new PropertiesFile();


        //Load initial language
        ObservableList<String> sourceLangOptn = FXCollections.observableArrayList("Japanese","English");
        fromLanguage.setValue(prop.getProperty("fromLanguage"));
        fromLanguage.setItems(sourceLangOptn);
        ObservableList<String> outputLangOptn = FXCollections.observableArrayList("Japanese","English");
        toLanguage.setValue(prop.getProperty("toLanguage"));
        toLanguage.setItems(outputLangOptn);

        fromLanguage.getSelectionModel()
                .selectedItemProperty()
                .addListener( (observable, oldValue, newValue) -> {
                    prop.setProperty("fromLanguage", newValue.toString());
                    System.out.println(newValue);
                });

        toLanguage.getSelectionModel()
                .selectedItemProperty()
                .addListener( (observable, oldValue, newValue) -> {
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
        //fromText.setSortType(TableColumn.SortType.DESCENDING);

        Tooltip t = new Tooltip("Close Shutter");
        Tooltip.install(btnCloseShutter,t);
        closeShutter();
    }




    @FXML
    private void handleButtonAction(MouseEvent event){
        if (event.getTarget()== btnClick){
            captureImage(clickView);
            translateView.setVisible(false);
            historyView.setVisible(false);
            settingsView.setVisible(false);
        }
        if (event.getTarget()== btnTranslate){
            clickView.setVisible(false);
            historyView.setVisible(false);
            translateView.setVisible(true);
            settingsView.setVisible(false);
        }
        if (event.getTarget()== btnHistory){
            populateTable();
            clickView.setVisible(false);
            translateView.setVisible(false);
            settingsView.setVisible(false);
            historyView.setVisible(true);

        }
        if (event.getTarget()== btnSettings){
            clickView.setVisible(false);
            translateView.setVisible(false);
            historyView.setVisible(false);
            settingsView.setVisible(true);
        }



    }


    @FXML
    private void translate(){
        Translator translator=new Translator();
        String languageFrom = fromLanguage.getSelectionModel().getSelectedItem().toString();
        String languageTo = toLanguage.getSelectionModel().getSelectedItem().toString();
        try {
            translatedTextTransView.setText(translator.callUrlAndParseResult(languageFrom, languageTo, sourceTextTransView.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateTable() {
        ObservableList<TranslatedText> list = FXCollections.observableArrayList();;
        Nitrite db = ConnectionProvider.getConnection();
//        NitriteCollection collection = db.getCollection("test");
//
//        Cursor cursor = collection.find();
//
//        for (Document document : cursor) {
//            TranslatedText translatedText = new TranslatedText(document.get("from").toString(),document.get("to").toString());
////            translatedText.setFromText(document.get("from").toString());
////            translatedText.setFromText(document.get("to").toString());
//            translatedTexts.add(translatedText);
//  //
//            list.add(translatedText);
//            System.out.println(document.get("from").toString());
//            System.out.println(document.get("to").toString());
////            System.out.println(translatedText.getFromText());
//        }

        ObjectRepository<TranslatedText> repository = db.getRepository(TranslatedText.class);

        Cursor<TranslatedText> cursor= repository.find(FindOptions.sort("_id", SortOrder.Descending));
        for (TranslatedText translatedText : cursor) {
               list.add(translatedText);
            }


        if (list!=null){
            historyTable.setItems(list);
        }

//        Cursor<Employee> cursor = repository.find(ObjectFilters.gt("age", 30),
//                sort("age", SortOrder.Ascending)
//                        .thenLimit(0, 10));

    }

    @FXML
    private void closeShutter(){
        clickView.setVisible(false);
        translateView.setVisible(false);
        historyView.setVisible(false);
        settingsView.setVisible(false);
    }

    @FXML
    private void exchangeLanguage(){
        String languageFrom = fromLanguage.getSelectionModel().getSelectedItem().toString();
        String languageTo = toLanguage.getSelectionModel().getSelectedItem().toString();
        fromLanguage.setValue(languageTo);
        toLanguage.setValue(languageFrom);
    }

}