package com.example.testforsobesinterview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.testforsobesinterview.fragments.DayFragment;
import com.example.testforsobesinterview.fragments.NightFragment;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Town> towns = Arrays.asList(new Town("Moscow"),new Town("Shanghai"),new Town("Mumbai")
            ,new Town("Prague"),new Town("Amsterdam"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            if (resolveTime() == 1){
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_main_view, DayFragment.class,null)
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_main_view, NightFragment.class,null)
                        .commit();
            }
        }
    }

    private int resolveTime(){
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);

        if(hours > 6 && hours < 19) // 1- day, 0 - night
            return 1;
        else
            return 0;
    }

    public List<Town> getTowns(){
        return towns;
    }
}