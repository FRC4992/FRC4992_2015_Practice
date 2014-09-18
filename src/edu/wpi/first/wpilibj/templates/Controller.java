package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {
    
    Joystick joy;
    
    public double leftStickX(){
        return joy.getRawAxis(1);
    }
    public double leftStickY(){
        return joy.getRawAxis(2);
    }
    public boolean ButtonA(){
        return joy.getRawButton(1);
    }
    public boolean ButtonB(){
        return joy.getRawButton(2);
    }
    public boolean ButtonX(){
        return joy.getRawButton(3);
    }
    public boolean ButtonY(){
        return joy.getRawButton(4);
    }
    public boolean LeftButton(){
        return joy.getRawButton(5);
    }
    public boolean RightButton(){
        return joy.getRawButton(6);
    }
    public Controller(int i){
        joy = new Joystick(i);
    }
}
