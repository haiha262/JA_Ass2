package aps.building;

import aps.system.SystemSupport;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Tran Ha
 */
public class Car {

    static final double carWidth = SystemSupport.carWidth;//CONFIG
    static final double carLength = SystemSupport.carLength;//CONFIG;
    static final double carEntrySpeed = SystemSupport.carEntrySpeed;//CONFIG
    private Color aColor;
    private int id;
    private Point2D fromPoint;
    private Point2D toPoint;
    private double moveFrom;

    private boolean finish;
    private boolean status;
    private double speedMove;

    private int angleFrom;
    private int angleTo;

    private boolean printInfo = false;
    private MOVE_DIRECT moveDir;

    public enum MOVE_DIRECT {

        HORIZONTAL, VERTICAL, ROTATION, IDLE
    }

    /**
     * Cars Constructor
     *
     * @param aColor Color
     * @param id id of car
     * @param fromPoint Move from
     * @param toPoint to target
     * @param moveDir A Direction
     */
    public Car(Color aColor, int id, Point2D fromPoint, Point2D toPoint, MOVE_DIRECT moveDir) {
        this.aColor = aColor;
        this.id = id;

        this.fromPoint = fromPoint;
        this.toPoint = toPoint;

        this.finish = false;
        this.moveDir = moveDir;
        if (moveDir == MOVE_DIRECT.HORIZONTAL) {
            this.moveFrom = fromPoint.getX();
        } else {
            this.moveFrom = fromPoint.getY();
        }

        setNewPosition(fromPoint.getX(), fromPoint.getY());
    }

    /**
     * Method set Rotation Car
     *
     * @param angleFrom angle From
     * @param angleTo angle To
     */
    public void setRotation(int angleFrom, int angleTo) {
        this.angleFrom = angleFrom;
        this.angleTo = angleTo;
        this.moveDir = MOVE_DIRECT.ROTATION;
        setFinish(false);
    }

    /**
     * Method setMoveDir
     *
     * @param moveDir MOVE_DIRECT
     */
    public void setMoveDir(MOVE_DIRECT moveDir) {
        this.moveDir = moveDir;
    }

    /**
     * Method set Print Info
     *
     * @param value true if it will be printed
     */
    public void setPrintInfo(boolean value) {
        this.printInfo = value;
    }

    /**
     * Get Car id
     *
     * @return id of car
     */
    public int getId() {
        return id;
    }

    /**
     * Get car color
     *
     * @return color of car
     */
    public Color getColor() {
        return aColor;
    }

    @Override
    public String toString() {
        return "Cars{" + "id=" + id + " Color:            }";
    }

    /**
     * Set the value of Color
     *
     * @param id new value of id
     */
    public void setColor(Color aColor) {
        this.aColor = aColor;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method set Finish
     *
     * @param finish true if finish
     */
    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    /**
     * Method isFinish
     *
     * @return The return status a process
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * Method setNewPosition
     *
     * @param x A new start X
     * @param y A new Start Y
     */
    public void setNewPosition(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    /**
     * Method draw Car
     *
     * @param g A Graphics2D
     */
    public void draw(Graphics2D g) {
        g.setTransform(SystemSupport.af_def);
        if (printInfo) {
            g.setColor(Color.BLACK);

            g.drawString("*Car ", SystemSupport.DEFAULT_WIDTH - 40, 100);
            g.drawString("id " + id + " ", SystemSupport.DEFAULT_WIDTH - 40, 120);

            g.setColor(this.aColor);
            g.fillRect(SystemSupport.DEFAULT_WIDTH + 10, 110, 10, 10);
        } else {
            g.setColor(this.aColor);
        }
        g.setTransform(SystemSupport.af_scale);
        float thick = 1.75f;
        g.setStroke(new BasicStroke(thick / SystemSupport.theScale));
        if (moveDir == MOVE_DIRECT.ROTATION) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.rotate(Math.toRadians(angleFrom), X, Y);// rotate at center of Car
            g2d.setColor(this.aColor);
            g2d.draw(new Rectangle2D.Double(X - Car.carWidth / 2, Y - Car.carLength / 2, carWidth, carLength));
            g2d.dispose();
        } else {

            if (moveDir == MOVE_DIRECT.HORIZONTAL) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(this.aColor);

                g2d.rotate(Math.toRadians(45), X, Y);// rotate at center of Car
                g2d.draw(new Rectangle2D.Double(X - Car.carWidth / 2, Y - Car.carLength / 2, carWidth, carLength));
                g2d.dispose();
            } else if (moveDir == MOVE_DIRECT.VERTICAL || moveDir == MOVE_DIRECT.IDLE) {
//                g.draw(new Rectangle2D.Double(X - Car.carLength / 2, Y - Car.carWidth / 2, carLength, carWidth));
                g.draw(new Rectangle2D.Double(X - Car.carWidth / 2, Y - Car.carLength / 2, carWidth, carLength));
            }
        }
        thick = 1.0f;
        g.setStroke(new BasicStroke(thick / SystemSupport.theScale));
    }
    private double X;
    private double Y;

    /**
     * Method update Car
     *
     * @param dt timer
     */
    public void update(long dt) {

        if (!isFinish()) {
            if (moveDir == MOVE_DIRECT.ROTATION) {
                angleFrom += 1;
                if (angleFrom > angleTo) {
                    angleFrom = angleTo;
                    setFinish(true);
                }
            } else if (moveDir == MOVE_DIRECT.IDLE) {
                X = (int) fromPoint.getX();
                Y = (int) fromPoint.getY();
            } else {
                speedMove = carEntrySpeed * 10 / dt;
                if (moveDir == MOVE_DIRECT.HORIZONTAL) {
                    double deltaX = X - toPoint.getX();
                    double deltaY = Y - toPoint.getY();
                    double direction = Math.atan(deltaY / deltaX);
                    X -= (speedMove * (double) Math.cos(direction));
                    Y -= (speedMove * (double) Math.sin(direction));

                    if (X < toPoint.getX()) {
                        X = toPoint.getX();
                        Y = toPoint.getY();
                        setFinish(true);
                    }
                } else {
                    if (moveFrom > toPoint.getY()) {

                        X = (int) toPoint.getX();
                        Y = (int) moveFrom;

                    } else {
                        moveFrom = toPoint.getY();
                        setFinish(true);
                    }
                }
            }
        }

    }

}
