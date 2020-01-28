/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aaproject.Map;

import Soldier.UsMarine;
import Soldier.VietCongInfantry;
import java.util.ArrayList;

/**
 *
 * @author Darren
 */
public class MapUnit {

    private volatile boolean occupancy = false;
    private String border = null;
    private volatile UsMarine marine = null;
    private volatile VietCongInfantry vc = null;
    private int row;
    private int column;

    public MapUnit(String type, int row, int column) {
        this.row = row;
        this.column = column;
        if (type.equals("North Border")) {
            this.border = "N";
            this.occupancy = true;
        } else if (type.equals("South Border")) {
            this.border = "S";
            this.occupancy = true;
        } else if (type.equals("East Border")) {
            this.border = "E";
            this.occupancy = true;
        } else if (type.equals("West Border")) {
            this.border = "W";
            this.occupancy = true;
        }
    }

    @Override
    public synchronized String toString() {
        if (border != null) {
            return border;
        } else {
            if (marine != null) {
                if (marine.isDead()) {
                    return "D";
                } else {
                    return "M";
                }

            } else if (vc != null) {
                if (vc.isDead()) {
                    return "D";
                } else {
                    return "V";
                }
            }
        }
        return " ";

    }

    public synchronized int getRow() {
        return row;
    }

    public synchronized int getColumn() {
        return column;
    }

    public synchronized void markOccupied() {
        this.occupancy = true;
    }

    public synchronized void insertVc(VietCongInfantry vc) {
        this.vc = vc;

    }

    public synchronized void insertMarine(UsMarine marine) {
        this.marine = marine;

    }

    public synchronized VietCongInfantry getVc() {
        return vc;
    }

    public synchronized UsMarine getMarine() {
        return marine;
    }

    public synchronized void markNotOccupied() {
        this.occupancy = false;
    }

    public synchronized boolean getOccupancy() {
        return this.occupancy;
    }

    public synchronized void leaveVc() {
        vc = null;
    }

    public synchronized void leaveMarine() {
        marine = null;
    }
}
