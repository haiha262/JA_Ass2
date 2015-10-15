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
import java.awt.geom.Point2D;

/**
 *
 * @author M
 */
public class Shuttle {

    private double x;
    private double y;
    private double w;
    private double h;

    private double oldPosX;
    private Room aRoom;
    private Trolley aTrolley;
    private Comb aComb;
    private Car aCar;
    private final double floorPms;   // floors/ms

    private double target;
    private double targetH;
    private double curPos;
    private double curPosH;

    private int toBay;
    private Floor currentFloor;
    private double speedMove;
    private double df;
    private boolean finish;
    public final double TOL = 0.001;

    private boolean flag = false;
    private boolean setMoveRight = false;
    private boolean isGoingToFloor = false;
    private boolean setReleaseCar = false;

    private boolean isOnGround = false;

    public enum Direction {

        OFF, DOWN, UP, LEFT, RIGHT;
    }
    private Direction curDir = Direction.OFF;

    /**
     * Shuttle Constructor
     *
     * @param x start X
     * @param y start Y
     * @param w width
     * @param h length
     * @param initialPos start position
     * @param secsPerFloor time to move between each floor
     * @param aCar Car
     */
    public Shuttle(double x, double y, double w, double h, double initialPos, double secsPerFloor, Car aCar) {
        floorPms = 0.01 / secsPerFloor;   // unit/ms
        curPos = initialPos * SystemSupport.heightSeparation;
        this.speedMove = 10 * SystemSupport.trolleySpeed;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.oldPosX = x;

        aRoom = new Room(x - w / 2, y - h / 2, w, h);

        this.flag = false;
        this.setMoveRight = false;
        this.setReleaseCar = false;

        this.aCar = aCar;
        this.aTrolley = new Trolley(x, y, w, h, y, SystemSupport.turnTableY, aCar);

        setisOnGround(true);

    }

    /**
     * Method get Size
     *
     * @return The return Point2D value Width and Length
     */
    public Point2D getSize() {
        return new Point2D.Double(w, h);
    }

    /**
     * Method get a Trolley
     *
     * @return The return Trolley value
     */
    public Trolley getaTrolley() {
        return this.aTrolley;
    }

    /**
     * Method draw
     *
     * @param g A
     */
    public void draw(Graphics2D g) {
        g.setTransform(SystemSupport.af_def);

        g.drawString("*Shuttle: " + curDir, SystemSupport.DEFAULT_WIDTH - 40, 40);
        if (isOnFloor()) {
            g.drawString("curBay=" + (int)( this.x / SystemSupport.bayWidth )+ ",to Bay=" + toBay, SystemSupport.DEFAULT_WIDTH - 40, 60);
        }
        g.setColor(Color.BLUE);
        g.setTransform(SystemSupport.af_scale);
        aRoom.draw(g, "");
        if (aTrolley != null) {
            aTrolley.draw(g);
        }
        g.setColor(Color.BLACK);
    }

    /**
     * Method getPosition
     *
     * @return The return value
     */
    public double getPosition() {
        return curPos;
    }

    /**
     * Method is At Target
     *
     * @return The return value
     */
    public boolean isAtTarget() {
        return curPos == target && curDir == Direction.OFF;
    }

    /**
     * Method is At Target Horizontal
     *
     * @return The return value
     */
    public boolean isAtTargetHorizontal() {
        return curPosH == targetH && curDir == Direction.OFF;
    }

    public void setMovePosition() {
        this.curPosH = x;
    }

    /**
     * Method set Move Horizontal
     *
     * @param targetH A parameter
     */
    public void setMoveHorizontal(double targetH) {
        df = 0;
        oldPosX = this.x;
        this.setFinish(false);
        this.targetH = targetH;
        if (targetH > curPosH + TOL) {
            curDir = Direction.RIGHT;
            aTrolley.setCurDir(Trolley.Move_direct.HOLDON);
        } else if (targetH < curPosH - TOL) {
            curDir = Direction.LEFT;
            aTrolley.setCurDir(Trolley.Move_direct.HOLDON);
        } else {
            curDir = Direction.OFF;
            curPosH = targetH;
        }
    }

    /**
     * Method set Bay Of Floor for moving
     *
     * @param toBay A parameter
     * @param currentFloor A parameter
     */
    public void setBayOfFloor(int toBay, Floor currentFloor) {
        this.toBay = toBay;
        this.currentFloor = currentFloor;
        double start = currentFloor.getABayList(toBay - 1).getPosition().getX();
        double w = currentFloor.getABayList(toBay - 1).getSize().getX();
        this.targetH = (start + w / 2);
        setMoveHorizontal(targetH);
        System.out.println("Shaft setTarget " + toBay + ", new state: " + curDir + "," + curPosH);
    }

    public boolean isOnFloor() {
        return isGoingToFloor;
    }

