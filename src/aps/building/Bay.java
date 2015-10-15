 package aps.building;

import aps.system.SystemSupport;
import java.awt.geom.Point2D;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Tran Ha
 */
public class Bay {

    private int id;
    private boolean empty;
    private Point2D size;
    private Point2D position;
    private static final double length = SystemSupport.bayLength ;
    private static final double width =  SystemSupport.bayWidth ;
    private Car aCar;

    /**
     * Bays Constructor
     *
     * @param id Bay id
     * @param empty true if it has not a Car
     */
    public Bay(int id, boolean empty) {
        this.id = id;
        this.empty = empty;
        this.size = new Point2D.Double(width, length);
    }

    /**
     * Get the value of position
     *
     * @return the value of position
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Set the value of position
     *
     * @param position new value of position
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Get the value of position
     *
     * @return the value of position
     */
    public Point2D getSize() {
        return size;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
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
     * Get the value of empty
     *
     * @return the value of empty
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Set the value of empty
     *
     * @param empty new value of empty
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * Method get a Car
     *
     * @return The return Car type
     */
    public Car getaCar() {
        return aCar;
    }

    /**
     * Method set a Car
     *
     * @param aCar Car type
     */
    public void setaCar(Car aCar) {
        this.aCar = aCar;
        if (aCar != null) {
            setEmpty(false);
        } else {
            setEmpty(true);
        }
    }

    @Override
    public String toString() {
        return "Bays{" + "id=" + id + ", empty=" + empty + ", aCar=" + aCar + '}';
    }

}
