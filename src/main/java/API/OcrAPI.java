package API;

import org.json.JSONArray;
import org.json.JSONObject;
import utility.MultipartUtility;
import translate.Translator;
import utility.ConstantUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OcrAPI {
    private String OCR_API;

    public OcrAPI() {
        this.OCR_API = ConstantUtil.OCR_API;
    }

    public String getOCRtext(boolean isOverlayRequired, File imageUrl, String language) throws Exception {
        try {
            List<String> response = getResponse(isOverlayRequired, imageUrl, language, this.OCR_API);

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
        multipart.addFormField("language", Translator.threeDigitLang.get(language));
        multipart.addFilePart("file", imageUrl);
        List<String> response = multipart.finish();
        return response;
    }
}
