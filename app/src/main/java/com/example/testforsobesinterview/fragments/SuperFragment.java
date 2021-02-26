package com.example.testforsobesinterview.fragments;
/*
 * author Lobov-IR
 */

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testforsobesinterview.R;
import com.example.testforsobesinterview.Weather;

import java.util.List;

public class SuperFragment extends Fragment {
    public SuperFragment(){}

        /*
                            ADAPTER
        */
    public class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder>{
        private List<Weather> weatherList;

        public WeatherAdapter(List<Weather> weatherList){
            this.weatherList = weatherList;
        }

        @NonNull
        @Override
        public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new WeatherHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SuperFragment.WeatherHolder holder, int position) {
            Weather weather = weatherList.get(position);
            holder.bind(weather);
        }

        @Override
        public int getItemCount() {
            return weatherList.size();
        }
    }


    /*
                        HOLDER
    */
    public class WeatherHolder extends RecyclerView.ViewHolder{
        private TextView firtsLine;
        private TextView temperature;
        private ImageView weatherImage;
        private TextView secondLine;


        public WeatherHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_day_of_week,parent,false));
            itemView.setOnClickListener(null);
            firtsLine = itemView.findViewById(R.id.firstLine);
            temperature = itemView.findViewById(R.id.temperatyre);
            weatherImage = itemView.findViewById(R.id.weatherImage);
            secondLine = itemView.findViewById(R.id.secondLine);
        }

        public void bind(Weather weather){
            firtsLine.setText(weather.getDayOwWeek());
            temperature.setText(weather.getTemperature() + getString(R.string.degree) + " C");
            secondLine.setText(weather.getDate());
            switch (weather.getWeatherCode()){
                case CLOUDS:{
                    weatherImage.setImageResource(R.drawable.cloud);
                    break;
                }
                case RAIN:{
                    weatherImage.setImageResource(R.drawable.rain);
                    break;
                }
                case CLEAR:{
                    weatherImage.setImageResource(R.drawable.clear);
                }
            }

        }
    }
}