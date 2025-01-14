/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.team16488.opmodes.auto;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team16488.skystone.Robot;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="EncoderPID", group="Pushbot")

public class testEncoderPID extends LinearOpMode {

    /* Declare OpMode members. */

    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 0.3 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.1 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    private DcMotorEx FrontLeftMotor;
    private DcMotorEx FrontRightMotor;
    private DcMotorEx RearLeftMotor;
    private DcMotorEx RearRightMotor;

    public static final double NEW_P = 1.0;
    public static final double NEW_I = 0;
    public static final double NEW_D = 0;
    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        FrontLeftMotor = (DcMotorEx)hardwareMap.get(DcMotorEx.class, "FL");
        FrontRightMotor = (DcMotorEx)hardwareMap.get(DcMotorEx.class, "FR");
        RearRightMotor = (DcMotorEx)hardwareMap.get(DcMotorEx.class, "BR");
        RearLeftMotor = (DcMotorEx)hardwareMap.get(DcMotorEx.class, "BL");
        FrontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RearLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();



        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        FrontLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        FrontRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        RearLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        RearRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        FrontLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        FrontRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        RearLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        RearRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                FrontLeftMotor.getCurrentPosition(),
                FrontRightMotor.getCurrentPosition());
        telemetry.update();
        PIDCoefficients pidOrig = FrontLeftMotor.getPIDCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER);
        PIDCoefficients pidNew = new PIDCoefficients(NEW_P, NEW_I, NEW_D);
        FrontLeftMotor.setPIDCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidNew);
        FrontRightMotor.setPIDCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidNew);
        RearRightMotor.setPIDCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidNew);
        RearLeftMotor.setPIDCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidNew);
        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        encoderDrive(DRIVE_SPEED,  48,  48, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
        encoderMecanumDrive(TURN_SPEED,   true, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
        encoderDrive(DRIVE_SPEED, -24, -24, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout


        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        FrontLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        FrontRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        RearLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        RearRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        FrontLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        FrontRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        RearLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        RearRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = FrontLeftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = FrontRightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            FrontLeftMotor.setTargetPosition(newLeftTarget);
            FrontRightMotor.setTargetPosition(newRightTarget);
            RearLeftMotor.setTargetPosition(newLeftTarget);
            RearRightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            FrontLeftMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            FrontRightMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            RearLeftMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            RearRightMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            FrontLeftMotor.setPower(Math.abs(speed));
            FrontRightMotor.setPower(Math.abs(speed));
            RearLeftMotor.setPower(Math.abs(speed));
            RearRightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (FrontLeftMotor.isBusy() && FrontRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        FrontLeftMotor.getCurrentPosition(),
                        FrontRightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            FrontLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);
            RearLeftMotor.setPower(0);
            RearRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            FrontLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            FrontRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            RearLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            RearRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
    public void encoderMecanumDrive(double speed,
                                    boolean direction, double Inches,
                                    double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        FrontLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        FrontRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        RearLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        RearRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        FrontLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        FrontRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        RearLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        RearRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        // Ensure that the opmode is still active
        if (opModeIsActive()) {


            // Determine new target position, and pass to motor controller
            newLeftTarget = FrontLeftMotor.getCurrentPosition() + (int)(Inches * COUNTS_PER_INCH);
            newRightTarget = FrontRightMotor.getCurrentPosition() + (int)(Inches * COUNTS_PER_INCH);

            if(direction)
            {
                newRightTarget = newRightTarget*(-1);
            }
            else
            {
                newLeftTarget = newLeftTarget*(-1);
            }
            FrontLeftMotor.setTargetPosition(newLeftTarget);
            FrontRightMotor.setTargetPosition(newRightTarget);
            RearLeftMotor.setTargetPosition(newRightTarget);
            RearRightMotor.setTargetPosition(newLeftTarget);

            // Turn On RUN_TO_POSITION
            FrontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FrontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RearLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RearRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            FrontLeftMotor.setPower(Math.abs(speed));
            FrontRightMotor.setPower(Math.abs(speed));
            RearLeftMotor.setPower(Math.abs(speed));
            RearRightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (FrontLeftMotor.isBusy() && FrontRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        FrontLeftMotor.getCurrentPosition(),
                        FrontRightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            FrontLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);
            RearLeftMotor.setPower(0);
            RearRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            FrontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FrontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RearLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RearRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
