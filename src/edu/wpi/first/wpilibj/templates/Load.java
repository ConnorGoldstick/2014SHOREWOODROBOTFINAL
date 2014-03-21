/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Victor;

public class Load extends Thread {

    Victor victor;
    AnalogChannel encoder;
    public final double LOADSPEED_WITH_BALL = 0.9;
    public final double LOADSPEED_WITHOUT_BALL = 0.5;
    public final double LOADSPEED_AFTER_APEX = 0.2;
    public final double ENCODER_ANGLE_STOP = 0.5;
    public final double ENCODER_ANGLE_THRESHOLD = 3;

    public Load(Victor v, AnalogChannel e) {
        victor = v;
        encoder = e;
    }

    public void loadWithBall() {
        if (encoder.getVoltage() > ENCODER_ANGLE_THRESHOLD || encoder.getVoltage() < ENCODER_ANGLE_STOP) {
            victor.set(LOADSPEED_WITH_BALL);
        } else {
            victor.set(LOADSPEED_AFTER_APEX);
        }
    }

    public void loadWithoutBall() {
        System.out.println("load2");
        if (encoder.getVoltage() > ENCODER_ANGLE_THRESHOLD || encoder.getVoltage() < ENCODER_ANGLE_STOP) {
            victor.set(LOADSPEED_WITHOUT_BALL);
            System.out.println("load3");
        } else {
            victor.set(LOADSPEED_AFTER_APEX);
            System.out.println("load4");
        }
    }
}
