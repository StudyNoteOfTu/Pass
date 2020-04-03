package com.example.pass.test.shade;

import java.util.Timer;
import java.util.TimerTask;

public class LongClickTimer{
    public Timer timer;


    public LongClickTimer(int millis) {
        timer = new Timer();
        timer.schedule(new LongClickTask(), millis);
    }
    class LongClickTask extends TimerTask {
        public void run() {
            timer.cancel();
        }
    }
}
