package com.example.testforsobesinterview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
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
            resolveFragment(getSupportFragmentManager());
        }
    }

    public static void resolveFragment(FragmentManager supportFragmentManager){
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);

        if(hours > 6 && hours < 19) // 1- day, 0 - night
            supportFragmentManager.beginTransaction().add(R.id.fragment_main_view, DayFragment.class,null)
                    .commit();
        else
            supportFragmentManager.beginTransaction().add(R.id.fragment_main_view, NightFragment.class,null)
                    .commit();
    }

    public List<Town> getTowns(){
        return towns;
    }
}