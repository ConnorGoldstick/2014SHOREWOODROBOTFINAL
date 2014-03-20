/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Drive extends Thread {

    Jaguar jagleft1, jagright3, jagleft2, jagright4;
    Solenoid sol1, sol2;
    DigitalOutput speedColor;
    Joystick xBox;
    boolean running = false;
    double speed, turn, leftspeed, rightspeed;
    public final double DEADZONE_AMOUNT_TURN = 0.1;
    public final double TURNING_COEFFICENT = 0.5;
    public final double RAMP_AMOUNT = 0.1;
    public final double STOP_SKIPPING = 0.075;
    public final double DEAD_ZONE = 0.05;

    public Drive(Jaguar j1, Jaguar j2, Jaguar j3, Jaguar j4, Solenoid s1, Solenoid s2, Joystick x, DigitalOutput s) {
        jagleft1 = j1;
        jagleft2 = j2;
        jagright3 = j3;
        jagright4 = j4;
        sol1 = s1;
        sol2 = s2;
        xBox = x;
        speedColor = s;
    }

    public void setRun(boolean run) {
        running = run;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (Exception ex) {
            }
            sol1.set(false);
            sol2.set(true);
            while (running) {
                try {
                    Thread.sleep(10);
                } catch (Exception ex) {
                }
                boolean aButton = xBox.getRawButton(1);
                boolean bButton = xBox.getRawButton(2);
                if (xBox.getRawAxis(2) >= 0) {
                    speed = Math.sqrt(xBox.getRawAxis(2) * xBox.getRawAxis(2) * xBox.getRawAxis(2));
                } else {
                    speed = -Math.sqrt(-xBox.getRawAxis(2) * (xBox.getRawAxis(2) * xBox.getRawAxis(2)));
                }
                turn = TURNING_COEFFICENT * xBox.getRawAxis(4); //may need to adjust this
                if (turn < DEADZONE_AMOUNT_TURN && turn > -DEADZONE_AMOUNT_TURN) { //maybe need to adjust this
                    turn = 0;
                }
                if (aButton) {
                    slowGear();
                }
                if (bButton) {
                    fastGear();
                }

                leftJagSet();
                rightJagSet();

                setSpeed();
            }
        }
    }

    public void rightJagSet() {
        if (Math.abs((speed + turn) - leftspeed) < STOP_SKIPPING) { //don't skip target
            rightspeed = speed + turn;
        }
        if (rightspeed < speed + turn) { //ramp up
            rightspeed = rightspeed + .1;
        }
        if (rightspeed > speed + turn) {//ramp down
            rightspeed = rightspeed - .1;
        }
        if (Math.abs(rightspeed) < DEAD_ZONE) { //dead zone
            rightspeed = 0;
        }
    }

    public void leftJagSet() {
        if (Math.abs((speed - turn) - leftspeed) < STOP_SKIPPING) { //don't skip target
            leftspeed = speed - turn;
        }
        if (leftspeed < speed - turn) { //ramp up
            leftspeed = leftspeed + RAMP_AMOUNT;
        }
        if (leftspeed > speed - turn) { //ramp down
            leftspeed = leftspeed - RAMP_AMOUNT;
        }
        if (Math.abs(leftspeed) < DEAD_ZONE) { //dead zone
            leftspeed = 0;
        }
    }

    public void setSpeed() {
        jagleft1.set(leftspeed);
        jagleft2.set(leftspeed);
        jagright3.set(-(rightspeed));
        jagright4.set(-(rightspeed));
    }

    public void fastGear() {
        sol1.set(true);
        sol2.set(false);
        speedColor.set(true);
    }

    public void slowGear() {
        sol1.set(false);
        sol2.set(true);
        speedColor.set(false);
    }
}