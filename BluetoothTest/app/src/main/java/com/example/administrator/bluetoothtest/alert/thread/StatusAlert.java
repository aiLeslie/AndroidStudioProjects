package com.example.administrator.bluetoothtest.alert.thread;

import java.util.ArrayList;

public class StatusAlert implements Runnable {
    public boolean aswitch = false;
    private ArrayList<Status> statuses = new ArrayList<>();
    private long delay = 500;

    public StatusAlert(Status... state) {
        for (Status status : state) {
            statuses.add(status);
        }
    }

    public void addStatus(Status state) {
        statuses.add(state);
    }

    public void clearStatuses() {
        aswitch = false;
        statuses.clear();
    }

    public void startAlert() {
        if (!aswitch) {
            aswitch = true;
            new Thread(this).start();
        }

    }

    @Override
    public void run() {
        try {
            while (aswitch) {
                for (Status status : statuses) {
                    if (!aswitch)return;
                    status.description();
                    Thread.sleep(delay);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }


    public interface Status {
        void description();
    }
}
