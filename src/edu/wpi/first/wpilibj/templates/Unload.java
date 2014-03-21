/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

public class Unload extends Thread {

    Victor victor;
    Solenoid sol4, sol5;
    public final double UNLOADING_SPEED = -0.5;

    public Unload(Victor v, Solenoid s4, Solenoid s5) {
        victor = v;
        sol4 = s4;
        sol5 = s5;
    }

    public void unload() {
        turnOffSuction();
        victor.set(UNLOADING_SPEED);
    }
    
    public void turnOffSuction(){
        sol4.set(false);
        sol5.set(true);
    }
}
