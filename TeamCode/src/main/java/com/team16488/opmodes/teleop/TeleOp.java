package com.team16488.opmodes.teleop;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.team16488.control.Control;
import com.team16488.skystone.Robot;

/**
 * This is the Main Class that the robot looks for
 * this class calls all of the classes that are responsable for the chassis and subsytem control
 * this is the main script that the robot runs when you start the OpMode.
 * <p>
 * All Classes converge at this OpMode
 *
 * @author Parham Baghbanbashi: parhambagh@gmail.com
 * @author Ernest Wong
 * <p>github: https://github.com/StrRamsRobotics/SkyStone/tree/Parham-Baghbanbashi</p>
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "telop")
public class TeleOp extends OpMode {
    /**
     * Robot object
     *
     * <p>See: {@link com.team16488.skystone.Robot}</p>
     */
    private Robot robot;

    /**
     * This is what will be run on initialization of the robot
     * <p>
     * The main function of the method is to construct and create our objects
     * </p>
     */


    private Control control;

    public void init() {
        robot = new Robot(this, telemetry);
        control = new Control(this, robot);
        robot.lIftStageOne.LiftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lIftStageOne.LiftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    /**
     * This method runs once the driver hits start
     * <p>
     * This also runs robot.start
     *
     * <p>See: {@link Robot}</p>
     */
    @Override
    public void start() {
        robot.start();
    }

    /**
     * This method runs repetedly after the driver hits start
     * <p>
     * This method runs the Chassis control class and subsystem control class
     *
     */
    @Override
    public void loop() {
        control.control();
    }

    /**
     * This method Runs once the Driver hits stop
     *
     * Stops the robot
     * <p>See: {@link Robot}</p>
     */
    @Override
    public void stop() {
        robot.stop();
    }


}
