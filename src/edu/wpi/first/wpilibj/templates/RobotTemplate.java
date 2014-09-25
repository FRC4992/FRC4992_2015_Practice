//Adam Kaminski, the programmer, made a change to this .java file.

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    
    Controller Driver = new Controller(1);
    RobotDrive chassis = new RobotDrive(1, 2, 3, 4);
    Compressor comp = new Compressor(1,1);
    Preferences pref = Preferences.getInstance();
    DriverStationLCD dsl = DriverStationLCD.getInstance();
    Victor armMotor = new Victor(5);     
    DoubleSolenoid armPistonRight = new DoubleSolenoid(1,2);
    DoubleSolenoid armPistonLeft = new DoubleSolenoid(3,4);
    DoubleSolenoid launchPiston = new DoubleSolenoid(5,6);
    double sensitivity = 0.75;
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomousInit() {
        chassis.setSafetyEnabled(false);
        launchPiston.set(DoubleSolenoid.Value.kReverse);
        comp.start();
        chassis.drive(0.6, 0);
        Timer.delay(1.5);
        chassis.drive(0.0, 0.0);
    } 
    
    /**
     * This function is called periodically while the robot is in autonomous mode.
     */
    public void autonomousPeriodic(){
    }
    /**
     * This function is called once each time the robot enters operator control.
     */
    public void teleopInit() {
    SmartDashboard.putNumber("Slider 1", sensitivity*100);
    armPistonRight.set(DoubleSolenoid.Value.kReverse);
    armPistonLeft.set(DoubleSolenoid.Value.kReverse);
    launchPiston.set(DoubleSolenoid.Value.kReverse);
    comp.start();
    }
    
    /**
     * This function is called periodically while the robot is in teleop mode.
     */
    public void teleopPeriodic(){
        sensitivity=(Math.ceil(SmartDashboard.getNumber("Slider 1"))/100);
        if(Math.sqrt((Driver.leftStickY()*Driver.leftStickY())+(Driver.leftStickX()*Driver.leftStickX()))>0.2){
            chassis.arcadeDrive((Driver.ButtonA()? 1: -1)*Driver.leftStickY()*(Driver.ButtonX()? 1: sensitivity), -1*Driver.leftStickX()*(Driver.ButtonX()? 1: sensitivity)); 
        }
        else {
            chassis.stopMotor();
        }
        UserMessage(2, Driver.ButtonA()? "Reverse": "Forward");
        if (Driver.LeftButton()&&!previousLeftButton){
            if (armPosition){
                armPosition = false;
                armPistonRight.set(DoubleSolenoid.Value.kForward);
                armPistonLeft.set(DoubleSolenoid.Value.kForward);
                armMotor.set(-1.0);
                UserMessage(3, "Arm Down");
            }
            else{
                armPosition = true;
                armPistonRight.set(DoubleSolenoid.Value.kReverse);
                armPistonLeft.set(DoubleSolenoid.Value.kReverse);
                armMotor.set(0.0);
                if (launchPosition == false){
                    launchPiston.set(DoubleSolenoid.Value.kReverse);
                    launchPosition = true;
                    UserMessage(4, "Launcher Down");
                }
                
                UserMessage(3, "Arm Up  ");
            }
        }
        if (Driver.RightButton()&&!previousRightButton){
            if (launchPosition){
                launchPosition = false;
                launchPiston.set(DoubleSolenoid.Value.kForward);
                UserMessage(4, "Launcher Up  ");
            }
            else{
                launchPosition = true;
                launchPiston.set(DoubleSolenoid.Value.kReverse);
                UserMessage(4, "Launcher Down");
            }
        }
        if (Driver.ButtonY()&&!previousYButton){
            if (checkArmMotor){
                armMotor.set(-1.0);
                checkArmMotor=false;
                UserMessage(5, "Arm Motor On  ");
            }
            else{
                armMotor.set(0.0);
                checkArmMotor=true;
                UserMessage(5, "Arm Motor  Off");
            }
        }
            
            if (Driver.ButtonB()&&!previousBButton){
            if (checkArmMotor){
                armMotor.set(1.0);
                checkArmMotor=false;
                UserMessage(5, "Arm Motor On  ");
            }
            else{
                armMotor.set(0.0);
                checkArmMotor=true;
                UserMessage(5, "Arm Motor  Off");
            }
        }
        previousRightButton = Driver.RightButton();
        previousLeftButton = Driver.LeftButton();
        previousAButton = Driver.ButtonA();
        previousYButton = Driver.ButtonY();
        previousBButton = Driver.ButtonB();
        dsl.updateLCD();
    }
    
    /**
     * This function is called once each time the robot is disabled.
     */
    public void disableInit(){
        comp.stop();
        armPistonRight.set(DoubleSolenoid.Value.kOff);
        armPistonLeft.set(DoubleSolenoid.Value.kOff);
        launchPiston.set(DoubleSolenoid.Value.kOff);
    }
    
    /**
     * This function prints information from the robot to the DriverStation.
     */
    public void UserMessage(int line, String message){
        for (int i = 0; 22 < message.length()|| i >30; i++) message+= " ";
        if (line==1)dsl.println(DriverStationLCD.Line.kUser1, 1, message);
        else if (line==2)dsl.println(DriverStationLCD.Line.kUser2, 1, message);
        else if (line==3)dsl.println(DriverStationLCD.Line.kUser3, 1, message);
        else if (line==4)dsl.println(DriverStationLCD.Line.kUser4, 1, message);
        else if (line==5)dsl.println(DriverStationLCD.Line.kUser5, 1, message);
    }
}
