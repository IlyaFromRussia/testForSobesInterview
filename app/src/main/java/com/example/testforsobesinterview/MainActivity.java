package com.example.testforsobesinterview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.testforsobesinterview.fragments.DayFragment;
import com.example.testforsobesinterview.fragments.NightFragment;
import com.example.testforsobesinterview.fragments.SuperFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            resolveFragment(getSupportFragmentManager());
        }
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras().getBundle("extr");
                List<Weather> weather = (ArrayList<Weather>) b.get("weather");
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_main_view);
                if (fragment.isVisible()){  // не будет NPE, т.к. в onPause() есть unregisterReceiver
                    ((SuperFragment) fragment).updateUI(weather);
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("weatherEvent"));

        Intent intent = new Intent(this, UpdateWeatherService.class);
        startService(intent);
    }

    public void resolveFragment(FragmentManager supportFragmentManager){
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);

        if(hours > 6 && hours < 19) // 1- day, 0 - night
            supportFragmentManager.beginTransaction().add(R.id.fragment_main_view, DayFragment.class,null)
                    .commit();
        else
            supportFragmentManager.beginTransaction().add(R.id.fragment_main_view, NightFragment.class,null)
                    .commit();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        Intent stop = new Intent(this, UpdateWeatherService.class);
        stopService(stop);
        Log.d("ON-PAUSE-TG", "onPause() worked!");
        super.onPause();
    }
}