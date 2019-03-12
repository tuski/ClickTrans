package translate;

import org.json.JSONArray;
import org.json.JSONObject;

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
    HashMap<String,String> threeDigitLang=new HashMap<String, String>();
    HashMap<String,String> twoDigitLang=new HashMap<String, String>();

    public Translator() {
        threeDigitLang.put("Japanese","jpn");
        threeDigitLang.put("English","eng");

        twoDigitLang.put("Japanese","ja");
        twoDigitLang.put("English","en");
    }


    public String sendPost(boolean isOverlayRequired, File imageUrl, String language) throws Exception {
        String url = "https://api.ocr.space/parse/image"; // OCR API Endpoints
        StringBuffer responseString = new StringBuffer();
        try {
            MultipartUtility multipart = new MultipartUtility(url, "UTF-8");

            multipart.addFormField("isOverlayRequired", Boolean.toString(isOverlayRequired));
            multipart.addFormField("isCreateSearchablePdf", "false");
            multipart.addFormField("language", language);
            multipart.addFilePart("file", imageUrl);
            List<String> response = multipart.finish();

            JSONObject obj = new JSONObject(response.get(0));
            JSONArray jsonArray = obj.getJSONArray("ParsedResults");
            JSONObject childJSONObject = jsonArray.getJSONObject(0);

            for (String line : response) {
                responseString.append(line);
            }

            return childJSONObject.getString("ParsedText");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Could not translate";
        }

       // return String.valueOf(responseString);
    }


    public String callUrlAndParseResult(String langFrom, String langTo, String word) throws Exception {

        String url = "https://translate.googleapis.com/translate_a/single?" +
                "client=gtx&" +
                "sl=" + langFrom +
                "&tl=" + langTo +
                "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        return parseResult(response.toString());
    }

    private static String parseResult(String inputJson) throws Exception {
        /*
         * inputJson for word 'hello' translated to language Hindi from English-
         * [[["नमस्ते","hello",,,1]],,"en"]
         * We have to get 'नमस्ते ' from this json.
         */

        JSONArray jsonArray = new JSONArray(inputJson);
        JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
        JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);

        return jsonArray3.get(0).toString();
    }



}
