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
import aps.building.Room;
import aps.transporter.Door;
import aps.transporter.Lift;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author M
 */
public class PaintPanel extends JPanel {

    private boolean debug = false;
    private boolean isProgess;
    static Graphics2D g2;

    /**
     *
     */
    public Statement aStatement;
    static int currentState = Statement.STATE_STARTUP;
//    static int currentState = Statement.STATE_COLLECT_CAR_INIT;

    //dooublebuff
    private Dimension d;
    static Image bufferimage;
    Graphics buffer_g;
    private int bufferWidth;
    private int bufferHeight;
    private Building aBuilding;

    /**
     * PaintPanel Construction
     * @param isDoubleBuffered true if using double buffer of system
     */
    public PaintPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        //load config
        SystemSupport.loadConfig();

        aStatement = new Statement(currentState);
        SystemSupport.aBuilding = new Building(SystemSupport.NF, SystemSupport.NUM_BAYS_N + SystemSupport.NUM_BAYS_S);
        this.aBuilding = SystemSupport.aBuilding;
        addSomeCar();
        startupInit();
        isProgess = false;

    }

    /**
     * Method reset Buffer
     *
     */
    private void resetBuffer() {
        // always keep track of the image size
        bufferWidth = getSize().width;
        bufferHeight = getSize().height;

        //    clean up the previous image
        if (buffer_g != null) {
            buffer_g.dispose();
            buffer_g = null;
        }
        if (bufferimage != null) {
            bufferimage.flush();
            bufferimage = null;
        }
        System.gc();

        //    create the new image with the size of the panel
        bufferimage = createImage(bufferWidth, bufferHeight);
        buffer_g = bufferimage.getGraphics();
    }
    //private float theScale;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        if (true) {
            if (bufferimage == null) {
                resetBuffer();
            }
            if (buffer_g != null) {
                //this clears the offscreen image, not the onscreen one
                buffer_g.clearRect(0, 0, bufferWidth, bufferHeight);

                //calls the paintbuffer method with 
                //the offscreen graphics as a param
                paintBuffer(buffer_g);

                //we finaly paint the offscreen image onto the onscreen image
                g.drawImage(bufferimage, 0, 0, this);
            }
        } else {
            paintBuffer(g);
        }

    }

    /**
     * paint Buffer
     * @param g Graphics
     */
    public void paintBuffer(Graphics g) {

        g2 = (Graphics2D) g;

        processScreen();
//        isSuspend = true;
//        if (isSuspend) {
//            return;
//        }

        currentState = aStatement.getStatement();
        try {
            switch (currentState) {
                case Statement.STATE_STARTUP: {
                    changeState(Statement.STATE_STARTUPDATE);
                    // startupInit();
                    break;
                }
                case Statement.STATE_STARTUPDATE: {

                    startUpdate();
                    break;
                }
                case Statement.STATE_ENTER_INIT: {
                    carEnterInit();

                    break;
                }
                case Statement.STATE_ENTER: {
                    carEnterUpdate();
                    break;
                }

                case Statement.STATE_TURN_CAR_INIT: {
                    turnTableInit();
                    break;
                }
                case Statement.STATE_TURN_CAR: {
                    turnTableUpdate();
                    break;
                }

                case Statement.STATE_COLLECT_CAR_INIT: {
                    collectCarInit();
                    break;
                }
                case Statement.STATE_COLLECT_CAR: {
                    collectCarUpdate();
                    break;
                }
                case Statement.STATE_MOVE_TO_LIFT_INIT: {
                    moveToLiftInit();
                    break;
                }
                case Statement.STATE_MOVE_TO_LIFT: {
                    moveToLiftUpdate();
                    break;
                }
                case Statement.STATE_MOVE_LIFT_INIT: {
                    moveLiftInit();
                    break;
                }
                case Statement.STATE_MOVE_LIFT: {
                    moveLiftUpdate();
                    break;
                }
                case Statement.STATE_MOVE_TO_BAY_INIT: {
                    moveToBayInit();
                    break;
                }
                case Statement.STATE_MOVE_TO_BAY: {
                    moveToBayInitUpdate();
                    break;
                }

                case Statement.STATE_RELEASE_CAR_INIT: {
                    releaseCarInit();
                    break;
                }
                case Statement.STATE_RELEASE_CAR: {
                    releaseCarUpdate();
                    break;
                }
                case Statement.STATE_RELEASE_CAR_STEP2_INIT: {
                    releaseCarStep2Init();
                    break;
                }
                case Statement.STATE_RELEASE_CAR_STEP2: {
                    releaseCarStep2Update();
                    break;
                }
                case Statement.STATE_RELEASE_CAR_STEP3_INIT: {
                    releaseCarStep3Init();
                    break;
                }
                case Statement.STATE_RELEASE_CAR_STEP3: {
                    releaseCarStep3Update();
                    break;
                }
                case Statement.STATE_RELEASE_CAR_STEP4_INIT: {
                    releaseCarStep4Init();
                    break;
                }
                case Statement.STATE_RELEASE_CAR_STEP4: {
                    releaseCarStep4Update();
                    break;
                }
            }
//            System.out.println("state :" + currentState);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //g.drawImage(bufferimage, 0, 0, this);
        g2.setTransform(SystemSupport.af_def);//restore
    }

    /**
     * Checking process of parking or collecting car
     * @return true if it is processing
     */
    public boolean isProgess() {
        return isProgess;
    }

    private int liftTarget;
    private int bayTarget;

    /**
     * Checking a new car can park
     * @return true if car can park
     */
    public boolean canParking() {
        if (!isProgess) {
            for (int floor = 0; floor < aBuilding.getTotalFloor(); floor++) {
                Floor aFloor = aBuilding.getFloor(floor);
                for (int bay = 0; bay < aFloor.getTotalBay(); bay++) {
                    if (aFloor.getABayList(bay).isEmpty()) {
                        liftTarget = floor + 1;
                        bayTarget = bay + 1;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Change state of system
     * @param state the new state
     */
    public void changeState(int state) {
        aStatement.setStatement(state);
    }

    
    /**
     * Method rectangle Init
     *
     * @param x start X
     * @param y start Y
     * @param w Width
     * @param l Length
     * @return The return Point2D[] value
     */
    private Point2D[] rectangleInit(double x, double y, double w, double l) {
        Point2D[] theRect = new Point2D[5];
        theRect[0] = new Point2D.Double(x, y);
        theRect[1] = new Point2D.Double(x + w, y);
        theRect[2] = new Point2D.Double(x + w, y + l);
        theRect[3] = new Point2D.Double(x, y + l);
        theRect[4] = new Point2D.Double(x, y);
        return theRect;

    }

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

    private GeneralPath theShape = new GeneralPath();
    private GeneralPath theShapeFloor = new GeneralPath();

    /**
     * Method init Ground
     *
     */
    private void initGround() {
        ArrayList pointArrayList = SystemSupport.getFloorData();
        for (int i = 0; i < pointArrayList.size(); i++) {
            Object obj = pointArrayList.get(i);
            Point2D[] p = (Point2D[]) obj;

            theShape.moveTo(p[0].getX(), p[0].getY()); // part (a)
            for (int j = 1; j < p.length; j++) {
                theShape.lineTo(p[j].getX(), p[j].getY());
            }
        }
    }

    /**
     * Method init Floor
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
    private Floor currentFloor;

    /**
     * Method Init Bay
     *
     */
    private void InitBays() {
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
     * Method draw Ground
     *
     */
    private void drawGround() {
        g2.setColor(Color.BLACK);
        float thick = 3.0f;
        g2.setStroke(new BasicStroke(thick / SystemSupport.theScale));
        g2.draw(theShape); //draw room
        //turntable
        double turnTableW = 6.800 - 1;//C1->B9 //CONFIG
        g2.setStroke(new BasicStroke(1.0f / SystemSupport.theScale));
        Ellipse2D.Double shape = new Ellipse2D.Double(SystemSupport.turnTableX - turnTableW / 2, SystemSupport.turnTableY - turnTableW / 2, turnTableW, turnTableW);
        g2.draw(shape);
    }

    /**
     * Method draw Floor
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
            }
        }
    }

    private Lift alift;

    /**
     * Method start up Init
     *
     */
    private void startupInit() {
        initGround();

        double X = SystemSupport.liftX;//1.15> center
        double Y = 0;//-2: B5
        double W = 2.0; //A5-B5
        double L = 3.9; //B7-B5
        //init lift 
        double initialPos = 0;//CONFIG
        double secsPerFloor = 1.5;//CONFIG
        alift = new Lift(X - W / 2, Y - L / 2, W, L, initialPos, secsPerFloor, null);

    }

    /**
     * Method start Update
     *
     */
    private void startUpdate() {
        drawGround();
        g2.setStroke(new BasicStroke(1.0f / SystemSupport.theScale));
        alift.draw(g2);
    }

    private Car aCar;

    private Color[] CarColor = {Color.BLACK, Color.BLUE, Color.CYAN, Color.MAGENTA,
        Color.RED, Color.ORANGE, Color.DARK_GRAY, Color.PINK
    };

    /**
     * Method create New Car
     *
     */
    private void createNewCar() {

        //random color and id
        // generate the random integers for r, g and b value
        Random r = new Random();
//        Color c = new Color(r.nextInt(50), r.nextInt(50), r.nextInt(50), 255);
        Color c = CarColor[r.nextInt(CarColor.length)];
        int id = r.nextInt(9999);
        //Cars(Graphics2D g, Color aColor, int id, Point2D fromPoint, Point2D toPoint, MOVE_DIRECT moveDir) {
        aCar = new Car(c, id,
                new Point2D.Double(SystemSupport.carStartX, SystemSupport.carStartY),
                new Point2D.Double(SystemSupport.turnTableX, SystemSupport.turnTableY),
                Car.MOVE_DIRECT.HORIZONTAL);

    }

    /**
     * Method car Enter Init
     *
     */
    private void carEnterInit() {//start a progess
        isProgess = true;

        createNewCar();
        changeState(Statement.STATE_ENTER);
    }

    /**
     * Method car Enter Update
     *
     */
    private void carEnterUpdate() {
        aCar.update(SystemSupport.dt);
        g2.setStroke(new BasicStroke(1.0f / SystemSupport.theScale));
        aCar.draw(g2);

        if (aCar.isFinish()) {
            changeState(Statement.STATE_TURN_CAR_INIT);
            SystemSupport.DelayTime(500);
        }
        startUpdate();
    }

    private void turnTableInit() {
        aCar.setRotation(45, 180);
        changeState(Statement.STATE_TURN_CAR);
    }

    /**
     * Method turn Table Update
     *
     */
    private void turnTableUpdate() {

        startUpdate();
        aCar.update(SystemSupport.dt);
        aCar.draw(g2);
        if (aCar.isFinish()) {
            changeState(Statement.STATE_COLLECT_CAR_INIT);
        }

    }

    /**
     * Method collect Car Init
     *
     */
    private void collectCarInit() {

        double X = SystemSupport.liftX;//1.15> center
        double Y = 0;//-2: B5
        double W = 2.0; //A5-B5
        double L = 3.9; //B7-B5

        //init lift 
        double initialPos = 0;//CONFIG
        double secsPerFloor = SystemSupport.secsPerFloor;//CONFIG
        alift = new Lift(X - W / 2, Y - L / 2, W, L, initialPos, secsPerFloor, null);
        alift.getaShuttle().getaTrolley().setIsCollectCar(true);
        if (aCar == null) {
            createNewCar();
            aCar.setNewPosition(SystemSupport.turnTableX, SystemSupport.turnTableY);
            aCar.setMoveDir(Car.MOVE_DIRECT.IDLE);
        }
        alift.setaCar(aCar);
        alift.getaDoor().setCurDir(Door.Direction.OPENING);
        changeState(Statement.STATE_COLLECT_CAR);
    }

    /**
     * Method collect Car Update
     *
     */
    private void collectCarUpdate() {
        startUpdate();
        if (alift.isFinish()) {
            changeState(Statement.STATE_MOVE_TO_LIFT_INIT);
        }
        alift.update(SystemSupport.dt);
    }

    /**
     * Method move To Lift Init
     *
     */
    private void moveToLiftInit() {
        alift.getaShuttle().setMovePosition();
        alift.getaShuttle().setMoveHorizontal(SystemSupport.liftX);
        changeState(Statement.STATE_MOVE_TO_LIFT);
    }

    /**
     * Method move To Lift Update
     *
     */
    private void moveToLiftUpdate() {
        startUpdate();
        if (alift.getaShuttle().isFinish()) {
            changeState(Statement.STATE_MOVE_LIFT_INIT);
        }
        alift.update(SystemSupport.dt);
    }

    /**
     * Method move Lift Init
     *
     */
    private void moveLiftInit() {

        // liftTarget = 5;//check empty of floor //CONFIG
        currentFloor = aBuilding.getFloor(liftTarget - 1);
        alift.setTarget(liftTarget);

        changeState(Statement.STATE_MOVE_LIFT);
    }

    /**
     * Method moveLiftUpdate
     *
     */
    private void moveLiftUpdate() {

        startUpdate();
        if (alift.isAtTarget()) {
            changeState(Statement.STATE_MOVE_TO_BAY_INIT);
        }
        alift.update(SystemSupport.dt);
    }

    /**
     * Method move To Bay Init
     *
     */
    private void moveToBayInit() {

        //init bays of floor
        currentFloor = aBuilding.getFloor(liftTarget - 1);
        initFloor();
        InitBays();
        //find bayTarget
        //exmaple
        // if (debug) {
        // bayTarget = 9; // 0< bayTarget <=numberBay
        //}        //init Shutle     
        alift.getaShuttle().setMovePosition();
        alift.getaShuttle().setBayOfFloor(bayTarget, currentFloor);//center of bay

        changeState(Statement.STATE_MOVE_TO_BAY);
        drawFloor();
        flag = false;
    }

    private boolean flag;

    /**
     * Method move To Bay Init Update
     *
     */
    private void moveToBayInitUpdate() {

        if (alift.getaShuttle().isOnFloor()) {
            drawFloor();
            if (alift.getaShuttle().isFinish())//lift go down
            {
                alift.setTarget(0);
            }
            alift.draw(g2);
        } else {
            if (alift.getaShuttle().isOnGround()) {
                startUpdate();
                if (!flag) {
                    alift.getaShuttle().setMoveHorizontal(SystemSupport.turnTableX);
                    flag = true;
                }
                if (alift.getaShuttle().isFinish()) {
                    isProgess = false;
                }
            }
        }

        alift.update(SystemSupport.dt);
    }

    /**
     * Method add Some Car
     *
     */
    private void addSomeCar() {
//if (debug)
        {
            //at Floor 1 bay 6
            for (int fl = 0; fl < SystemSupport.NF; fl++) {
                for (int i = 0; i < 7; i++) {
                    Random r = new Random();
                    Floor aFloor = aBuilding.getFloor(fl);
                    Bay abay = aFloor.getABayList(r.nextInt(9));
                    while (!abay.isEmpty()) {
                        abay = aFloor.getABayList(r.nextInt(9));
                    }
                    Color c = CarColor[r.nextInt(CarColor.length)];
                    int id = r.nextInt(9999);
                    //Cars(Graphics2D g, Color aColor, int id, Point2D fromPoint, Point2D toPoint, MOVE_DIRECT moveDir) {
                    Car newCar = new Car(c, id,
                            new Point2D.Double(0, 0),
                            new Point2D.Double(0, 0),
                            Car.MOVE_DIRECT.IDLE);
                    abay.setaCar(newCar);
                }
            }
//            Floor aFloor = aBuilding.getFloor(1 - 1);
//            Bay abay = aFloor.getABayList(6 - 1);
//
//            Random r = new Random();
//            Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256), 255);
//            int id = r.nextInt(9999);
//            //Cars(Graphics2D g, Color aColor, int id, Point2D fromPoint, Point2D toPoint, MOVE_DIRECT moveDir) {
//            Car newCar = new Car(c, 1235,
//                    new Point2D.Double(0, 0),
//                    new Point2D.Double(0, 0),
//                    Car.MOVE_DIRECT.IDLE);
//            abay.setaCar(newCar);
//
//            //AT FLor 1 bay 2
//            aFloor = aBuilding.getFloor(1 - 1);
//            abay = aFloor.getABayList(2 - 1);
//            c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256), 255);
//            id = r.nextInt(9999);
//            //Cars(Graphics2D g, Color aColor, int id, Point2D fromPoint, Point2D toPoint, MOVE_DIRECT moveDir) {
//            newCar = new Car(c, 8888,
//                    new Point2D.Double(0, 0),
//                    new Point2D.Double(0, 0),
//                    Car.MOVE_DIRECT.IDLE);
//            abay.setaCar(newCar);
        }
    }

    /**
     * Find a car
     * @param id of car
     * @return true if car exist
     */
    public boolean setFindID(int id) {
        int Fl_length = aBuilding.getTotalFloor();
        for (int fl = 0; fl < Fl_length; fl++) {
            Floor curFl = aBuilding.getFloor(fl);
            int bay_length = curFl.getTotalBay();
            for (int curBay = 0; curBay < bay_length; curBay++) {
                Car curCar = curFl.getABayList(curBay).getaCar();

                if (curCar != null && curCar.getId() == id) {
                    bayTarget = curBay + 1;
                    liftTarget = fl + 1;
                    return true;

                }
            }
        }
        return false;
    }

    /**
     * Method release Car Init
     *
     */
    private void releaseCarInit() {
//        bayTarget = 5 + 1;// CONFIG
//        liftTarget = 0 + 1;// CONFIG
        currentFloor = aBuilding.getFloor(liftTarget - 1);
        initFloor();
        InitBays();
        isProgess = false;
        double X = SystemSupport.liftX;//1.15> center
        double Y = 0;//-2: B5
        double W = 2.0; //A5-B5
        double L = 3.9; //B7-B5

        //init lift 
        double initialPos = 0;//CONFIG
        double secsPerFloor = SystemSupport.secsPerFloor;//CONFIG
        alift = new Lift(X - W / 2, Y - L / 2, W, L, initialPos, secsPerFloor, null);

        alift.getaDoor().setCurDir(Door.Direction.CLOSED);
        alift.getaShuttle().setMovePosition();
        alift.getaShuttle().setMoveHorizontal(SystemSupport.liftX);

        alift.getaShuttle().getaTrolley().setisReleaseCar(true);

        changeState(Statement.STATE_RELEASE_CAR);
    }

    /**
     * Method release Car Update
     *
     */
    private void releaseCarUpdate() {

        if (alift.getaShuttle().isOnGround()) {
            startUpdate();
            if (alift.getaShuttle().isFinish()) {
                changeState(Statement.STATE_RELEASE_CAR_STEP2_INIT);
            }
        }

        alift.update(SystemSupport.dt);

    }

    /**
     * Method release Car Step 2 Init
     *
     */
    private void releaseCarStep2Init() {
        alift.setTarget(liftTarget);
        changeState(Statement.STATE_RELEASE_CAR_STEP2);
        flag = false;
    }

    /**
     * Method release Car Step 2 Update
     *
     */
    private void releaseCarStep2Update() {
        if (alift.getaShuttle().isOnFloor()) {
            drawFloor();
            if (alift.getaShuttle().isFinish())//lift go down
            {
                if (!flag) {
                    alift.getaShuttle().setMovePosition();
                    alift.getaShuttle().setBayOfFloor(bayTarget, currentFloor);//center of bay
                    flag = true;
                } else {//done collect Car
                    //liftdown
                    alift.setTarget(0);
                }
            }
            alift.draw(g2);
        } else {
            if (alift.getaShuttle().isOnGround()) {
                startUpdate();
                if (alift.isAtTarget()) {
                    changeState(Statement.STATE_RELEASE_CAR_STEP3_INIT);
                }
            }
        }

        alift.update(SystemSupport.dt);
    }

    /**
     * Method release Car Step 3 Init
     *
     */
    private void releaseCarStep3Init() {
        alift.getaShuttle().setMovePosition();
        alift.getaShuttle().setMoveHorizontal(SystemSupport.turnTableX);
        changeState(Statement.STATE_RELEASE_CAR_STEP3);
        flag = false;
    }

    /**
     * Method release Car Step 3 Update
     *
     */
    private void releaseCarStep3Update() {
        startUpdate();
        if (alift.getaShuttle().isFinish()) {
            changeState(Statement.STATE_RELEASE_CAR_STEP4_INIT);//move car out
        }
        alift.update(SystemSupport.dt);
    }

    /**
     * Method release Car Step 4 Init
     *
     */
    private void releaseCarStep4Init() {
        Room infoTrolley = alift.getaShuttle().getaTrolley().getInfo();
        double Y = infoTrolley.getY() + infoTrolley.getRoomLength() / 2;
        double to = -8.625 - (-2.875);//B5-B1
        alift.getaShuttle().getaTrolley().setNewMove(Y, to);

        changeState(Statement.STATE_RELEASE_CAR_STEP4);
        flag = false;
    }

    /**
     * Method release Car Step 4 Update
     *
     */
    private void releaseCarStep4Update() {
        startUpdate();
        if (alift.getaShuttle().getaTrolley().isFinish()) {
            isProgess = false;
        }
        alift.update(SystemSupport.dt);
    }
}
