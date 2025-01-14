package com.team16488.skystone;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ThreadPool;
import com.team16488.library.subsystems.Subsystem;
import com.team16488.library.subsystems.autonomous.ColourSensor;
import com.team16488.library.subsystems.autonomous.Vision;
import com.team16488.library.subsystems.AlternateIntake;
import com.team16488.library.subsystems.Claw;
import com.team16488.library.subsystems.Intake;
import com.team16488.library.subsystems.IntakeRaise;
import com.team16488.library.subsystems.LiftStageFourBar;
import com.team16488.library.subsystems.LiftStageOne;
import com.team16488.library.subsystems.MecanumDrive;
import com.team16488.library.subsystems.MecanumDrive2;
import com.team16488.library.subsystems.Puller;
import com.team16488.library.subsystems.autonomous.ColourSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * This class is what does all of the
 * requred threading for the vairous subsystems to
 * run in parrallel
 * <p>
 * In this class we call all our subsystems and
 * give each subsystem's update method a thread that runs via the runnable
 * this will be then submitted in the executor service.
 * </p>
 *
 * @author Parham Baghbanbashi
 * <p>github: https://github.com/StrRamsRobotics/SkyStone/tree/Parham-Baghbanbashi</p>
 */
public class Robot {
    /** MecanumDrive object*/
    public MecanumDrive drive;

    /** Claw subsystem object*/
    public Claw claw;
    /** Puller subsystem object*/
    public Puller puller;
    /** Intake subsystem object */
    public Intake intake;
    /**
     * LiftStageOne subsystem object
     */
    public LiftStageOne lIftStageOne;

    public MecanumDrive2 drive2;

    public ColourSensor colourSensor;

    public AlternateIntake alternateIntake;

    public LiftStageFourBar liftStageFourBar;

    public IntakeRaise intakeRaise;

    public Vision vision;

    /** The executor service that is responsible for the submitting of tasks (subsystem.update()) */
    private ExecutorService subsystemUpdateExecutor;
    /**
     * Starts the robot/ Checks if robot is started
     */
    private boolean started;
    /**
     * List of all the classes that extend the Subsystem Class(MecanumDrive, Arm, etc)*
     *
     * <p>See: {@link Subsystem}</p>
     */
    private List<Subsystem> subsystems;
    /** telemtry for the OpMode*/
    private Telemetry telemetry;
    /** Runnable responsible for creating threads for each subsystem in the list of subsystems(any class that extends Subsystem)*/
    private Runnable subsystemUpdateRunnable = () -> {

        while (!Thread.currentThread().isInterrupted()) {

            try {

                for (Subsystem subsystem : subsystems) {

                    if (subsystem == null) continue;


                    try {

                        subsystem.update();

                    } catch (Throwable t) {

                        this.telemetry.addData("Exception running thread 1", t);
                        this.telemetry.update();

                    }

                }

            } catch (Throwable t) {

                this.telemetry.addData("Exception running thread 2", "");
                this.telemetry.update();
            }

        }

    };

    /**
     * The Class constructor, This is what calls all our subsystems and allows us to access them and their methods inside our
     * ChassisControl and SubsystemControl classes
     *
     * Also adds a new single thread executor.
     *
     * @param opMode The OpMode that the Class is used in.
     * @param telemetry The telemetry of the OpMode the Class is used in
     */
    public Robot(OpMode opMode, Telemetry telemetry){
        this.telemetry = telemetry;

        subsystems = new ArrayList<>();
        try {
            drive2 = new MecanumDrive2(opMode.hardwareMap);
            subsystems.add(drive2);
        } catch (IllegalArgumentException e) {
            telemetry.addData("error runnning Drive", "look at robot or subsystem");
        }
        try {
            intakeRaise = new IntakeRaise(opMode.hardwareMap);
            subsystems.add(intakeRaise);
        } catch (IllegalArgumentException e) {
            telemetry.addData("error runnning intakeRaise", "look at robot or subsystem");

        }
        try {
            liftStageFourBar = new LiftStageFourBar(opMode.hardwareMap);
            subsystems.add(liftStageFourBar);
        } catch (IllegalArgumentException e) {
            telemetry.addData("error runnning shortStick", "look at robot or subsystem");

        }
/*
        try{
            drive = new MecanumDrive(opMode.hardwareMap);
            subsystems.add(drive);
        } catch (IllegalArgumentException e){

        }

*/

        try{
            claw = new Claw(opMode.hardwareMap);
            subsystems.add(claw);
        }catch(IllegalArgumentException e){
            telemetry.addData("error runnning claw", "look at robot or subsystem");

        }

        try{
            intake = new Intake(opMode.hardwareMap);
            subsystems.add(intake);
        }catch(IllegalArgumentException e){
            telemetry.addData("error runnning Intake", "look at robot or subsystem");

        }


        try {
            lIftStageOne = new LiftStageOne(opMode.hardwareMap);
            subsystems.add(lIftStageOne);
        }catch(IllegalArgumentException e){
            telemetry.addData("error runnning liftStageOne", "look at robot or subsystem");

        }

        try{
            puller = new Puller(opMode.hardwareMap);
            subsystems.add(puller);
        }catch(IllegalArgumentException e){
            telemetry.addData("error runnning pullers", "look at robot or subsystem");

        }
        try {
            alternateIntake = new AlternateIntake(opMode.hardwareMap);
            subsystems.add(alternateIntake);
        } catch (IllegalArgumentException e) {
        }


        try {
            colourSensor = new ColourSensor((opMode.hardwareMap));
            subsystems.add(colourSensor);
        } catch (IllegalArgumentException e) {

        }

     /*   try {
            vision = new Vision((opMode.hardwareMap));
            subsystems.add(vision);
        } catch (IllegalArgumentException e) {

        }*/

        subsystemUpdateExecutor = ThreadPool.newSingleThreadExecutor("subsystem update");

    }

    /**
     * This Method Starts the Robot
     */
    public void start(){
        if(!started){
            subsystemUpdateExecutor.submit(subsystemUpdateRunnable);
            started = true;
        }
    }

    /**
     * This Method Stops the Robot
     */
    public void stop(){
        if(subsystemUpdateExecutor != null){
            subsystemUpdateExecutor.shutdownNow();
            subsystemUpdateExecutor = null;
        }
    }


}
