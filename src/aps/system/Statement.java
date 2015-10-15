package aps.system;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 


public class Statement {

    static final int STATE_STARTUP = -1;
    static final int STATE_STARTUPDATE = 0;
    static final int STATE_ENTER_INIT = 1;
    static final int STATE_ENTER = 2;

    static final int STATE_TURN_CAR_INIT = 3;
    static final int STATE_TURN_CAR = 4;

    static final int STATE_COLLECT_CAR_INIT = 5;
    static final int STATE_COLLECT_CAR = 6;

    static final int STATE_MOVE_TO_LIFT_INIT = -7;
    static final int STATE_MOVE_TO_LIFT = -8;

    static final int STATE_MOVE_LIFT_INIT = 7;
    static final int STATE_MOVE_LIFT = 8;

    static final int STATE_MOVE_TO_BAY_INIT = 9;
    static final int STATE_MOVE_TO_BAY = 10;

    static final int STATE_RELEASE_CAR_INIT = 11;
    static final int STATE_RELEASE_CAR = 12;
    static final int STATE_RELEASE_CAR_STEP2_INIT = 13;
    static final int STATE_RELEASE_CAR_STEP2 = 14;
    static final int STATE_RELEASE_CAR_STEP3_INIT = 15;
    static final int STATE_RELEASE_CAR_STEP3 = 16;
    static final int STATE_RELEASE_CAR_STEP4_INIT = 17;
    static final int STATE_RELEASE_CAR_STEP4 = 18;
//    static final int STATE_PARKING = 6;
//    static final int STATE_DELIVER_CAR = 7;
    static final int STATE_PAUSE = 9999;
    static final int STATE_TEST_DRAW = 1000;

    public static int currentState;

    /**
     * Statement Constructor
     *
     * @param currentState A parameter
     */
    public Statement(int currentState) {
        this.currentState = currentState;

    }

    /**
     * Get the value of Statement
     *
     * @return the value of Statement
     */
    public static int getStatement() {
        return currentState;
    }

    /**
     * Set the value of Statement
     *
     * @param Statement new value of Statement
     */
    public void setStatement(int currentState) {
        this.currentState = currentState;
    }

}
