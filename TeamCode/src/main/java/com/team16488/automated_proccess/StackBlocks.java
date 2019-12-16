package com.team16488.automated_proccess;

import com.team16488.skystone.Robot;

/**
 * This is the Class that deals with stacking the blocks
 * for the macros on the subsystem driver controler
 *
 * @author Parham Baghbanbashi: parhambagh@gmail.com
 * @author Ernest Wong
 *
 * <p>github: https://github.com/StrRamsRobotics/SkyStone/tree/Parham-Baghbanbashi</p>
 */
public class StackBlocks {
    public Robot robot;

    public double tickCount;
    /**
     * This is the constructor for the class
     *
     * @param oprobot The robot Object for the OpMode
     */
    public StackBlocks(Robot oprobot, double tickCount) {
        robot = oprobot;
        this.tickCount = tickCount;
    }

    public void stackOneBlockHigh() {
        robot.lift.setPosition(tickCount * 1);
    }

    public void stackTwoBlockHigh() {
        robot.lift.setPosition(tickCount * 2);
    }


}