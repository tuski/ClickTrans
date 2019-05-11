package translate;

import database.ConnectionProvider;
import entity.TranslatedText;
import org.apache.http.client.utils.URLEncodedUtils;
import org.dizitart.no2.*;
import org.dizitart.no2.objects.ObjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class Translator {
    HashMap<String, String> threeDigitLang = new HashMap<String, String>();
    HashMap<String, String> twoDigitLang = new HashMap<String, String>();

    public Translator() {
        threeDigitLang.put("Japanese", "jpn");
        threeDigitLang.put("English", "eng");

        twoDigitLang.put("Japanese", "ja");
        twoDigitLang.put("English", "en");

    }


    public String sendPost(boolean isOverlayRequired, File imageUrl, String language) throws Exception {
        String url = "https://api.ocr.space/parse/image"; // OCR API Endpoints
        StringBuffer responseString = new StringBuffer();
        try {
            MultipartUtility multipart = new MultipartUtility(url, "UTF-8");

            multipart.addFormField("isOverlayRequired", Boolean.toString(isOverlayRequired));
            multipart.addFormField("isCreateSearchablePdf", "false");
            multipart.addFormField("language", threeDigitLang.get(language));
            multipart.addFilePart("file", imageUrl);
            List<String> response = multipart.finish();

            JSONObject obj = new JSONObject(response.get(0));
            JSONArray jsonArray = obj.getJSONArray("ParsedResults");
            JSONObject childJSONObject = jsonArray.getJSONObject(0);

            for (String line : response) {
                responseString.append(line);
            }

            //String ocrTxt =  URLEncoder.encode(childJSONObject.getString("ParsedText"), "UTF-8");
            System.out.println(childJSONObject.getString("ParsedText"));
            return childJSONObject.getString("ParsedText");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Could not translate";
        }

        // return String.valueOf(responseString);
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

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            result = parseResult(response.toString());
            result.trim();

            if (result.equals(" ") || result.isEmpty()) {
                result = "Could not translate. Try changing the language";
            } else {
                Nitrite db = ConnectionProvider.getConnection();
//            NitriteCollection collection = db.getCollection("test");
//            Document doc = Document.createDocument("from",word).put("to",result);
//            collection.insert(doc);
                System.out.println("entered=" + word + " result= " + result);

                ObjectRepository<TranslatedText> repository = db.getRepository(TranslatedText.class);
                repository.insert(new TranslatedText(word, result));

//            Cursor cursor = collection.find(FindOptions.sort("id",SortOrder.Descending));
//            for (Document document : cursor) {
//                System.out.println(document.get("from"));
//                System.out.println(document.get("to"));
//            }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    private static String parseResult(String inputJson) throws Exception {
        JSONArray jsonArray = new JSONArray(inputJson);
        JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);

        String result = "";
        for (int i = 0; i < jsonArray2.length(); i++) {
            JSONArray jsonArray3 = (JSONArray) jsonArray2.get(i);
            result = result + " " + jsonArray3.get(0).toString();
        }

        return result;
    }


}
