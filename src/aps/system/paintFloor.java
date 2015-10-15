package aps.system;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import aps.building.Bay;
import aps.building.Building;
import aps.building.Car;
import aps.building.Floor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class paintFloor extends JPanel {

    private Building aBuilding;
    private GeneralPath theShapeFloor = new GeneralPath();
    private int floor;
    private Floor currentFloor;
    private Dimension d;
    //private float theScale;
    static Graphics2D g2;

   
    /**
     * paintFloor Constructor
     *
     * @param floor A parameter
     */
    public paintFloor(int floor) {
        this.floor = floor;
        aBuilding = SystemSupport.aBuilding;//new Building(SystemSupport.NF, SystemSupport.NUM_BAYS_N + SystemSupport.NUM_BAYS_S);
    }

    /**
     * Method initalize Floor
     *
     */
    private void initFloor() {
        Point2D[] theRoom = new Point2D[5];
        theRoom[0] = new Point2D.Double(0, -8.6);
        theRoom[1] = new Point2D.Double(14, -8.6);
        theRoom[2] = new Point2D.Double(14, 8.625 + 9.6);
        theRoom[3] = new Point2D.Double(0, 8.625 + 9.6);
        theRoom[4] = new Point2D.Double(0, -8.6);

        theShapeFloor.moveTo(theRoom[0].getX(), theRoom[0].getY()); // part (a)
        for (int j = 1; j < theRoom.length; j++) {
            theShapeFloor.lineTo(theRoom[j].getX(), theRoom[j].getY());
        }
    }

     /**
     * Method initalize Bay
     *
     */
    private void InitBays() {
        currentFloor = aBuilding.getFloor(this.floor);
        int numberBay = currentFloor.getBayList().size();
        for (int i = 0; i < numberBay; i++) {
            Point2D aPosition;
            double x, y;

            Bay abay = currentFloor.getABayList(i);
            if (i < currentFloor.getNumBayNorth()) {
                x = (i * abay.getSize().getX() + SystemSupport.firstNorthBayX);
                y = 0 + SystemSupport.aisleWidth / 2;
                aPosition = new Point2D.Double(x, y);
            } else {
                x = ((i - 5) * abay.getSize().getX() + SystemSupport.firstSouthBayX);
                y = 0 - SystemSupport.aisleWidth / 2 - abay.getSize().getY();// center of cur shaft to caculate
                aPosition = new Point2D.Double(x, y);

            }
            abay.setPosition(aPosition);
        }
    }

     /**
     * Method paint Floor
     *
     */
    private void drawFloor() {

        float thick = 3.0f;
        g2.setStroke(new BasicStroke(thick / SystemSupport.theScale));
        g2.draw(theShapeFloor); //draw room
        //draw bays
        thick = 1.0f;
        g2.setStroke(new BasicStroke(thick / SystemSupport.theScale));
        int numberBay = currentFloor.getBayList().size();
        for (int i = 0; i < numberBay; i++) {
            Bay abay = currentFloor.getABayList(i);
            Car aCurCar = abay.getaCar();

            double x = abay.getPosition().getX();
            double y = abay.getPosition().getY();
            double w = abay.getSize().getX();
            double h = abay.getSize().getY();
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(new Rectangle2D.Double(x, y, w, h));
            if (aCurCar != null) {
                aCurCar.setNewPosition(x + w / 2, y + h / 2);
                aCurCar.setMoveDir(Car.MOVE_DIRECT.IDLE);
                aCurCar.draw(g2);
                g2.setTransform(SystemSupport.af_def);
                Font font = new Font("SanSerif", Font.PLAIN, 12);
                g2.setFont(font);  // always 12 approx
                g2.setColor(Color.BLACK);
                g2.drawString(aCurCar.getId() + "", (int) (x + w / 2), (int) (y + h / 2));
                g2.setTransform(SystemSupport.af_scale);
            }
        }
    }

      /**
     * Method translate the screen for User
     *
     */
    private void processScreen() {
        SystemSupport.af_def = g2.getTransform();  // save
        //progress 1st
        float xScale, yScale;
        float minX, minY;
        Rectangle2D rect;
        d = getSize();

        //rect = theShape.getBounds2D();
        xScale = (float) (d.getWidth() / 11.9);//not yet
        yScale = (float) (d.getHeight() / (8.625 + 9.675));//not yet
        SystemSupport.theScale = (Math.min(xScale, yScale));
        //(d):
        minX = (float) (0);
        minY = (float) (-8.625);

        g2.translate(0.0f, (float) (d.getHeight()));
        g2.scale(SystemSupport.theScale, -SystemSupport.theScale);
        g2.translate(-minX, -minY);
        SystemSupport.af_scale = g2.getTransform();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        processScreen();
        aBuilding = SystemSupport.aBuilding;//new Building(SystemSupport.NF, SystemSupport.NUM_BAYS_N + SystemSupport.NUM_BAYS_S);;
        initFloor();
        InitBays();
        drawFloor();

    }
}
