/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Soldier;

import aaproject.Map.Map;
import aaproject.Map.MapUnit;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Darren
 */
public class UsMarine {

    private int hp = 100;
    private int armor = 100;
    private String army = "US";
    private double accuracyAtMaxRange = 70.0;
    private int reloadTime = 2000;
    private int magazineCapacity = 20;
    private double damageMax = 55.0;
    private double fireRateDelay = 1000;
    private double effectiveRange = 10.0;
    private double visualRange = 18.0;
    private Map map;
    private MapUnit target = null;
    private int row;
    private int column;
    private Random r = new Random();
    private String name;
    public static Lock myLock = new ReentrantLock();

    public UsMarine(Map map, int startRow, int startColumn, String name) {
        this.map = map;
        this.row = startRow;
        this.column = startColumn;
        this.name = name;
        map.getMap()[row][column].insertMarine(this);
    }
    
    //get name of soldier
    public String getName() {
        return name;
    }
    
    //check whether soldier has a target
    public boolean hasTarget() {
        return target != null;
    }

    //look for a target
    public boolean lookAround() {
        for (int i = 1; i < map.getSize() - 1; i++) {
            for (int j = 1; j < map.getMap()[i].length - 1; j++) {
                if (Math.sqrt((i - row) * (i - row) + (j - column) * (j - column)) < effectiveRange && map.getMap()[i][j].toString().equals("V")) {
                    if (target == null) {
                        target = map.getMap()[i][j];
                        return true;
                    } else if (Math.sqrt((i - row) * (i - row) + (j - column) * (j - column)) < Math.sqrt((target.getRow() - row) * (target.getRow() - row) + (target.getColumn() - column) * (target.getColumn() - column)) && map.getMap()[i][j].toString().equals("V")) {
                        target = map.getMap()[i][j];
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //try to shoot at target
    public void shoot() throws NullPointerException {
        
        //check if target still in range
        if (Math.sqrt((target.getRow() - row) * (target.getRow() - row) + (target.getColumn() - column) * (target.getColumn() - column)) > effectiveRange || target.getVc().isDead()) {
            target = null;
            return;
        }
        
        //check if need to reload , if needed simulate reload
        if (magazineCapacity == 0) {
            try {
                Thread.sleep(reloadTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            magazineCapacity = 25;
        }
        
        //check if target still in range
        if (Math.sqrt((target.getRow() - row) * (target.getRow() - row) + (target.getColumn() - column) * (target.getColumn() - column)) > effectiveRange || target.getVc().isDead()) {
            target = null;
            return;
        }
        System.out.println(name + " shot at " + target.getVc().getName());
        
        //check if shot hit 
        if (r.nextInt(101) <= accuracyAtMaxRange) {
            target.getVc().hit();
            System.out.println(name + " injured " + target.getVc().getName() + " by " + damageMax + "Hp Points");
            magazineCapacity -= 1;
            if (target.getVc().isDead()) {
                target = null;
            }
        }
        try {
            Thread.sleep((long) fireRateDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //check if target still in range
        if (Math.sqrt((target.getRow() - row) * (target.getRow() - row) + (target.getColumn() - column) * (target.getColumn() - column)) > effectiveRange || target.getVc().isDead()) {
            target = null;
            return;
        }
    }
    
    //deduct hp on succesful hit 
    public void hit() {
        if (armor > 0) {
            armor -= damageMax;
        } else {
            hp -= damageMax;
        }
    }
    
    //return whether soldier is dead
    public boolean isDead() {
        return hp <= 0;
    }
    
    //return soldier hp
    public int getHp() {
        return hp;
    }

    //walk 1  space
    public void walk() {

        int desiredRow = row;
        int desiredColumn = column;

        while (map.getMap()[desiredRow][desiredColumn].getOccupancy()) {

            //walk towards enemy
            for (int i = 1; i < map.getSize() - 1; i++) {
                for (int j = 1; j < map.getMap()[i].length - 1; j++) {
                    if (Math.sqrt((i - row) * (i - row) + (j - column) * (j - column)) < visualRange && map.getMap()[i][j].toString().equals("V")) {
                        if (i > row) {
                            desiredRow += 1;
                        }
                        if (i < row) {
                            desiredRow -= 1;
                        }
                        if (j > column) {
                            desiredColumn += 1;
                        }
                        if (j < column) {
                            desiredColumn -= 1;
                        }
                    }
                }
            }
            if (row != desiredRow && !map.getMap()[desiredRow][desiredColumn].getOccupancy()) {
                break;
            }
            //OR walk randomly to enemy base
            if (desiredRow == row && row + 1 < map.getMap().length - 1) {
                desiredRow += 1;
                if (r.nextInt(2) == 0 && column + 1 < map.getMap()[0].length - 1) {
                    desiredColumn += 1;
                } else if (column - 1 > 1) {
                    desiredColumn -= 1;
                }
            } else {
                desiredRow = row - 1;
                if (r.nextInt(2) == 0 && column + 1 < map.getMap()[0].length - 1) {
                    desiredColumn += 1;
                } else if (column - 1 > 1) {
                    desiredColumn -= 1;
                }
            }
        }
        myLock.lock();
        try {
            map.getMap()[row][column].markNotOccupied();
            map.getMap()[row][column].leaveMarine();
            map.getMap()[desiredRow][desiredColumn].markOccupied();
            map.getMap()[desiredRow][desiredColumn].insertMarine(this);
            row = desiredRow;
            column = desiredColumn;
        } finally {
            myLock.unlock();
        }
    }

}
