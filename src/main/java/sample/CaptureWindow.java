package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.json.JSONArray;
import org.json.JSONObject;
import translate.MultipartUtility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Is used to capture an area of the screen.
 *
 * @author GOXR3PLUS
 */
public class CaptureWindow extends Stage {

    /**
     * The border pane.
     */
    // BorderPane and Canvas
    BorderPane borderPane = new BorderPane();
    Text sourceText;
    Text translatedText;
   static String parsedText;
   static String transText;
    /**
     * The canvas.
     */
    Canvas canvas = new Canvas();

    /**
     * The gc.
     */
    GraphicsContext gc = canvas.getGraphicsContext2D();

    /**
     * The stage.
     */
    Stage stage;

    /**
     * The width.
     */
    // Variables
    int width;

    /**
     * The height.
     */
    int height;

    /**
     * The x pressed.
     */
    int xPressed = 0;

    /**
     * The y pressed.
     */
    int yPressed = 0;

    /**
     * The x now.
     */
    int xNow = 0;

    /**
     * The y now.
     */
    int yNow = 0;

    /**
     * The foreground.
     */
    Color foreground = Color.rgb(255, 167, 0);

    /**
     * The background.
     */
    Color background = Color.rgb(0, 0, 0, 0.3);

    /**
     * Constructor.
     *  @param screenWidth  the screen width
     * @param screenHeight the screen height
     * @param primary      the primary
     * @param sourceText
     * @param translatedText
     */
    public CaptureWindow(double screenWidth, double screenHeight, Stage primary, Text sourceText, Text translatedText) {
        stage = primary;
        this.sourceText=sourceText;
        this.translatedText=translatedText;

        setX(0);
        setY(0);
        setWidth(screenWidth);
        setHeight(screenHeight);
        initOwner(primary);
        initStyle(StageStyle.TRANSPARENT);
        setAlwaysOnTop(true);

        // BorderPane
        borderPane.setStyle("-fx-background-color:rgb(0,0,0,0.1);");

        // Canvas
        canvas.setWidth(screenWidth);
        canvas.setHeight(screenHeight);
        canvas.setOnMousePressed(m -> {
            xPressed = (int) m.getScreenX();
            yPressed = (int) m.getScreenY();
        });
        canvas.setOnMouseDragged(m -> {
            xNow = (int) m.getScreenX();
            yNow = (int) m.getScreenY();
            repaintCanvas();
        });

        borderPane.setCenter(canvas);

        // Scene
        setScene(new Scene(borderPane, Color.TRANSPARENT));
        getScene().setCursor(Cursor.CROSSHAIR);
        getScene().setOnKeyReleased(key -> {
            if (key.getCode() == KeyCode.B) {
                close();
                System.out.println("Key Released....");


                ////////////
                int[] ints = calculatedRectangle();
                Rectangle screen= new Rectangle(ints[0],ints[1],ints[2],ints[3]);

        try {
            BufferedImage screenCapture = new Robot().createScreenCapture(screen);

            File imageFile = new File("c:\\Users\\0568\\Pictures\\out.jpg");
            ImageIO.write(screenCapture,"jpg", imageFile);

            sendPost(false,imageFile,"jpn");

        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


            } else if (key.getCode() == KeyCode.ESCAPE)
                close();
        });

        // gc
        gc.setLineDashes(6);
        gc.setFont(Font.font("null", FontWeight.BOLD, 14));
    }

    /**
     * Repaints the canvas *.
     */
    protected void repaintCanvas() {

        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setStroke(foreground);
        gc.setFill(background);
        gc.setLineWidth(3);

        if (xNow > xPressed && yNow > yPressed) { // Right and Down

            calculateWidthAndHeight(xNow - xPressed, yNow - yPressed);
            gc.strokeRect(xPressed, yPressed, width, height);
            gc.fillRect(xPressed, yPressed, width, height);

        } else if (xNow < xPressed && yNow < yPressed) { // Left and Up

            calculateWidthAndHeight(xPressed - xNow, yPressed - yNow);
            gc.strokeRect(xNow, yNow, width, height);
            gc.fillRect(xNow, yNow, width, height);

        } else if (xNow > xPressed && yNow < yPressed) { // Right and Up

            calculateWidthAndHeight(xNow - xPressed, yPressed - yNow);
            gc.strokeRect(xPressed, yNow, width, height);
            gc.fillRect(xPressed, yNow, width, height);

        } else if (xNow < xPressed && yNow > yPressed) { // Left and Down

            calculateWidthAndHeight(xPressed - xNow, yNow - yPressed);
            gc.strokeRect(xNow, yPressed, width, height);
            gc.fillRect(xNow, yPressed, width, height);
        }

    }

    /**
     * Show the window.
     */
    public void showWindow() {
        xNow = 0;
        yNow = 0;
        xPressed = 0;
        yPressed = 0;
        repaintCanvas();
        show();
    }

    /**
     * Calculates the width and height of the rectangle.
     *
     * @param w the w
     * @param h the h
     */
    private final void calculateWidthAndHeight(int w, int h) {
        width = w;
        height = h;
    }

    /**
     * Selects whole Screen.
     */
    public void selectWholeScreen() {
        xPressed = 0;
        yPressed = 0;
        xNow = (int) getWidth();
        yNow = (int) getHeight();
    }

    /**
     * Return an array witch contains the (UPPER_LEFT) Point2D of the rectangle
     * and the width and height of the rectangle.
     *
     * @return the int[]
     */
    public int[] calculatedRectangle() {

        if (xNow > xPressed) { // Right
            if (yNow > yPressed) // and DOWN
                return new int[]{xPressed, yPressed, xNow - xPressed, yNow - yPressed};
            else if (yNow < yPressed) // and UP
                return new int[]{xPressed, yNow, xNow - xPressed, yPressed - yNow};
        } else if (xNow < xPressed) { // LEFT
            if (yNow > yPressed) // and DOWN
                return new int[]{xNow, yPressed, xPressed - xNow, yNow - yPressed};
            else if (yNow < yPressed) // and UP
                return new int[]{xNow, yNow, xPressed - xNow, yPressed - yNow};
        }

        return new int[]{xPressed, yPressed, xNow, yNow};
    }



    private String sendPost(boolean isOverlayRequired, File imageUrl, String language) throws Exception {
        String url = "https://api.ocr.space/parse/image"; // OCR API Endpoints
        StringBuffer responseString = new StringBuffer();
        try {
            MultipartUtility multipart = new MultipartUtility(url, "UTF-8");

            multipart.addFormField("isOverlayRequired", Boolean.toString(isOverlayRequired));
            multipart.addFormField("isCreateSearchablePdf","false");
            multipart.addFormField("language",language);
            multipart.addFilePart("file", imageUrl);
            List<String> response = multipart.finish();

            JSONObject obj = new JSONObject(response.get(0));
            JSONArray jsonArray = obj.getJSONArray("ParsedResults");
            JSONObject childJSONObject = jsonArray.getJSONObject(0);

             parsedText = childJSONObject.getString("ParsedText");
             transText = callUrlAndParseResult("ja","en",childJSONObject.getString("ParsedText"));
            sourceText.setText(parsedText);
            translatedText.setText(transText);
            System.out.println("value = "+parsedText+"trans= "+transText);
            for (String line : response) {
                responseString.append(line);
            }
        } catch (IOException ex) {
            // Log.v("OCR Exception",ex.getMessage());
        }
        //return result
        return String.valueOf(responseString);
    }


    private static String callUrlAndParseResult(String langFrom, String langTo,
                                                String word) throws Exception {

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