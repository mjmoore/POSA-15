package vandy.mooc.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import vandy.mooc.aidl.WeatherData;
import vandy.mooc.jsonweather.JsonWeather;
import vandy.mooc.jsonweather.WeatherJSONParser;

public class Utils {

    private final static String TAG = Utils.class.getCanonicalName();
    private final static String WebServiceUrl = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=";

    public static List<WeatherData> getResults(final String location) {

        System.out.println("Attempting to get results");

        final List<JsonWeather> jsonWeather = new ArrayList<>();

        try {
            final URL url = new URL(WebServiceUrl + location);
            System.out.println(url.toString());
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try (InputStream in = new BufferedInputStream(urlConnection.getInputStream())) {
                final WeatherJSONParser parser = new WeatherJSONParser();
                jsonWeather.addAll(parser.parseJsonStream(in));
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished communication with server");

        final List<WeatherData> weatherData = new ArrayList<>();
        for (JsonWeather weather : jsonWeather) {
            if(weather.getCod() != 200)
                continue;

            weatherData.add(new WeatherData(
                weather.getName(), weather.getWind().getSpeed(), weather.getWind().getDeg(),
                weather.getMain().getTemp(), weather.getMain().getHumidity(),
                weather.getSys().getSunrise(), weather.getSys().getSunset()));
        }

        System.out.println("Finished unmarshalling data. Found " + weatherData.size());
        return weatherData;
    }

    public static void hideKeyboard(Activity activity, IBinder windowToken) {
        final InputMethodManager mgr = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
