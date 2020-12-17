package com.creatordp.stockappadroid.services;

import android.os.Looper;

import android.os.Handler;

public class RequestWorker extends Thread{
    public Handler dataHandler;
    public Looper looper;
    @Override
    public void run() {
        Looper.prepare();
        dataHandler = new Handler();
        looper = Looper.myLooper();
        Looper.loop();
    }
}
