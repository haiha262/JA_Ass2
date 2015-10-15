/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps.transporter;

import aps.building.Car;
import aps.building.Room;
import aps.system.SystemSupport;
import aps.transporter.Door.Direction;
import java.awt.Color;
import java.awt.Graphics2D;


public class Lift {

    private Shuttle aShuttle;
    private Door aDoor;

    private Room aRoom;
    private Car aCar;
    private double x;
    private double y;
    private double w;
    private double h;

   
    /**
     * Lift Constructor
     *
     * @param x start X
     * @param y start Y
     * @param w width
     * @param h length
     * @param initialPos start position
     * @param secsPerFloor time to move between each floor
     * @param aCar Car
     */
    public Lift(double x, double y, double w, double h, double initialPos, double secsPerFloor, Car aCar) {

        aRoom = new Room(x, y, w, h);
      
        this.x = x;
        this.y = y;
        this.w = aRoom.getRoomWidth();
        this.h = aRoom.getRoomLength();

        this.aCar = aCar;

        double doorThick = 0.3;
        double doorX = 2.3;//A7X
        double doorY = 2.875 - doorThick / 2;//A7Y
        double doorW = 4.5 - 2.3;//A8-A7

        aDoor = new Door(doorX + doorW / 2, doorY + doorThick / 2, doorW, doorThick);

        floorPms = 0.001 / secsPerFloor;   // unit/ms
        curPos = initialPos * SystemSupport.heightSeparation;
        double aShuttleW = doorW;
        double aShuttleL = 2.875 - (-2.875) - doorThick * 2;//A8-C8
        double aShuttleX = doorX;
        double aShuttleY = SystemSupport.aisleY;
        aShuttle = new Shuttle(aShuttleX + aShuttleW / 2, aShuttleY, aShuttleW, aShuttleL, initialPos, secsPerFloor, aCar);

        isSetOpen = false;
        isSetClose = false;
        isStartRelease = false;
    }

    /**
     * get aShuttle
     * @return aShuttle
     */
    public Shuttle getaShuttle() {
        return aShuttle;
    }

    /**
     * get aDoor
     * @return
     */
    public Door getaDoor() {
        return aDoor;
    }

    /**
     * set a Shuttle
     * @param aShuttle
     */
    public void setaShuttle(Shuttle aShuttle) {
        this.aShuttle = aShuttle;
    }

    /**
     * set a Car
     * @param aCar
     */
    public void setaCar(Car aCar) {
        this.aCar = aCar;
        aShuttle.setCurrentCar(aCar);
    }

    /**
     * draw
     * @param g Graphics2D
     */
    public void draw(Graphics2D g) {
        g.setTransform(SystemSupport.af_def);
        g.setColor(Color.BLACK);
        g.drawString("*Lift: " + curDir +" - Fl:"+ (int)(curPos/SystemSupport.heightSeparation) , SystemSupport.DEFAULT_WIDTH - 40, 20);
        //g.drawString("setTarget " + toBay, SystemSupport.DEFAULT_WIDTH  - 60, 80);
//        System.out.println("Shuttle setTarget " + toBay + ", new state: " + curDir + "," + curPos);
        g.setTransform(SystemSupport.af_scale);

        if (aShuttle.isOnGround()) {
            aRoom.draw(g, "");
            aDoor.draw(g);
        } else {
            aRoom.fill(g, "");
        }
        aShuttle.draw(g);
    }
    private boolean isSetOpen;
    private boolean isSetClose;
    private boolean isFinish;
    private boolean isStartRelease;

    /**
     * is Finish
     * @return true/false
     */
    public boolean isFinish() {
        return isFinish;
    }

    /**
     * set Finish
     * @param isFinish true if it finished
     */
    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public enum Direction {

        OFF, DOWN, UP;
    }
    private Direction curDir = Direction.OFF;
    private double target;
    public final double TOL = 0.001;
    private double curPos;
    private final double floorPms;   // floors/ms

