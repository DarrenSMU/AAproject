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
public class vcCallable implements Callable<Boolean> {

    private VietCongInfantry vc;
    private CyclicBarrier gate;

    public vcCallable(VietCongInfantry vc, CyclicBarrier gate) {
        this.vc = vc;
        this.gate = gate;
    }

    @Override
    public Boolean call() {
        try {
            gate.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(vcCallable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
            Logger.getLogger(vcCallable.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (!vc.isDead()) {

            while (vc.hasTarget() && !vc.isDead()) {
                try {
                    vc.shoot();
                } catch (NullPointerException ne) {
                    System.out.println(vc.getName() + " Missed");

                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(vcCallable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            vc.lookAround();
            while (vc.hasTarget() && !vc.isDead()) {
                try {
                    vc.shoot();
                } catch (NullPointerException ne) {
                    System.out.println(vc.getName() + " Missed");

                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(vcCallable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (!vc.hasTarget() && !vc.isDead()) {
                vc.walk();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(vcCallable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return vc.isDead();

    }

}
