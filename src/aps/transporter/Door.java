/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps.transporter;

import aps.system.SystemSupport;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import sun.rmi.runtime.Log;

/**
 *
 * @author M
 */
public class Door {

    private double x;
    private double y;
    private double w;
    private double h;
    private double df;
    private Direction curDir = Direction.IDLE;
    private double halfWidth;

    public enum Direction {

        IDLE, OPENED, CLOSED, OPENING, CLOSING;
    }

    public Door(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        setCurDir(Direction.IDLE);
    }

    public Direction getCurDir() {
        return curDir;
    }

    public void setCurDir(Direction curDir) {
        this.curDir = curDir;
        if (this.curDir == Direction.OPENING) {
            this.halfWidth = w / 2;
            this.df = 0;
        } else if (this.curDir == Direction.CLOSING) {
            this.halfWidth = 0.1;
            this.df = 1.0;
        }

    }

    public void draw(Graphics2D g) {

        halfWidth = (0.1 + (w / 2 - 0.1) * (1.0 - df));

       // g.fillRect(x - w / 2, y - h / 2, halfWidth, h);
        //g.fillRect(x + w / 2 - halfWidth, y - h / 2, halfWidth, h);
        g.fill(new Rectangle2D.Double(x - w / 2, y - h / 2, halfWidth, h));
        g.fill(new Rectangle2D.Double(x + w / 2 - halfWidth, y - h / 2, halfWidth, h));

    }

    public void update(long dt) {
        if (getCurDir() == Direction.OPENING) {
            df += dt / (double) (1000 * SystemSupport.slidingDoorTime);
            if (df >= 1.0) {
                df = 1.0;
                setCurDir(Direction.OPENED);
            }
        } else if (getCurDir() == Direction.CLOSING) {
            df -= dt / (double) (1000 * SystemSupport.slidingDoorTime);
            if (df < 0) {
                df = 0;
                setCurDir(Direction.CLOSED);
            }
        }

    }
}