    /**
     * Initiate movement toward target. If the current position is within
     * tolerance TOL of the target, the motor will be OFF. Otherwise movement
     * "up" or "down" is initialised. Movement will actually occur in update().
     *
     * @param target position to move toward.
     */
    public void setTarget(double target) {
        this.setFinish(false);
        this.target = target * SystemSupport.heightSeparation;
        if (target > curPos + TOL) {
            curDir = Direction.UP;

        } else if (target < curPos - TOL) {
            curDir = Direction.DOWN;
        } else {
            curDir = Direction.OFF;
            curPos = target;
        }
        System.out.println("Shaft setTarget " + target + ", new state: " + curDir + "," + curPos);

    }

    public boolean isAtTarget() {
        return curPos == target && curDir == Direction.OFF;
    }

    /**
     * Update lift with dt time
     *
     * @param dt
     */
    public void update(long dt) {
        // aShuttle.update(dt);
        aDoor.update(dt);
        aShuttle.update(dt);
        switch (curDir) {
            case DOWN:
                curPos -= dt * floorPms;
//                if (getaShuttle().getaTrolley().isReleaseCar()) {
//                    // setisOnGround(true);
//                    getaShuttle().setisOnFloor(false);
//                }
                if (curPos <= target + TOL) {
                    curPos = target;
                    curDir = Direction.OFF;
                    getaShuttle().setisOnGround(true);
                    getaShuttle().setisOnFloor(false);
                    System.out.println("Shaft update, new state: " + curDir + "," + curPos);
                }
                System.out.println("Shaft update, curFl " + curPos / SystemSupport.heightSeparation);
                break;
            case UP:
                curPos += dt * floorPms;
//                if (getaShuttle().getaTrolley().isReleaseCar()) {
//                    getaShuttle().setisOnGround(false);
//                    //setisOnFloor(true);
//                }
                if (curPos >= target - TOL) {
                    curPos = target;
                    curDir = Direction.OFF;
                    getaShuttle().setisOnFloor(true);
                    getaShuttle().setisOnGround(false);
                    System.out.println("Shaft update, new state: " + curDir + "," + curPos + "curFl" + curPos / SystemSupport.heightSeparation);
                }
                System.out.println("Shaft update, curFl " + curPos / SystemSupport.heightSeparation);
                break;
        }
        if (aShuttle.isOnGround()) {
            if (aShuttle.getaTrolley().isReleaseCar()) {
                aDoor.setCurDir(Door.Direction.IDLE);
                //if have a more door on  exit room
//                if (aDoor.getCurDir() == Door.Direction.OPENED && !isSetOpen) {
//                    aShuttle.getaTrolley().setIsCollectCar(true);
//                    aShuttle.getaTrolley().setNewMove(this.y + this.h / 2, this.y + this.h * 3 / 2);
//                    isSetOpen = true;
//                }
//                if (aShuttle.getaTrolley().isFinish() && !isSetClose) {
//                    aDoor.setCurDir(Door.Direction.CLOSING);
//                    isSetClose = true;
//                }
//                if (aDoor.getCurDir() == Door.Direction.CLOSED
//                        && aShuttle.getaTrolley().getaComb().getComb_status() == Comb.Comb_status.OFF) {
//                    setFinish(true);
//                }
//            if (aShuttle.isOnGround() && aShuttle.getaTrolley().IsCarryCar() && !isStartRelease) {
//                isStartRelease = true;
//                aDoor.setCurDir(Direction.OPENING);
//            }
            } else {
                if (aDoor.getCurDir() == Door.Direction.OPENED && !isSetOpen) {
                    aShuttle.getaTrolley().setIsCollectCar(true);
                    aShuttle.getaTrolley().setCurDir(Trolley.Move_direct.FORWARD);
                    isSetOpen = true;
                }
                if (aShuttle.getaTrolley().isFinish() && !isSetClose) {
                    aDoor.setCurDir(Door.Direction.CLOSING);
                    isSetClose = true;
                }
                if (aDoor.getCurDir() == Door.Direction.CLOSED) {
                    setFinish(true);
                }
            }
        }
    }

}
