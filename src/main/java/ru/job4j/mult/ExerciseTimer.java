package ru.job4j.mult;

import java.util.concurrent.atomic.AtomicBoolean;

public class ExerciseTimer implements Runnable {
    volatile long timerPreSet;
    volatile AtomicBoolean timerStatus = new AtomicBoolean(true);
    volatile boolean timerSwitcher = false;
    volatile long timeStart = 0;
    volatile long timeCurrent;
    volatile long period;

    public ExerciseTimer(long setPointInSeconds) {
        this.timerPreSet = setPointInSeconds * 1000;
    }

    public void preSetTimer(long setPointInSeconds) {
        this.timerPreSet = setPointInSeconds * 1000;
    }

    public void switchON() {
        this.timerSwitcher = true;
    }

    public void switchOFF() {
        this.timerSwitcher = false;
    }

    public boolean getStatus() {
        return this.timerStatus.get();
    }

    public void setStatus(boolean status) {
        boolean result = false;
        while (!result) {
            boolean currentStatus = timerStatus.get();
            result = timerStatus.compareAndSet(currentStatus, status);
        }
    }

    @Override
    public void run() {
        while (timerSwitcher) {
            timeStart = System.currentTimeMillis();
            while (getStatus()) {
                timeCurrent = System.currentTimeMillis();
                period = timeCurrent - timeStart;
                if (period > timerPreSet) {
                    setStatus(false);
                }
            }
        }
    }
}
