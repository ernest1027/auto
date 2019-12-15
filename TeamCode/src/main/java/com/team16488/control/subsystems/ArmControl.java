package com.team16488.control.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.team16488.skystone.Robot;

public class ArmControl {
    /**
     * Robot Class Object
     */
    private Robot robot;
    /**
     * Sets the Virtical rotation power
     *
     * @see com.team16488.library.subsystems.ArmHead
     */
    private double vPower;
    /**
     * Sets the Horizontal rotation power
     *
     * @see com.team16488.library.subsystems.ArmHead
     */
    private double hPower;
    /**
     * Sets the position of the claw
     *
     * @see com.team16488.library.subsystems.ArmHead
     */
    private boolean clawOpen = true;

    private Gamepad subsystemDriver;

    public ArmControl(OpMode opMode, Robot robot) {
        this.robot = robot;
        subsystemDriver = opMode.gamepad2;

    }

    public void armControl() {

        if (subsystemDriver.right_bumper) {
            clawOpen = true;
        }
        if (clawOpen) {
            robot.armHead.setOpen(true);
        }
        if (!clawOpen) {
            robot.armHead.setOpen(false);
        }

        if (subsystemDriver.x) {
            robot.arm.setPower(1.0);
        }
        if (subsystemDriver.y) {
            robot.arm.setPower(-1.0);
        }
        if (subsystemDriver.a) {
            // reset pos using encoders
        }


        if (subsystemDriver.dpad_up) {
            vPower += 0.1;
        }
        if (subsystemDriver.dpad_down) {
            vPower -= 0.1;
        }
        if (subsystemDriver.dpad_left) {
            hPower += 0.1;
        }

        if (subsystemDriver.dpad_right) {
            hPower -= 0.1;
        }

        robot.armHead.setverticalRotation(vPower);
        robot.armHead.sethorizontalRotationPosition(hPower);


        if (subsystemDriver.x) {
            clawOpen = true;
        }
        if (subsystemDriver.b) {
            clawOpen = false;
        }
        if (clawOpen) {
            robot.armHead.setOpen(true);
        }
        if (!clawOpen) {
            robot.armHead.setOpen(false);
        }


        robot.arm.setPower(-subsystemDriver.right_stick_y);


    }
}
