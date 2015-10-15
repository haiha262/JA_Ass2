/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps.building;

import aps.system.SystemSupport;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 *
 * @author M
 */
public class Room {

    private Point2D[] theRoom;
    private double roomWidth ;
    private double roomLength ;

    private double roomStartX;
    private double roomStartY;

    private boolean isRoomTurnTable = true;
  

    /**
     * Method get Room Width
     *
     * @return The return room width 
     */
    public double getRoomWidth() {
        return roomWidth;
    }

    /**
     * Method get Room Length
     *
     * @return The return Room Length
     */
    public double getRoomLength() {
        return roomLength;
    }
    /**
     * Method get X
     *
     * @return The return start X
     */
    public double getX() {
        return roomStartX;
    }
    /**
     * Method getY
     *
     * @return The return start Y
     */
    public double getY() {
        return roomStartY;
    }


//    public Room(double roomStartX, double roomStartY) {
//        this.roomStartX = roomStartX;
//        this.roomStartY = roomStartY;
//        
//        roomInit(roomStartX, roomStartY,roomWidth,roomLength);
//    }

    /**
     * Room Constructor
     *
     * @param roomStartX  StartX
     * @param roomStartY StartY
     * @param roomWidth width
     * @param roomLength Length
     */
    public Room(double roomStartX, double roomStartY, double roomWidth, double roomLength) {
        this.roomStartX = roomStartX;
        this.roomStartY = roomStartY;
        this.roomWidth = roomWidth;
        this.roomLength = roomLength;
        roomInit(roomStartX, roomStartY,roomWidth,roomLength);
    }

    /**
     * Method roomInit
     *
     * @param x  StartX
     * @param y StartY
     * @param w width
     * @param l Length
     */
    private void roomInit(double x, double y,double w, double l ) {
        theRoom = new Point2D[5];
        theRoom[0] = new Point2D.Double(x, y);
        theRoom[1] = new Point2D.Double(x + w, y);
        theRoom[2] = new Point2D.Double(x + w, y + l);
        theRoom[3] = new Point2D.Double(x, y + l);
        theRoom[4] = new Point2D.Double(x, y);

    }

    /**
     * Method draw
     *
     * @param g Graphics2D
     * @param name name of room
     */
    public void draw(Graphics2D g, String name) {

        SystemSupport.drawTheShape(g, theRoom);
        
        //draw title room
        float x = (float) (theRoom[0].getX() + roomWidth / 3);
        float y = (float) (theRoom[0].getY() + roomLength + 20);
        g.drawString(name, x, y);
    }
     /**
      * Method fill the room
      *
      * @param g Graphics2D
      * @param name name of room
      */
     public void fill(Graphics2D g, String name) {

        SystemSupport.fillTheShape(g, theRoom);
        
        //draw title room
        float x = (float) (theRoom[0].getX() + roomWidth / 3);
        float y = (float) (theRoom[0].getY() + roomLength + 20);
        g.drawString(name, x, y);
    }

}
