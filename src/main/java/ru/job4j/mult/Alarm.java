package ru.job4j.mult;

import java.util.concurrent.atomic.AtomicBoolean;

public class Alarm implements Runnable {
    private volatile AtomicBoolean status = new AtomicBoolean(false);

    public void switchAlarm(Boolean status) {
        boolean monitor = false;
        while (!monitor) {
            boolean currentStatus = getStatus();
            monitor = this.status.compareAndSet(currentStatus, status);
        }
    }

    public Boolean getStatus() {
        return status.get();
    }

    @Override
    public void run() {
        switchAlarm(false);
    }
}
