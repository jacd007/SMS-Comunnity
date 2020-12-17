package com.jacd.smscommunity.service;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacd.smscommunity.R;
import com.jacd.smscommunity.common.SendUtils;
import com.jacd.smscommunity.common.Utils;
import com.jacd.smscommunity.model.ContactsModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class MyService extends Service {

    private static final String TAG = "Service";
    public MyService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Servicio iniciado...");
        Hilo hil = new Hilo(this);
        hil.execute();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}



