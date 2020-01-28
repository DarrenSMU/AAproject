/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Soldier;

import aaproject.Map.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darren
 */
public class marineCallable implements Callable<Boolean> {

    private UsMarine m;
    private CyclicBarrier gate;

    public marineCallable(UsMarine m, CyclicBarrier gate) {
        this.m = m;
        this.gate = gate;
    }

    @Override
    public Boolean call() {
        try {
            gate.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(marineCallable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
            Logger.getLogger(marineCallable.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (!m.isDead()) {

            while (m.hasTarget() && !m.isDead()) {
                try {
                    m.shoot();
                } catch (NullPointerException ne) {
                    System.out.println(m.getName() + " Missed");

                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(marineCallable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            m.lookAround();
            while (m.hasTarget() && !m.isDead()) {
                try {
                    m.shoot();
                } catch (NullPointerException ne) {
                    System.out.println(m.getName() + " Missed");

                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(marineCallable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!m.hasTarget() && !m.isDead()) {
                m.walk();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(marineCallable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return m.isDead();

    }

}
