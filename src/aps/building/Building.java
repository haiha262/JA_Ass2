 package aps.building;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import aps.building.Floor;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author M
 */
public class Building extends JPanel{
    private ArrayList<Floor> floorList;
    private int numberFloor;
    private int numberBay;

    
    /**
     * Building Constructor
     *
     * @param numberFloor number of Floor
     * @param numberBay numberof Bay
     */
    public Building(int numberFloor, int numberBay) {
        this.numberFloor = numberFloor;
        this.numberBay = numberBay;
        this.floorList = new ArrayList<Floor>();
        for (int i = 1;i <= numberFloor; i++)
        {
            floorList.add(new Floor(i, numberBay));
        }
    }

    /**
     * Method get Total Floor
     *
     * @return The return total fo
     */
    public int getTotalFloor()
    {
        return floorList.size();
    }
    /**
     * Get Floor list
     * @return array list floor
     */
    public ArrayList<Floor> getFloorList() {
        return floorList;
    }

    /**
     * Get a floor with index
     * @param index
     * @return a Floor
     */
    public Floor getFloor(int index)
    {
        return floorList.get(index);
    }

    @Override
    public String toString() {
        return "Building{" + "floorList=" + floorList + ", numberFloor=" + numberFloor + ", numberBay=" + numberBay + '}';
    }
            
}
