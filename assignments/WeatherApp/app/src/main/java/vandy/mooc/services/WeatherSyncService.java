package vandy.mooc.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vandy.mooc.aidl.WeatherCall;
import vandy.mooc.jsonweather.JsonWeather;
import vandy.mooc.jsonweather.WeatherJSONParser;

public class WeatherSyncService extends Service {

    public static Intent makeIntent(Context context) {
        return new Intent(context, WeatherSyncService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return weatherCall;
    }

    private final WeatherCall.Stub weatherCall = new WeatherCall.Stub() {

        @Override
        public List<JsonWeather> getCurrentWeather(String location) throws RemoteException {

            final List<JsonWeather> weatherData = new ArrayList<>();
            try {
                final URL calloutUrl = new URL("http://api.openweathermap.org/data/2.5/weather?units=metric&q=" + location);
                final InputStream inStream = calloutUrl.openConnection().getInputStream();
                weatherData.addAll(new WeatherJSONParser().parseJsonStream(inStream));
            } catch (MalformedURLException e) {
                Log.wtf(this.getClass().getName(), "Issue with URL");
            } catch (IOException e) {
                Log.wtf(this.getClass().getName(), "Issue with connection or the JSON response");
            } finally {
                return weatherData;
            }
        }
    };

}
