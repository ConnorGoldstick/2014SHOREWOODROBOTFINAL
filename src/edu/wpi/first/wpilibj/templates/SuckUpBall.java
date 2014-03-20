/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

public class SuckUpBall {

    Victor victor;
    Solenoid sol4, sol5;
    public final double VICTOR_SPEED_WHILE_SUCTIONING = -0.4;

    public SuckUpBall(Victor v, Solenoid s4, Solenoid s5) {
        victor = v;
        sol4 = s4;
        sol5 = s5;
    }

    public void suckItUp() {
        victor.set(VICTOR_SPEED_WHILE_SUCTIONING);
        turnSuctionyOn();
    }
    
    public void turnSuctionyOn(){
        sol4.set(true);
        sol5.set(false);
    }
}
