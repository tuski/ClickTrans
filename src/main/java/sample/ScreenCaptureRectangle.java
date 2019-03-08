//package sample;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//
//class ScreenCaptureRectangle {
//  Rectangle captureRect;
//
//  ScreenCaptureRectangle(final BufferedImage screen) {
//    BufferedImage screenCopy = new BufferedImage(screen.getWidth(),
//        screen.getHeight(), screen.getType());
//   // JLabel screenLabel = new JLabel(new ImageIcon(screenCopy));
//    //JScrollPane screenScroll = new JScrollPane(screenLabel);
//
//    //screenScroll.setPreferredSize(new Dimension(300, 300));
//
//    //repaint(screen, screenCopy);
//    //screenLabel.repaint();
//
//    screenLabel.addMouseMotionListener(new MouseMotionAdapter() {
//      Point start = new Point();
//      @Override
//      public void mouseMoved(MouseEvent me) {
//        start = me.getPoint();
//        repaint(screen, screenCopy);
//        screenLabel.repaint();
//      }
//      @Override
//      public void mouseDragged(MouseEvent me) {
//        Point end = me.getPoint();
//        captureRect = new Rectangle(start, new Dimension(end.x - start.x, end.y
//            - start.y));
//        repaint(screen, screenCopy);
//        screenLabel.repaint();
//      }
//    });
//    JOptionPane.showMessageDialog(null, screenScroll);
//  }
//
//  public void repaint(BufferedImage orig, BufferedImage copy) {
//    Graphics2D g = copy.createGraphics();
//    g.drawImage(orig, 0, 0, null);
//    g.setColor(Color.RED);
//    if (captureRect == null) {
//      return;
//    }
//    g.draw(captureRect);
//    g.setColor(new Color(25, 25, 23, 10));
//    g.fill(captureRect);
//    g.dispose();
//  }
//}