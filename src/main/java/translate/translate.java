package translate;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


public class translate {

	public static void main(String[] args) {
		
		String apiKey="f3d225a70988957";
		boolean isOverlayRequired=true;
		String imageUrl="https://ibin.co/4ZQgi0FOtwTo.jpg";
		String language="jpn";

		//OCRAsyncTask asyncTask = new OCRAsyncTask(apiKey, isOverlayRequired, imageUrl, language);
		try {
			File file= new File("c:\\Capture.jpg");
			
			//System.out.println(asyncTask.sendPost(apiKey, isOverlayRequired, imageUrl, language));
			System.out.println(sendPost(false, file, "jpn"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			System.out.println(callUrlAndParseResult("ja","en","見積もり"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	// public static String googleTranslateApi(String text) {
	// String returnString = "";
	//
	// try {
	// String textEncoded=URLEncoder.encode(text, "utf-8");
	// textEncoded=text;
	// System.out.println(textEncoded);
	// String url =
	// "https://translate.googleapis.com/translate_a/single?client=gtx&sl=ja&tl=en&dt=t&q="
	// + textEncoded;
	// HttpClient httpclient = new DefaultHttpClient();
	// HttpResponse response = httpclient.execute(new HttpGet(url));
	// StatusLine statusLine = response.getStatusLine();
	// if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	// ByteArrayOutputStream out = new ByteArrayOutputStream();
	// response.getEntity().writeTo(out);
	// String responseString = out.toString();
	// out.close();
	//
	// String aJsonString = responseString;
	// aJsonString = aJsonString.replace("[", "");
	// aJsonString = aJsonString.replace("]", "");
	// aJsonString = aJsonString.substring(1);
	// int plusIndex = aJsonString.indexOf('"');
	// aJsonString = aJsonString.substring(0, plusIndex);
	//
	// returnString = aJsonString;
	// } else{
	// response.getEntity().getContent().close();
	// throw new IOException(statusLine.getReasonPhrase());
	// }
	// } catch(Exception e) {
	// returnString = e.getMessage();
	// }
	//
	// return returnString;
	// }
	//
	 
	  
	 private static String callUrlAndParseResult(String langFrom, String langTo,
              String word) throws Exception 
{

String url = "https://translate.googleapis.com/translate_a/single?"+
"client=gtx&"+
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

return parseResult(response.toString());
}

private static String parseResult(String inputJson) throws Exception
{
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

static String url = "https://api.ocr.space/parse/image"; // OCR API Endpoints
private static String sendPost(boolean isOverlayRequired, File imageUrl, String language) throws Exception {

    StringBuffer responseString = new StringBuffer();
    try {
        MultipartUtility multipart = new MultipartUtility(url, "UTF-8");

        multipart.addFormField("isOverlayRequired", Boolean.toString(isOverlayRequired));
        multipart.addFormField("isCreateSearchablePdf","true");
        //multipart.addFormField("isSearchablePdfHideTextLayer ","true");
        multipart.addFilePart("file", imageUrl);
        List<String> response = multipart.finish();

        for (String line : response) {
            responseString.append(line);
        }
    } catch (IOException ex) {
       // Log.v("OCR Exception",ex.getMessage());
    }

    //return result
    return String.valueOf(responseString);
}

}