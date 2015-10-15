/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps.transporter;

import aps.building.Floor;
import aps.building.Car;
import aps.building.Room;
import aps.system.SystemSupport;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author M
 */
public class Trolley {

    private double x;
    private double y;
    private double w;
    private double h;

    private double from;
    private double to;
    private Room aRoom;
    private double width = (double)SystemSupport.trolleyWidth;
    private double length = (double)SystemSupport.trolleyLenght;
    private Move_direct curDir = Move_direct.IDLE;
    private double speedMove;
    private Comb aComb;

    private double df;

    private boolean finish;
    private boolean canCarry;

    private int toBay;
    private double target;
    private double curPos;

    public final double TOL = 0.001;
    private Car aCar;
    private boolean isReleaseCar;
    private boolean isCollectCar;

    public enum Move_direct {

        IDLE, FORWARD, REVERSE, HOLDON;
    }

    /**
     * Trolley Constructor
     *
     * @param x start X
     * @param y start Y
     * @param w Width
     * @param h Length
     * @param from Move From
     * @param to Move To
     * @param aCar A car
     */
    public Trolley(double x, double y, double w, double h, double from, double to, Car aCar) {
        this.aCar = aCar;
        isReleaseCar = false;
        isCollectCar = false;
        //init Trolley
        aComb = new Comb(x, y, w, h);

        this.x = x;
        this.y = y;
        this.w = width;
        this.h = length;

        this.from = from;
        this.to = to;
        this.df = 0;
        //set Speed
        this.speedMove = 10*SystemSupport.trolleySpeed;
        //create 
        aRoom = new Room(x - this.w / 2, y - this.h / 2, this.w, this.h);
       

        setCurDir(Move_direct.IDLE);
        setFinish(false);
        setIsCarryCar(false);
    }

    

    /**
     * Method set A Car
     *
     * @param aCar A Car
     */
    public void setACar(Car aCar) {
        this.aCar = aCar;
    }
    
    /**
     * Method getInfo
     *
     * @return The return Room
     */
    public Room getInfo() {
        return aRoom;
    }
    
    /**
     * Method set New Move
     *
     * @param from New From
     * @param to New To
     */
    public void setNewMove(double from, double to) {
        this.from = from;
        this.to = to;
        df = 0.0;
        setCurDir(Move_direct.FORWARD);
    }

    /**
     * Method set New Position
     *
     * @param x New X
     * @param y New Y
     */
    public void setNewPosition(double x, double y) {
        this.x = x;
        this.y = y;
        aRoom = new Room(x - this.w / 2, y - this.h / 2, this.w, this.h);
        aComb.setNewPostion(x, y);
        if (IsCarryCar() ) {
            if (aCar != null) {
                aCar.setNewPosition(x, y);
            }
        }        
    }

    /**
     * Method get Cur Direction
     *
     * @return The return Direction
     */
    public Move_direct getCurDir() {
        return curDir;
    }

    /**
     * Method set New Direction
     *
     * @param curDir new Direction
     */
    public void setCurDir(Move_direct curDir) {
        df = 0.0;
        this.curDir = curDir;
        setFinish(false);
    }

    /**
     * Method setFinish
     *
     * @param finish 
     */
    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    /**
     * Method isFinish
     *
     * @return The return value
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * Method isCollectCar
     *
     * @return The return true/false
     */
    public boolean isCollectCar() {
        return isCollectCar;
    }

    /**
     * Method set Is CollectCar
     *
     * @param isCollectCar true/false
     */
    public void setIsCollectCar(boolean isCollectCar) {
        this.isCollectCar = isCollectCar;       
    }

    /**
     * Method isReleaseCar
     *
     * @return The return true/false
     */
    public boolean isReleaseCar() {
        return isReleaseCar;
    }

    /**
     * Method setisReleaseCar
     *
     * @param isReleaseCar true/false
     */
    public void setisReleaseCar(boolean isReleaseCar) {
        this.isReleaseCar = isReleaseCar;       
    }

    /**
     * Method get aComb
     *
     * @return The return value
     */
    public Comb getaComb() {
        return aComb;
    }

    /**
     * Method set a Comb
     *
     * @param aComb A 
     */
    public void setaComb(Comb aComb) {
        this.aComb = aComb;
    }

    public boolean IsCarryCar() {       
        return canCarry;
    }

    /**
     * Method set Trolley can Carry Car
     *
     * @param canCarry true/false
     */
    public void setIsCarryCar(boolean canCarry) {
         if(aCar!=null)
        {
            aCar.setPrintInfo(canCarry);
        }
        this.canCarry = canCarry;
    }

    @Override
    public String toString() {
        return "Torlley{" + "curDir=" + curDir + ", target=" + toBay + ", curPos=" + curPos + '}';
    }

    public void draw(Graphics2D g) {
        aComb.draw(g);
        g.setColor(Color.GREEN);
        if (aRoom != null) {
            aRoom.draw(g, "");
        }
        if (aCar != null) {           
            aCar.draw(g);
        }
        g.setTransform(SystemSupport.af_def);
        g.setColor(Color.BLACK);
        g.drawString("*Trolley " + curDir, SystemSupport.DEFAULT_WIDTH  - 40, 80);
        //g.drawString("setTarget " + toBay, SystemSupport.DEFAULT_WIDTH  - 60, 80);
//        System.out.println("Shuttle setTarget " + toBay + ", new state: " + curDir + "," + curPos);
        g.setTransform(SystemSupport.af_scale);
    }

    public void update(long dt) {
        aComb.update(dt);
        if (!isFinish()) {
            if (curDir != Move_direct.IDLE) {

                if (curDir == Move_direct.FORWARD) {
                    df +=  (double) speedMove/dt;
                    if (df > 1.0) {
                        df = 1.0;

                        df = 0.0;
                        setCurDir(Move_direct.REVERSE);
                        setIsCarryCar(!IsCarryCar());
                        if (IsCarryCar()) {
                            aComb.setComb_status(Comb.Comb_status.EXTEND);
                        } else {
                            aComb.setComb_status(Comb.Comb_status.UNDO_EXTEND);
                        }
                      
                        //swap to & from
                        double temp = from;
                        from = to;
                        to = temp;

                    }
                } else if (curDir == Move_direct.REVERSE                       
                        && ((IsCarryCar() && aComb.getComb_status() == Comb.Comb_status.RAISE)
                        || (!IsCarryCar() && aComb.getComb_status() == Comb.Comb_status.OFF))) {
                    df += (double) speedMove/dt;
                    if (df > 1.0) {
                        df = 1.0;
                        //finish Forward

                        if (!isCollectCar) {
                            setIsCarryCar(false);

                        }
                        setFinish(true);
                    }

                }
                System.out.println("df :" + df);
                y = (double) (from + (to - from) * df);
                setNewPosition(x, y);
            }
        }

    }
}
