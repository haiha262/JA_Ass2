  package aps.building;

import aps.system.SystemSupport;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author M
 */
public class Floor {

    private ArrayList<Bay> bayList;

    private int numberBay;
    private int id;
    public static final int NUM_BAYS_N = SystemSupport.NUM_BAYS_N;
    public static final int NUM_BAYS_S = SystemSupport.NUM_BAYS_S;

    /**
     * Construction
     * @param id floor number
     * @param numberBay number bay of floor
     */
    public Floor(int id, int numberBay) {
        this.id = id;
        this.numberBay = numberBay;
        this.bayList = new ArrayList<Bay>();
        for (int i = 1; i <= numberBay; i++) {

            bayList.add(new Bay(i, true));
        }
    }

    /**
     * get Total Bay
     * @return size of bayList
     */
    public int getTotalBay()
    {
        return bayList.size();
    }
    /**
     * get Number BAYS North
     * @return NUM_BAYS_N
     */
    public int getNumBayNorth() {
        return NUM_BAYS_N;
    }

    /**
     * get Number Bay South
     * @return
     */
    public int getNumBaysSouth() {
        return NUM_BAYS_S;
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
     * Get the value of bayList
     *
     * @return the value of bayList
     */
    public ArrayList getBayList() {
        return bayList;
    }

    /**
     * Add a new Bay
     *
     * @param aBay
     * @return true if successful
     */
    public boolean add(Bay aBay) {
        if (aBay == null) {
            return false;
        }
        return bayList.add(aBay);
    }

    /**
     * Set the value of bayList
     *
     * @param bayList new value of bayList
     */
    public void setBayList(ArrayList bayList) {
        this.bayList = bayList;
    }

    /**
     * Get the value of bayList at specified aBay
     *
     * @param aBay
     * @return the value of bayList at specified index
     */
    public int getABayList(Bay aBay) {
        return this.bayList.indexOf(aBay);
    }
    /**
     * Get the aBay of bayList at specified index
     *
     * @param index
     * @return the value of bayList at specified index
     */
    public Bay getABayList(int index) {
        return bayList.get(index);
    }

    @Override
    public String toString() {
        return "Floor{" + "bayList=" + bayList + ", numberBay=" + numberBay + ", id=" + id + '}';
    }
}
