///*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.-
 */
public class CentralCode extends IterativeRobot {

    final int autoMoveForwardTime = 1; // needs testing
    final double AUTO_MOVE_FORWARD_SPEED = 0.6;
    final double AUTO_DISTANCE = 0.85;
    final int AUTO_GYRO_REDUCTION = 90;
    Timer autoTimer;
    Jaguar jag1, jag2, jag3, jag4;
    Joystick xBox;
    Victor victor;
    Solenoid sol1, sol2, sol4, sol5, sol7, sol8;
    Relay relay/*
             * , relayCompressor
             */;
    DigitalInput digi14, digi13, digi3;
    DigitalOutput teamColor, speedColor;
    AnalogChannel ultrasonic, encoder;
    Gyro gyro;
    boolean inRange, tooClose, tooFar;
    Drive drive;
    loadAndShoot loadAndShoot;
    SmartDashboard smart;
    Compressor compressor;
    DriverStation driverstation;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        autoTimer = new Timer();
        jag1 = new Jaguar(1, 1);
        jag2 = new Jaguar(1, 2);
        jag3 = new Jaguar(1, 3);
        jag4 = new Jaguar(1, 4);
        victor = new Victor(5);

        sol1 = new Solenoid(1);
        sol2 = new Solenoid(2);

        sol4 = new Solenoid(4);
        sol5 = new Solenoid(5);

        sol7 = new Solenoid(7);
        sol8 = new Solenoid(8);

        relay = new Relay(1, 1);

        digi14 = new DigitalInput(1, 14);
        digi13 = new DigitalInput(1, 12);
        digi3 = new DigitalInput(1, 3);
        
        driverstation = DriverStation.getInstance();
        teamColor = new DigitalOutput(1, 7);
        speedColor = new DigitalOutput(1, 8);

        encoder = new AnalogChannel(2);
        ultrasonic = new AnalogChannel(3);

        gyro = new Gyro(1);
        gyro.setSensitivity(0.007);
        gyro.reset();

        xBox = new Joystick(1);



        drive = new Drive(jag1, jag2, jag3, jag4, sol1, sol2, xBox, speedColor);
        loadAndShoot = new loadAndShoot(encoder, victor, sol4, sol5, sol7, sol8, xBox, digi14, digi13, digi3, smart);

        drive.start();
        loadAndShoot.start();
        compressor = new Compressor(1, 13, 1, 8);
        compressor.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit() {
        //relayCompressor.set(Relay.Value.kOn);

        autoTimer.reset();
        autoTimer.start();

        gyro.reset();
        relay.set(Relay.Value.kOn);

        setSpeedFast();

        sol4.set(false);
        sol5.set(true);
        sol7.set(true);
        sol8.set(false);

        drive.setRun(false);
        loadAndShoot.setRun(false);
    }

    public void autonomousPeriodic() {
        drive.setRun(false);
        loadAndShoot.setRun(false);
        //    relay.set(Relay.Value.kOn);
        if (autoTimer.get() < autoMoveForwardTime) {
            autoForward();
            System.out.println("Moving forward, Timer at " + autoTimer.get() + ", Ultrasonic at " + ultrasonic.getAverageVoltage());
        }
        if (autoTimer.get() >= autoMoveForwardTime && ultrasonic.getAverageVoltage() <= AUTO_DISTANCE) {
            stop();
            shoot();
            System.out.println("Shooting, Ultrasonic at " + ultrasonic.getAverageVoltage());
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        //relayCompressor.set(Relay.Value.kOn);
        relay.set(Relay.Value.kOff);
        drive.setRun(true);
        loadAndShoot.teleoperatedInit();
        loadAndShoot.setRun(true);
        setLEDColour();
    }

    public void teleopPeriodic() {
        //relayCompressor.set(Relay.Value.kOn);

        //smart dashboard stuff
        smart.putBoolean("Fully Pressurized", compressor.getPressureSwitchValue());

        smart.putBoolean("fast gear", sol1.get() == true && sol2.get() == false);
        smart.putBoolean("slow gear", sol1.get() == false && sol2.get() == true);

        if (gyro.getAngle() > 360 || gyro.getAngle() < -360) {
            gyro.reset();
        }
        smart.putBoolean("good angle", gyro.getAngle() < 30 && gyro.getAngle() > -30);
        smart.putBoolean("too far right", (gyro.getAngle() > 30 && gyro.getAngle() < 180) || gyro.getAngle() < -180);
        smart.putBoolean("too far left", (gyro.getAngle() < -30 && gyro.getAngle() > -180) || gyro.getAngle() > 180);

        if (ultrasonic.getVoltage() < 0.5) {
            tooClose = true;
        } else {
            tooClose = false;
        }
        smart.putBoolean("Too close", tooClose);

        if (ultrasonic.getVoltage() > 1) {
            tooFar = true;
        } else {
            tooFar = false;
        }
        smart.putBoolean("Too far", tooFar);

        if (ultrasonic.getVoltage() >= 0.55 && ultrasonic.getVoltage() <= 0.8) {
            inRange = true;
        } else {
            inRange = false;
        }
        smart.putBoolean("In range", inRange);
    }

    public void disabledInit() {
        drive.setRun(false);
        loadAndShoot.resetBooleans();
        loadAndShoot.setRun(false);
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        //relayCompressor.set(Relay.Value.kOn);
        System.out.println(compressor.getPressureSwitchValue());
    }

    public void setSpeedFast() {
        sol1.set(true);
        sol2.set(false);
    }

    public void setSpeedSlow() {
        sol1.set(false);
        sol2.set(true);
    }

    public void shoot() {
        sol7.set(false);
        sol8.set(true);
    }

    public void stop() {
        jag1.set(0);
        jag2.set(0);
        jag3.set(0);
        jag4.set(0);
    }

    public void autoForward() {
        jag1.set(-AUTO_MOVE_FORWARD_SPEED);// - (gyro.getAngle()/AUTO_GYRO_REDUCTION));
        jag2.set(-AUTO_MOVE_FORWARD_SPEED);// - (gyro.getAngle()/AUTO_GYRO_REDUCTION));
        jag3.set(AUTO_MOVE_FORWARD_SPEED);// - (gyro.getAngle()/AUTO_GYRO_REDUCTION));
        jag4.set(AUTO_MOVE_FORWARD_SPEED);// - (gyro.getAngle()/AUTO_GYRO_REDUCTION));
    }
    
    public void setLEDColour(){
        System.out.println(driverstation.getAlliance());
        if (driverstation.getAlliance() == DriverStation.Alliance.kBlue) {
            teamColor.set(true);
        } else{
            teamColor.set(false);
        }
    }
}
