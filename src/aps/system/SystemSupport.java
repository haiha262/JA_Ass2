package aps.system;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import aps.building.Building;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author M
 */
public class SystemSupport {

    public static long dt = 100;
    public static Building aBuilding;
    public static float theScale;
    private static ArrayList arrPoint2D = new ArrayList();
    private ArrayList<Point2D> tempArrPoint2D = new ArrayList<>();
//default value in APS properties
    public static int DEFAULT_WIDTH = 700;
    public static int DEFAULT_HEIGHT = 800;
    public static double aisleY = 0.0;
    public static double aisleWidth = 5.5;//cal
    public static double bayLength = 5.75;
    public static double bayWidth = 2.2;
    public static double carEntrySpeed = 1.4;
    public static double carLength = 5.0;
    public static double carWidth = 1.9;
    public static double clampTime = 0.5;
    public static double combExtension = 0.25;
    public static double combRise = 0.1;
    public static double firstNorthBayX = 2.0;
    public static double firstSouthBayX = 2.0;
    public static double heightFloor1 = 2.58;
    public static double heightSeparation = 2.48;
    public static double liftSpeed = 0.13;
    public static double liftX = 1.6;
    public static int NF = 5;
    public static int NUM_BAYS_N = 5;
    public static int NUM_BAYS_S = 5;
    public static double pedDoorWidth = 1.0;
    public static double slidingDoorTime = 3.0;
    public static double trolleySpeed = 0.13;
    public static double trolleyWidth = 0.75;
    public static double trolleyLenght = 4;
    public static double turnTableX = 3.4;
    public static double turnTableY = 6.275;
    public static double carStartX = turnTableX + turnTableY;
    public static double carStartY = 0;
    public static double secsPerFloor = 1.5;//CONFIG
    public static AffineTransform af_def;
    public static AffineTransform af_scale;

