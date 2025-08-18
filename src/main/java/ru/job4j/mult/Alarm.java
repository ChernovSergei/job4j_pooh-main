package ru.job4j.mult;

import java.util.concurrent.atomic.AtomicBoolean;

public class Alarm implements Runnable {
    private volatile AtomicBoolean switcher = new AtomicBoolean(false);

    public void switchAlarm(Boolean onOff) {
        boolean monitor = false;
        while (!monitor) {
            boolean currentStatus = getStatus();
            monitor = switcher.compareAndSet(currentStatus, onOff);
        }
    }

    public Boolean getStatus() {
        return switcher.get();
    }

    @Override
    public void run() {
        switchAlarm(false);
    }
}
