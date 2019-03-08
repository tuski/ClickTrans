package sample;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ReactangleClickListner extends MouseAdapter {
    int x=0, y=0, width=0, height= 0;
    Point start = new Point();
    @Override
    public void mouseClicked(MouseEvent e) {

        x=e.getX();
        y=e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        //super.mouseReleased(e);
    }
}
