package org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence;

import java.util.List;

import lombok.Getter;

public class Sequence {
    protected List<Step> steps;
    protected int currentStep;

    @Getter
    protected boolean isRunning;

    Sequence() {
        currentStep = 0;
    }


    public int run() {
        if (!isRunning || !steps.get(currentStep).run()) {
            return currentStep;
        }

        currentStep += 1;
        if (currentStep == steps.size()) {
            currentStep = 0;
            isRunning = false;
        }

        return currentStep;
    }

    public void start() {
        isRunning = true;
    }

    public void reset() {
        currentStep = 0;
        isRunning = false;
    }
}
