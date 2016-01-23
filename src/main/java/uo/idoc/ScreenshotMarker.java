package uo.idoc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import uo.idoc.difference.HouseDifference;
import uo.idoc.model.House;

public class ScreenshotMarker {

  public ScreenshotMarker() {

  }

  private static final int OLD_COLOR = 0x00FF00E1;
  private static final int NEW_COLOR = 0x00FFEA00;
  private static final int LATITUDE_OFFSET_PX = 12;
  private static final int LONGITUDE_OFFSET_PX = 7;
  
  // assumes 0,0 lat long is at top left of image
  public BufferedImage markScreenshot(BufferedImage im, HouseDifference diff) {
    System.out.println(String.format("Marking %d new houses",diff.getOnlyInNew().size()));
    System.out.println(String.format("Marking %d old houses",diff.getOnlyInOld().size()));
    markHouses(im, diff.getOnlyInNew(), NEW_COLOR, true);
    markHouses(im, diff.getOnlyInOld(), OLD_COLOR, false);
    
    writeLegend(im);
    return im;
  }

  private void writeLegend(BufferedImage im) {    
    Graphics2D graph = im.createGraphics();
    
    int fontSize = 80;
    
    graph.setFont(new Font ("Courier New", 1, fontSize));
    graph.setColor(new Color(OLD_COLOR));
    graph.drawString("Fallen houses", fontSize*2, fontSize*2);
    graph.setColor(new Color(NEW_COLOR));
    graph.drawString("New Houses", fontSize*2, fontSize*3);
  }

  private void markHouses(BufferedImage im, Iterable<House> houses, int color, boolean isLeft) {    
    for (House h : houses) {
      int x = h.getLatitude();
      int y = h.getLongitude();
      // drawSquare(im, x + 8, y + 5, 6, color);
      drawArrow(im, x + LATITUDE_OFFSET_PX, y + LONGITUDE_OFFSET_PX, color, isLeft);
    }
  }
  
  private void drawArrow(BufferedImage im, int x, int y, int color, boolean isLeft) {
    Graphics2D graph = im.createGraphics();
    graph.setColor(new Color(color));
    
    int xOffset = isLeft ? -30 : 30;
    
    drawArrowLine(graph, x + xOffset, y + 30, x, y, 7, 3);
    graph.dispose();
  }
  
  /**
   * http://stackoverflow.com/a/27461352
   * Draw an arrow line between two point 
   * @param g the graphic component
   * @param x1 x-position of first point
   * @param y1 y-position of first point
   * @param x2 x-position of second point
   * @param y2 y-position of second point
   * @param d  the width of the arrow
   * @param h  the height of the arrow
   */
  private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h){
     int dx = x2 - x1, dy = y2 - y1;
     double D = Math.sqrt(dx*dx + dy*dy);
     double xm = D - d, xn = xm, ym = h, yn = -h, x;
     double sin = dy/D, cos = dx/D;

     x = xm*cos - ym*sin + x1;
     ym = xm*sin + ym*cos + y1;
     xm = x;

     x = xn*cos - yn*sin + x1;
     yn = xn*sin + yn*cos + y1;
     xn = x;

     int[] xpoints = {x2, (int) xm, (int) xn};
     int[] ypoints = {y2, (int) ym, (int) yn};

     g.drawLine(x1, y1, x2, y2);
     g.fillPolygon(xpoints, ypoints, 3);
  }

}
