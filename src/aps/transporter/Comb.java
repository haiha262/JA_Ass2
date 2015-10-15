/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps.transporter;

import aps.system.SystemSupport;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 *
 * @author M
 */
public class Comb {

    private double x;
    private double y;

    private double w;
    private double h;
    private double from;
    private double to;
    private double width = (double)SystemSupport.trolleyWidth;
    private double widthMax = (double)SystemSupport.trolleyWidth +(double) SystemSupport.combExtension * 2;
    private double defaultWidth;
    private double speedMove;

    public enum Comb_status {

        OFF, EXTEND, UNDO_EXTEND, RAISE
    }
    private Comb_status comb_status = Comb_status.OFF;

    public Comb(double x, double y, double w, double h ) {
    
        this.x = x;
        this.y = y;
        this.w = this.defaultWidth = width;

        this.h = h;

        setFinish(false);
        this.speedMove = (double) (1000 * SystemSupport.trolleySpeed);
    }

    public void setNewPostion(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g) {
        //draw comb
        double space = 0.15;//SystemSupport.metrePerPixel/5;
        if (getComb_status() == Comb_status.RAISE) {
            w = widthMax;
        }
            for (int i = 6; i <= 10; i++) {
                g.draw(new Line2D.Double(x - w / 2, y - h / 2 + i * space, x + w / 2, y - h / 2 + i * space));
                g.draw(new Line2D.Double(x - w / 2, y + h / 2 - i * space, x + w / 2, y + h / 2 - i * space));
            }
        

    }

    private boolean finish;

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public boolean isFinish() {
        return finish;
    }

    public Comb_status getComb_status() {
        return comb_status;
    }

    public void setComb_status(Comb_status comb_status) {
//        if (comb_status != Comb_status.OFF) {
//            setFinish(false);
//        }
        this.comb_status = comb_status;
    }

    public void update(long dt) {
        if (getComb_status() == Comb_status.EXTEND) {
            w +=  (10 * SystemSupport.trolleySpeed)/dt;
            if (w > widthMax) {
                w = widthMax;
                setComb_status(Comb_status.RAISE);
                setFinish(true);
            }
        } else if (getComb_status() == Comb_status.UNDO_EXTEND) {
            w -=  (10 * SystemSupport.trolleySpeed)/dt;
            if (w < defaultWidth) {
                w = defaultWidth;
                setComb_status(Comb_status.OFF);
                setFinish(true);
            }
        }

    }
}
