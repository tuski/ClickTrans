package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller extends MouseAdapter {
    int x=0, y=0, width=0, height= 0;
    Point start = new Point();
    Point end = new Point();

    Rectangle screen = new Rectangle();

    @FXML
    private Button sendMsg;

    @FXML
    private ImageView imgView;

    @FXML private AnchorPane ap;


    BorderPane borderPane = new BorderPane();

    /** The canvas. */
    Canvas canvas = new Canvas();


    @FXML
    public void captureImage(){

        //https://stackoverflow.com/questions/41287372/how-to-take-snapshot-of-selected-area-of-screen-in-javafx
        Stage stage = Main.pStage;
        CaptureWindow window = new CaptureWindow(Screen.getPrimary().getBounds().getWidth(),Screen.getPrimary().getBounds().getHeight(), stage);
        window.show();


       // Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        MouseListener ml;
        ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                start.setLocation(e.getX(),e.getY());
                System.out.println("pressed at= "+e.getX());
            }
        };

//        Stage stage = (Stage) ap.getScene().getWindow();
//        stage.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, new EventHandler<javafx.scene.input.MouseEvent>() {
//            public void handle(javafx.scene.input.MouseEvent e) {
//                start.setLocation(e.getX(),e.getY());
//                System.out.println("pressed at= "+e.getX());
//            }
//        });
//
//        Rectangle screen = new Rectangle(start, new Dimension(end.x - start.x, end.y - start.y));



    }

public void extractImage(){
    try {
        BufferedImage screenCapture = new Robot().createScreenCapture(screen);
        WritableImage myImage = SwingFXUtils.toFXImage(screenCapture, null);
        imgView.setImage(myImage);
        ImageIO.write(screenCapture,"jpg", new File("out.jpg"));



        Image cursor = ImageIO.read(new File("c:/cursor.gif"));
        int x = MouseInfo.getPointerInfo().getLocation().x;
        int y = MouseInfo.getPointerInfo().getLocation().y;

        Graphics2D graphics2D = screenCapture.createGraphics();
        graphics2D.drawImage(cursor, x, y, 16, 16, null); // cursor.gif is 16x16 size.
        ImageIO.write(screenCapture, "GIF", new File("c:/capture.gif"));

    } catch (AWTException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}

    @Override
    public void mouseClicked(MouseEvent e) {
        x=e.getX();
        y=e.getY();
        start.setLocation(x,y);
        System.out.println(x);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point end = e.getPoint();

        extractImage();
//         new Rectangle(start, new Dimension(end.x - start.x, end.y
//                - start.y));
//        screen.setBounds(start,start, new Dimension(end.x - start.x, end.y
//                - start.y));
        //super.mouseReleased(e);
    }

}
