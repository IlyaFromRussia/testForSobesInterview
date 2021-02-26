package com.example.testforsobesinterview;
/*
 * author Lobov-IR
 */

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testforsobesinterview.fragments.DayFragment;
import com.example.testforsobesinterview.fragments.NightFragment;
import com.example.testforsobesinterview.fragments.SuperFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherParser {
    List<Weather> weatherList = new ArrayList<>();
    public WeatherParser(Town currentTown, Activity activity, SuperFragment fragment){
        new Thread(() ->{
            try {
                URLConnection connection = new URL("https://api.openweathermap.org/data/2.5/forecast?lat=" + currentTown.getLatitude() +
                        "&lon=" + currentTown.getLongitude() + "&appid=914107844933cb3d3e69034a0c64e036").openConnection();
                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                char[] buffer = new char[256];
                int rc;

                StringBuilder sb = new StringBuilder();

                while ((rc = reader.read(buffer)) != -1)
                    sb.append(buffer, 0, rc);

                JSONObject object = new JSONObject(sb.toString());
                JSONArray arr = object.getJSONArray("list");

                for (int i=0; i < arr.length(); i++){
                    JSONObject oo = (JSONObject) arr.get(i);
                    if (oo.getString("dt_txt").contains("12:00:00")){
                        String date = oo.getString("dt_txt").substring(0,10); // date with format --> 2021-02-25
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date myDate = simpleDateFormat.parse(date);
                        simpleDateFormat.applyPattern("EEEE");
                        String myDayOfWeek = simpleDateFormat.format(myDate);
                        JSONObject obj = oo.getJSONObject("main");
                        double temp = obj.getDouble("temp") - 273.15;
                        temp = Double.valueOf(temp).intValue();
                        JSONArray arrWeather = oo.getJSONArray("weather");
                        String weather = ( (JSONObject) arrWeather.get(0) ).getString("main");
                        weatherList.add(new Weather(myDayOfWeek, date, temp, weather));
                    }
                }

                reader.close();

                activity.runOnUiThread(() ->{
                    if (fragment instanceof NightFragment)
                        ((NightFragment) fragment).updateUI(weatherList);
                    else ((DayFragment) fragment).updateUI(weatherList);
                });

            } catch (IOException | JSONException | ParseException e) {
                Log.d("ILYA-ERROR-TEG", e.getMessage());
                activity.runOnUiThread(() ->{
                    Toast.makeText(activity, R.string.forToast, Toast.LENGTH_LONG).show();
                });
            }


        }).start();
    }

    public List<Weather> getWeather(){
        return weatherList;
    }
}