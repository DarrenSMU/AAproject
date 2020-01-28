/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aaproject;

import Soldier.UsMarine;
import Soldier.VietCongInfantry;
import Soldier.marineCallable;
import Soldier.vcCallable;
import aaproject.Map.*;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darren
 */
public class AAproject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        final CyclicBarrier gate = new CyclicBarrier(11);

        Map map = new Map();
        //make Vc starting from south 
        VietCongInfantry vc1 = new VietCongInfantry(map, 29, 6, "Thuc Vey");
        VietCongInfantry vc2 = new VietCongInfantry(map, 29, 10, "Soc bie");
        VietCongInfantry vc3 = new VietCongInfantry(map, 29, 20, "ho chihn minh");
        VietCongInfantry vc4 = new VietCongInfantry(map, 28, 16, "Thuy");
        VietCongInfantry vc5 = new VietCongInfantry(map, 28, 12, "So ciet");

        //make UsMarine starting from north
        UsMarine us1 = new UsMarine(map, 1, 6, "blackie");
        UsMarine us2 = new UsMarine(map, 1, 10, "full retard");
        UsMarine us3 = new UsMarine(map, 1, 20, "kennedy");
        UsMarine us4 = new UsMarine(map, 2, 15, "Captain john");
        UsMarine us5 = new UsMarine(map, 2, 25, "jack");

        VietCongInfantry.myLock.lock();
        UsMarine.myLock.lock();
        try {
            System.out.print(map);
        } finally {
            VietCongInfantry.myLock.unlock();
            UsMarine.myLock.unlock();
        }

        //make viet cong army
        FutureTask<Boolean> vcTask1 = new FutureTask<>(new vcCallable(vc1, gate));
        Thread vcguerrila1 = new Thread(vcTask1);
        FutureTask<Boolean> vcTask2 = new FutureTask<>(new vcCallable(vc2, gate));
        Thread vcguerrila2 = new Thread(vcTask2);
        FutureTask<Boolean> vcTask3 = new FutureTask<>(new vcCallable(vc3, gate));
        Thread vcguerrila3 = new Thread(vcTask3);
        FutureTask<Boolean> vcTask4 = new FutureTask<>(new vcCallable(vc4, gate));
        Thread vcguerrila4 = new Thread(vcTask4);
        FutureTask<Boolean> vcTask5 = new FutureTask<>(new vcCallable(vc5, gate));
        Thread vcguerrila5 = new Thread(vcTask5);

        //make Us army
        FutureTask<Boolean> marineTask1 = new FutureTask<>(new marineCallable(us1, gate));
        Thread usMarine1 = new Thread(marineTask1);
        FutureTask<Boolean> marineTask2 = new FutureTask<>(new marineCallable(us2, gate));
        Thread usMarine2 = new Thread(marineTask2);
        FutureTask<Boolean> marineTask3 = new FutureTask<>(new marineCallable(us3, gate));
        Thread usMarine3 = new Thread(marineTask3);
        FutureTask<Boolean> marineTask4 = new FutureTask<>(new marineCallable(us4, gate));
        Thread usMarine4 = new Thread(marineTask4);
        FutureTask<Boolean> marineTask5 = new FutureTask<>(new marineCallable(us5, gate));
        Thread usMarine5 = new Thread(marineTask5);

        //start vietnam army thread
        vcguerrila1.start();
        vcguerrila2.start();
        vcguerrila3.start();
        vcguerrila4.start();
        vcguerrila5.start();

        //start Us army thread  
        usMarine1.start();
        usMarine2.start();
        usMarine3.start();
        usMarine4.start();
        usMarine5.start();

        try {
            gate.await();
        } catch (BrokenBarrierException ex) {
            Logger.getLogger(AAproject.class.getName()).log(Level.SEVERE, null, ex);
        }

        //join threads
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        boolean endSimulator = false;

        while (!endSimulator) {

            VietCongInfantry.myLock.lock();
            UsMarine.myLock.lock();
            try {
                System.out.print(map);
            } finally {
                VietCongInfantry.myLock.unlock();
                UsMarine.myLock.unlock();
            }

            if (vcTask1.isDone() && vcTask2.isDone() && vcTask3.isDone() && vcTask4.isDone() && vcTask5.isDone()) {
                Thread.sleep(5000);
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("US WON THE VIETNAM WAR");
                endSimulator = true;
            }
            if (marineTask1.isDone() && marineTask2.isDone() && marineTask3.isDone() && marineTask4.isDone() && marineTask5.isDone()) {
                Thread.sleep(5000);
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("VIET CONG WON THE VIETNAM WAR");
                endSimulator = true;
            }

            if (!endSimulator) {

                System.out.println();
                System.out.println("HP of VC1 " + vc1.getHp());
                System.out.println("HP of VC2 " + vc2.getHp());
                System.out.println("HP of VC3 " + vc3.getHp());
                System.out.println("HP of VC4 " + vc4.getHp());
                System.out.println("HP of VC5 " + vc5.getHp());
                System.out.println("HP of marine1 " + us1.getHp());
                System.out.println("HP of marine2 " + us2.getHp());
                System.out.println("HP of marine3 " + us3.getHp());
                System.out.println("HP of marine4 " + us4.getHp());
                System.out.println("HP of marine5 " + us5.getHp());
                Thread.sleep(2000);
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            }
        }

    }

}
