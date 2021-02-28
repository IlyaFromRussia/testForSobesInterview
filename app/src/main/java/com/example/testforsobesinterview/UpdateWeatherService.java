package com.example.testforsobesinterview;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.testforsobesinterview.database.TownBaseHelper;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class UpdateWeatherService extends Service {
    private final String TG = "ILYA-TG-FROM-SERVICE";
    private Thread thread = null;
    private boolean flag = true;

    public UpdateWeatherService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TG,"  onCreate()!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TG," onStartComand()!!!!");
        task();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void task(){
        thread = new Thread(() ->{
            while (flag){
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent("weatherEvent");

                TownBaseHelper townBaseHelper = new TownBaseHelper(this);
                Town currentTown = townBaseHelper.getCurrentTown();
                ArrayList<Weather> weather = null;
                try {
                    weather = (ArrayList<Weather>) WeatherParser.getWeatherList(currentTown, townBaseHelper);
                } catch (IOException | JSONException | ParseException e) {
                    e.printStackTrace();
                }

                Bundle extr = new Bundle();
                extr.putSerializable("weather", weather);
                intent.putExtra("extr", extr);

                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                Log.d(TG+"  ", intent.toString());
            }
        });

        thread.start();
    }

    @Override
    public void onDestroy() {
        Log.d(TG, "onDestroy() worked!!");
        setFlag(false);
        super.onDestroy();
    }

    public void setFlag(boolean b){flag = b;}
}