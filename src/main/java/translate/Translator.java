package translate;

import database.DBConnectionProvider;
import entity.TranslatedText;
import javafx.scene.control.Alert;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.ConstantUtil;
import utility.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class Translator {
    HashMap<String, String> threeDigitLang = new HashMap<String, String>();
    HashMap<String, String> twoDigitLang = new HashMap<String, String>();

    public Translator() {
        initComboBox();

    }

    private void initComboBox() {
        threeDigitLang.put("Japanese", "jpn");
        threeDigitLang.put("English", "eng");
        threeDigitLang.put("Arabic", "ara");
        threeDigitLang.put("Chinese", "chs");
        threeDigitLang.put("French", "fre");
        threeDigitLang.put("German", "ger");
        threeDigitLang.put("korean", "kor");
        threeDigitLang.put("Russian", "rus");
        threeDigitLang.put("Spanish", "spa");
        threeDigitLang.put("Swedish ", "swe");
        threeDigitLang.put("Turkish  ", "tur");

        twoDigitLang.put("Japanese", "ja");
        twoDigitLang.put("English", "en");
        twoDigitLang.put("Arabic", "ar");
        twoDigitLang.put("Chinese", "zh");
        twoDigitLang.put("French", "fr");
        twoDigitLang.put("German", "de");
        twoDigitLang.put("korean", "ko");
        twoDigitLang.put("Russian", "ru");
        twoDigitLang.put("Spanish", "es");
        twoDigitLang.put("Swedish", "sv");
        twoDigitLang.put("Turkish", "tr");
    }


    public String sendPost(boolean isOverlayRequired, File imageUrl, String language) throws Exception {
        String url = ConstantUtil.OCR_API; // OCR API Endpoints
        try {
            List<String> response = getResponse(isOverlayRequired, imageUrl, language, url);

            JSONObject obj = new JSONObject(response.get(0));
            JSONArray jsonArray = obj.getJSONArray("ParsedResults");
            JSONObject childJSONObject = jsonArray.getJSONObject(0);

            return childJSONObject.getString("ParsedText");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Could not translate";
        }
    }

    private List<String> getResponse(boolean isOverlayRequired, File imageUrl, String language, String url) throws IOException {
        MultipartUtility multipart = new MultipartUtility(url, "UTF-8");
        multipart.addFormField("isOverlayRequired", Boolean.toString(isOverlayRequired));
        multipart.addFormField("isCreateSearchablePdf", "false");
        multipart.addFormField("language", threeDigitLang.get(language));
        multipart.addFilePart("file", imageUrl);
        List<String> response = multipart.finish();
        return response;
    }


    public String callUrlAndParseResult(String langFrom, String langTo, String word) {
        word = word.trim();
        String url;
        String result = null;
        try {
            url = "https://translate.googleapis.com/translate_a/single?" +
                    "client=gtx&" +
                    "sl=" + twoDigitLang.get(langFrom) +
                    "&tl=" + twoDigitLang.get(langTo) +
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
            result.trim();

            if (result.equals(" ") || result.isEmpty()) {
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
