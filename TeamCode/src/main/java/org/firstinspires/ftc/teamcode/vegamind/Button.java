package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.robotcore.util.ElapsedTime;

import lombok.Getter;

public class Button {

    @Getter
    boolean state;

    ElapsedTime since_last_change;

    final int debounce_time = 50; // in MS

    public Button(){
        since_last_change = new ElapsedTime();
        state = false;
    }

    public void update(boolean state){
        if(since_last_change.milliseconds() <= debounce_time || this.state == state){
            return;
        }

        since_last_change.reset();

        this.state = state;
    }
}
