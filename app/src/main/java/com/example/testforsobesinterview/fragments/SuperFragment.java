package com.example.testforsobesinterview.fragments;
/*
 * author Lobov-IR
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testforsobesinterview.MainActivity;
import com.example.testforsobesinterview.R;
import com.example.testforsobesinterview.Town;
import com.example.testforsobesinterview.Weather;
import com.example.testforsobesinterview.database.TownBaseHelper;

import java.util.List;

public class SuperFragment extends Fragment implements LocationListener{
    public SuperFragment(){}
    protected LocationManager locationManager;

    @Override
    public void onStop() {
        if (locationManager!=null)
            locationManager.removeUpdates(this);
        super.onStop();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        selectCloserTown(location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            }
        }
    }

    protected void selectCloserTown(Location location){
        TownBaseHelper townBaseHelper = new TownBaseHelper(getContext());
        List<Town> townList = townBaseHelper.getAllTowns();
        // нахожу ближайший город.
        Town closerTown = null;
        Location townLocation = new Location("");
        townLocation.setLatitude(Double.parseDouble(townList.get(0).getLatitude()));
        townLocation.setLongitude(Double.parseDouble(townList.get(0).getLongitude()));
        float min=location.distanceTo(townLocation);

        for (Town town : townList){
            townLocation = new Location("");
            townLocation.setLatitude(Double.parseDouble(town.getLatitude()));
            townLocation.setLongitude(Double.parseDouble(town.getLongitude()));

            if (location.distanceTo(townLocation) < min){
                min = location.distanceTo(townLocation);
                closerTown = town;
            }
        }
        Log.d("ILYA-TEG", closerTown.getName());
        closerTown.setLastTown(1);
        townBaseHelper.reloadMark(closerTown);
        Toast.makeText(getContext(),getString(R.string.town_changed) +" "+ closerTown.getName(),Toast.LENGTH_SHORT).show();
        locationManager.removeUpdates(this);  // иначе цикл
        ((MainActivity) getActivity()).resolveFragment(getActivity().getSupportFragmentManager());
    }

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
                case ALLERT:{
                    weatherImage.setImageResource(R.drawable.complain);  // иначе приложение падает.
                }
                case SNOW:{
                    weatherImage.setImageResource(R.drawable.snowy);
                }
            }

        }
    }
}