    public static void DelayTime(long timeDelay) {
        try {
            Thread.sleep(timeDelay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawTheShape(Graphics2D g, Point2D[] theRoom) {
        GeneralPath theShape = new GeneralPath();
        theShape.moveTo(theRoom[0].getX(), theRoom[0].getY()); // part (a)
        for (int i = 1; i < theRoom.length; i++) {
            theShape.lineTo(theRoom[i].getX(), theRoom[i].getY());
        }
        g.draw(theShape); //draw room
    }

    public static void fillTheShape(Graphics2D g, Point2D[] theRoom) {
        GeneralPath theShape = new GeneralPath();
        theShape.moveTo(theRoom[0].getX(), theRoom[0].getY()); // part (a)
        for (int i = 1; i < theRoom.length; i++) {
            theShape.lineTo(theRoom[i].getX(), theRoom[i].getY());
        }

        g.setColor(Color.BLACK);
        g.fill(theShape); //draw room

    }

    public SystemSupport() {
    }
    public static final String INPUT_FILE_PROPS = "APS.properties";
    public static final String INPUT_FILE_FLOOR = "floorplan_data.txt";
    private Properties props;

    public static void loadConfig() {
        SystemSupport s = new SystemSupport();
//        s.loadAllConfig();
        try {
            s.loadFloor(INPUT_FILE_FLOOR);
            s.loadAllConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllConfig() {
        props = new Properties();
        try {
            FileInputStream in = new FileInputStream(INPUT_FILE_PROPS);
            props.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        public static int DEFAULT_WIDTH = 600;
//    public static int DEFAULT_HEIGHT = 800;
        DEFAULT_WIDTH = (int) getIntProperty("DEFAULT_WIDTH", 600);;
        DEFAULT_HEIGHT = (int) getIntProperty("DEFAULT_HEIGHT", 800);
//    public static double aisleY = 0.0;
        aisleY = (double) getIntProperty("aisleY", 0.0);
//    public static double bayLength = 5.75;
//    public static double bayWidth = 2.2;
        bayLength = (double) getIntProperty("bayLength", 5.75);
        bayWidth = (double) getIntProperty("bayWidth", 2.2);
//    public static double carEntrySpeed = 1.4;
        carEntrySpeed = (double) getIntProperty("carEntrySpeed", 1.4);
//    public static double carLength = 5.0;
        carLength = (double) getIntProperty("carLength", 5.0);
//    public static double carWidth = 1.9;
        carWidth = (double) getIntProperty("carWidth", 1.9);
//    public static double clampTime = 0.5;
        clampTime = (double) getIntProperty("clampTime", 0.5);
//    public static double combExtension = 0.25;
        combExtension = (double) getIntProperty("combExtension", 0.25);
//    public static double combRise = 0.1;
        combRise = (double) getIntProperty("combRise", 0.1);
//    public static double firstNorthBayX = 2.0;
        firstNorthBayX = (double) getIntProperty("firstNorthBayX", 2.0);
//    public static double firstSouthBayX = 2.0;
        firstSouthBayX = (double) getIntProperty("firstSouthBayX", 2.0);
//    public static double heightFloor1 = 2.58;
        heightFloor1 = (double) getIntProperty("heightFloor1", 2.58);
//    public static double heightSeparation = 2.48;
        heightSeparation = (double) getIntProperty("heightSeparation", 2.48);
//    public static double liftSpeed = 0.13;
        liftSpeed = (double) getIntProperty("liftSpeed", 0.13);
//    public static double liftX = 1.6;
        liftX = (double) getIntProperty("liftX", 1.6);
//    public static int NF = 5;
        NF = (int) getIntProperty("NF", 5);
//    public static int NUM_BAYS_N = 5;
        NUM_BAYS_N = (int) getIntProperty("NUM_BAYS_N", 5);
//    public static int NUM_BAYS_S = 5;       
        NUM_BAYS_S = (int) getIntProperty("NUM_BAYS_S", 5);
//    public static double pedDoorWidth = 1.0;
        pedDoorWidth = (int) getIntProperty("pedDoorWidth", 3.0);
//    public static double slidingDoorTime = 3.0;
        slidingDoorTime = (int) getIntProperty("slidingDoorTime", 3.0);
//    public static double trolleySpeed = 0.13;
        trolleySpeed = (getIntProperty("trolleySpeed", 0.13));//CONFIG
//    public static double trolleyWidth = 0.75;
        trolleyWidth = (getIntProperty("trolleyWidth", 0.75));//CONFIG
//    public static double turnTableX = 3.4;
        turnTableX = (getIntProperty("turnTableX", 3.4));//CONFIG
//    public static double turnTableY = 6.275;
        turnTableY = (getIntProperty("turnTableY", 6.275));//CONFIG
    }

    private double getIntProperty(String name, double defaultValue) {
        if (props.getProperty(name) != null) {
            return Float.parseFloat(props.getProperty(name));
        }
        return defaultValue;
    }

    //public static ArrayList<Point2D> readFileToArr(String filename) throws IOException
    public void loadFloor(String filename) throws IOException {
        //check exist file
        File f = new File(filename);

        if (!f.exists()) {
            // file is not exist
            throw new IOException("FILE NOT FOUND");
        } else {
            BufferedReader in = null;

            try {
                in = new BufferedReader(new FileReader(filename));
                String str = in.readLine();
                String[] list;
                while (str != null) {
                    list = str.split("[ ,]");
                    str = in.readLine();
                    //processArray(list);

                    if (list[0].contentEquals("m")) {
                        //before clear
                        //add to Point2D[][]
                        if (tempArrPoint2D.size() > 0) {
                            Point2D[] newPoint2D = new Point2D[tempArrPoint2D.size()];
                            for (int i = 0; i < tempArrPoint2D.size(); i++) {
                                newPoint2D[i] = tempArrPoint2D.get(i);
                            }
                            arrPoint2D.add(newPoint2D);
                            tempArrPoint2D.clear();
                        }
                    }
                    Point2D p = new Point2D.Float(Float.parseFloat(list[1]), Float.parseFloat(list[2]));
                    tempArrPoint2D.add(p);
                }
                //process the last
                if (tempArrPoint2D.size() > 0) {
                    Point2D[] newPoint2D = new Point2D[tempArrPoint2D.size()];
                    for (int i = 0; i < tempArrPoint2D.size(); i++) {
                        newPoint2D[i] = tempArrPoint2D.get(i);
                    }
                    arrPoint2D.add(newPoint2D);
                    tempArrPoint2D.clear();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        // return myList;
    }

    public static ArrayList getFloorData() {
        return arrPoint2D;
    }

}
