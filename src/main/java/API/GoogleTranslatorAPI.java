package API;

import database.DBConnectionProvider;
import entity.TranslatedText;
import javafx.scene.control.Alert;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.json.JSONArray;
import translate.Translator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GoogleTranslatorAPI {
    public GoogleTranslatorAPI() {
    }


    public String callUrlAndParseResult(String langFrom, String langTo, String word) {
        word = word.trim();
        String result = null;
        try {
            String url = "https://translate.googleapis.com/translate_a/single?" +
                    "client=gtx&" +
                    "sl=" + Translator.twoDigitLang.get(langFrom) +
                    "&tl=" + Translator.twoDigitLang.get(langTo) +
                    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = inputBuffer.readLine()) != null) {
                response.append(inputLine);
            }
            inputBuffer.close();

            result = parseResult(response.toString());

            if (result == null || result.equals(" ") || result.isEmpty()) {
                result = "Could not translate. Try changing the language";
            } else {
                Nitrite db = DBConnectionProvider.getConnection();
                ObjectRepository<TranslatedText> repository = db.getRepository(TranslatedText.class);
                repository.insert(new TranslatedText(word, result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String parseResult(String inputJson) {
        try {
            JSONArray jsonObject = new JSONArray(inputJson);
            JSONArray translatedArray = (JSONArray) jsonObject.get(0);

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < translatedArray.length(); i++) {
                JSONArray translatedTextArray = (JSONArray) translatedArray.get(i);
                result.append(" ").append(translatedTextArray.get(0).toString());
            }

            return result.toString();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not translate.");
            alert.setContentText("Is your language selection correct? Also, try zooming the text.");
            alert.showAndWait();
            System.out.println(e.getMessage());
        }
        return null;
    }
}