    public void setisOnFloor(boolean value) {
        isGoingToFloor = value;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public void setisOnGround(boolean value) {
        isOnGround = value;
    }

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

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public boolean isFinish() {
        return finish;
    }

    /**
     * Update the state of this object. Does nothing if the motor is OFF. When
     * position reaches target the motor switches OFF.
     *
     * @param dt increment of simulated time in millisec > 0
     */
    public void update(long dt) {
        aTrolley.update(dt);
        switch (curDir) {
            case DOWN:
                curPos -= dt * floorPms;
                if (aTrolley.isReleaseCar()) {
                    // setisOnGround(true);
                    setisOnFloor(false);
                }
                if (curPos <= target + TOL) {
                    curPos = target;
                    curDir = Direction.OFF;
                    setisOnGround(true);
                    setisOnFloor(false);
                    System.out.println("Shaft update, new state: " + curDir + "," + curPos);
                }
                break;
            case UP:
                curPos += dt * floorPms;
                if (aTrolley.isReleaseCar()) {
                    setisOnGround(false);
                    //setisOnFloor(true);
                }
                if (curPos >= target - TOL) {
                    curPos = target;
                    curDir = Direction.OFF;
                    setisOnFloor(true);
                    setisOnGround(false);
                    System.out.println("Shaft update, new state: " + curDir + "," + curPos);
                }
                break;
            case LEFT:
                df += (double) speedMove / dt;
                if (df > 1.0) {
                    df = 1.0;
                    curPosH = targetH;
                    curDir = Direction.OFF;
                    aTrolley.setCurDir(Trolley.Move_direct.IDLE);

                    setFinish(true);
                    System.out.println("Shaft update, new state: " + curDir + "," + curPosH);
                }

                x = (double) (curPosH + (targetH - curPosH) * df);
                aRoom = new Room(x - this.w / 2, y - this.h / 2, this.w, this.h);

                aTrolley.setNewPosition(x, y);

                break;
            case RIGHT:

                df += (double) speedMove / dt;
                if (df > 1.0) {
                    df = 1.0;
                    curPosH = targetH;
                    curDir = Direction.OFF;
                    aTrolley.setCurDir(Trolley.Move_direct.IDLE);
                    System.out.println("Shaft update, new state: " + curDir + "," + curPosH);
                    if (isOnFloor()) {
                        flag = true;
                    }
                    // setTarget(0.0);
                    //if (aTrolley.isCollectCar()) {
                    if (!aTrolley.IsCarryCar()) {
                        setFinish(true);
                    }
                    // }
                    if (aTrolley.isReleaseCar()) {
                        if (aTrolley.IsCarryCar()) {
                            setFinish(true);
                        }
                    }
                }
                x = (double) (curPosH + (targetH - curPosH) * df);
                aRoom = new Room(x - this.w / 2, y - this.h / 2, this.w, this.h);

                aTrolley.setNewPosition(x, y);
                break;

            case OFF:
            //do nothing
        }

        if (aTrolley.isReleaseCar()) {
            if (flag) {
                releaseCar();
                double start = currentFloor.getABayList(toBay-1).getPosition().getY();
                double h = currentFloor.getABayList(toBay-1).getSize().getY();
                aTrolley.setNewMove(this.y, start + h / 2);
                aTrolley.setIsCollectCar(true);//false : release car
                aTrolley.setFinish(false);
                flag = false;
                //move down
                setMoveRight = true;
                setFinish(false);
            }
            if (aTrolley.isFinish() && aTrolley.isCollectCar() && setMoveRight)//turn back old pos and move lift down
            {
                setMoveRight = false;
                setMovePosition();
                setMoveHorizontal(oldPosX);
                setReleaseCar = true;
            }

        } else {
            //set move to Bay target
            if (flag) {
                double start = currentFloor.getABayList(toBay - 1).getPosition().getY();
                double h = currentFloor.getABayList(toBay - 1).getSize().getY();
                aTrolley.setNewMove(this.y, start + h / 2);
                aTrolley.setIsCollectCar(false);//false : release car
                aTrolley.setFinish(false);
                flag = false;
                //move down
                setMoveRight = true;
            }
            if (aTrolley.isFinish() && !aTrolley.isCollectCar() && setMoveRight)//turn back old pos and move lift down
            {
                setMoveRight = false;
                setMovePosition();
                setMoveHorizontal(oldPosX);
                //store car into bay.
                storeCar();
                //
            }

        }
    }

    /**
     * Method store Car into Bay
     *
     */
    private void storeCar() {

        this.currentFloor.getABayList(toBay - 1).setaCar(aCar);
        aCar = null;
        aTrolley.setACar(aCar);
    }

    /**
     * Method release Car of bay
     *
     */
    private void releaseCar() {

        this.aCar = this.currentFloor.getABayList(toBay - 1).getaCar();
        this.aTrolley.setACar(aCar);
        this.currentFloor.getABayList(toBay - 1).setaCar(null);

    }

    /**
     * Method getCurrentCar
     *
     * @return The return aCar value
     */
    public Car getCurrentCar() {
        return this.aCar;
    }

    /**
     * Method setCurrentCar
     *
     * @param aCar Car
     */
    public void setCurrentCar(Car aCar) {
        this.aCar = aCar;
        this.aTrolley.setACar(aCar);
    }

    /**
     * Method get Current Floor
     *
     * @return The return Current Floor
     */
    public int getCurrentFloor() {
        return (int) (getPosition() / SystemSupport.heightSeparation + 0.5);
    }

    public String toString() {
        return "Shaft{" + curDir
                + ", curFl=" + getCurrentFloor()
                + ", targetFl=" + this.target / SystemSupport.heightSeparation + '}';
    }
}
