/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

public class Shoot extends Thread {

    Victor victor;
    Solenoid sol4, sol5, sol7, sol8;
    int count;
    Unload Unload;
    public final int CATAPAULT_EXTENSION_TIME = 150;

    public Shoot(Solenoid s4, Solenoid s5, Solenoid s7, Solenoid s8) {
        sol4 = s4;
        sol5 = s5;
        sol7 = s7;
        sol8 = s8;

        count = 0;
    }

    public void setCountToZero() {
        count = 0;
    }

    public void Shoot() {
        if (count < CATAPAULT_EXTENSION_TIME) {
            count++;
            catapaultShoot();
        }
        if (count >= CATAPAULT_EXTENSION_TIME) {
            catapaultUnshoot();
        }
    }

    public void catapaultShoot() {
        sol7.set(false);
        sol8.set(true);
    }

    public void catapaultUnshoot() {
        sol7.set(true);
        sol8.set(false);
    }
}
