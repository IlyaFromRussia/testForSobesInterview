package com.example.testforsobesinterview.fragments;
/*
 * author Lobov-IR
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testforsobesinterview.R;
import com.example.testforsobesinterview.Town;
import com.example.testforsobesinterview.Weather;
import com.example.testforsobesinterview.WeatherParser;
import com.example.testforsobesinterview.database.TownBaseHelper;

import java.util.List;


public class DayFragment  extends SuperFragment {
    public DayFragment(){ }

    private ImageButton search;
    private ImageButton navigate;
    private TextView header;
    private View rootView;
    private Town currentTown;
    private RecyclerView weatherList;
    private WeatherAdapter adapter;
    private TextView textUnderImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_day, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        search = rootView.findViewById(R.id.search);
        search.setOnClickListener((View v) ->{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main_view, SearchFragment.class, null).commit();
        });

        navigate = rootView.findViewById(R.id.navigate);
        navigate.setOnClickListener((View v) ->{
            // select closer town
        });

        header = rootView.findViewById(R.id.currentTown);
        header.setOnClickListener(null);  // иначе при случайном нажатии выпадает клавиатура.
        TownBaseHelper townBaseHelper = new TownBaseHelper(getContext());
        for (Town t : townBaseHelper.getAllTowns()){
            if (t.isLast() == 1)
                currentTown = t;
        }
        header.setText(currentTown.getName());

        textUnderImage = rootView.findViewById(R.id.textUnderImage);

        weatherList = rootView.findViewById(R.id.weatherList);
        weatherList.setLayoutManager(new LinearLayoutManager(getActivity()));
        WeatherParser parser = new WeatherParser(currentTown, getActivity(), this);
        return rootView;
    }

    public void updateUI(List<Weather> weather){
        adapter = new WeatherAdapter(weather);
        weatherList.setAdapter(adapter);
        Weather wea =weather.get(0);
        if (wea != null){
            textUnderImage.setText(wea.getWeatherCode().toString() +" "+ wea.getTemperature() + " " + getString(R.string.degree)+"C");
        }
    }
}