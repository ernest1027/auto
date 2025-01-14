package api.team16488;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
public class MotorControl extends OpMode {

    public void setLeftSidePower(double power, DcMotor FrontLeftMotor, DcMotor RearLeftMotor) {
        FrontLeftMotor.setPower(power);
        RearLeftMotor.setPower(power);
    }

    public void setRightSidePower(double power, DcMotor FrontRightMotor, DcMotor RearRightMotor) {
        FrontRightMotor.setPower(power);
        RearRightMotor.setPower(power);
    }

    public void setFrontRightToBottomLeftDiagonalPower(double power, DcMotor FrontRightMotor, DcMotor RearLeftMotor){
        FrontRightMotor.setPower(power);
        RearLeftMotor.setPower(power);
    }

    public void setFrontLeftToBottomRightDiagonalPower(double power, DcMotor FrontLeftMotor , DcMotor RearRightMotor){
        FrontLeftMotor.setPower(power);
        RearRightMotor.setPower(power);
    }

    public void init(){}
    public void loop(){}

}
