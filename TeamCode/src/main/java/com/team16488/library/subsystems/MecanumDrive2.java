package com.team16488.library.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class MecanumDrive2 extends Subsystem {

    private static final String[] MOTOR_NAMES = {"FR", "BR", "FL", "BL"};
    public DcMotorEx[] motors;
    private double[] powers;

    public MecanumDrive2(HardwareMap map) {
        powers = new double[4];
        motors = new DcMotorEx[4];

        for (int i = 0; i < 4; i++) {
            DcMotorEx dcMotor = (DcMotorEx)map.get(DcMotor.class, MOTOR_NAMES[i]);
            motors[i] = dcMotor;
            motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        motors[2].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[3].setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void setVelocity(double leftstickx, double leftsticky, double rightstickx) {
        powers[0] = leftsticky - rightstickx + leftstickx;
        powers[1] = leftsticky + rightstickx - leftstickx;
        powers[2] = leftsticky - rightstickx - leftstickx;
        powers[3] = leftsticky + rightstickx + leftstickx;
    }

    private void setMotors(double FL, double FR, double BL, double BR) {
        motors[0].setVelocity(FR*14000);
        motors[3].setVelocity(BR*14400);
        motors[1].setVelocity(FL*14400);
        motors[2].setVelocity(BL*14400);
    }

    public void Stop() {
        motors[0].setVelocity(0);
        motors[3].setVelocity(0);
        motors[1].setVelocity(0);
        motors[2].setVelocity(0);
    }

    @Override
    public void update() {
        setMotors(powers[0], powers[2], powers[1], powers[3]);
    }
}
