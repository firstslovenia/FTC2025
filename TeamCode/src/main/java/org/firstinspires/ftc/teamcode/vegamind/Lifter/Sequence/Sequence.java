package org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence;

import java.util.List;

import lombok.Getter;

public abstract class Sequence {
    protected List<Step> steps;

    @Getter
    protected int currentStep = 0;

    @Getter
    protected boolean isRunning = false;

    Sequence() {
        currentStep = 0;
    }


    public int run() {
        if (!isRunning){
            return currentStep;
        }

        if (!steps.get(currentStep).run()) {
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
        currentStep = steps.size()-1;
    }
}
