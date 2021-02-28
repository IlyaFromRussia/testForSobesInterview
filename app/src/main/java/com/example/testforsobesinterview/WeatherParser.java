package com.example.testforsobesinterview;
/*
 * author Lobov-IR
 */

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.example.testforsobesinterview.database.TownBaseHelper;
import com.example.testforsobesinterview.database.WeatherForDB;
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
import java.util.Locale;

public class WeatherParser {
    List<Weather> weatherList = null;
    List<WeatherForDB> weatherForDB = new ArrayList<>();

    public WeatherParser(Town currentTown, Activity activity, SuperFragment fragment) {
        new Thread(() -> {
            TownBaseHelper townBaseHelper = new TownBaseHelper(activity);
            try{
                weatherList = getWeatherList(currentTown,townBaseHelper);
            }
            catch (IOException | JSONException | ParseException e) {
                Log.d("ILYA-ERROR-TEG", e.getMessage());
                activity.runOnUiThread(() -> {
                    Toast.makeText(activity, R.string.forToast, Toast.LENGTH_LONG).show();
                    fragment.updateUI(townBaseHelper.getOldWeather(currentTown.getName()));
                });
            }
            activity.runOnUiThread(() ->{
                fragment.updateUI(weatherList);
            });

        }).start();
    }

    public static List<Weather> getWeatherList(Town currentTown, TownBaseHelper townBaseHelper)
            throws IOException, JSONException, ParseException {
        ArrayList<Weather> weathers = new ArrayList<>();
        ArrayList<WeatherForDB> weatherForDB = new ArrayList<>();

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
        int counterId = townBaseHelper.getMaxWeatherId() + 1;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject oo = (JSONObject) arr.get(i);
            if (oo.getString("dt_txt").contains("12:00:00")) {
                String date = oo.getString("dt_txt").substring(0, 10); // date with format --> 2021-02-25

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));
                Date myDate = simpleDateFormat.parse(date);
                simpleDateFormat.applyPattern("EEEE");
                String myDayOfWeek = simpleDateFormat.format(myDate);

                JSONObject obj = oo.getJSONObject("main");
                double temp = obj.getDouble("temp") - 273.15;
                temp = Double.valueOf(temp).intValue();

                JSONArray arrWeather = oo.getJSONArray("weather");
                String weather = ((JSONObject) arrWeather.get(0)).getString("main");

                weathers.add(new Weather(myDayOfWeek, date, temp, weather));

                weatherForDB.add(new WeatherForDB(counterId, currentTown.getName(), date, myDayOfWeek, temp, weather));
                counterId++;
            }
        }
        reader.close();
        townBaseHelper.setWeatherForTown(weatherForDB);

        return weathers;
    }
}