package vandy.mooc.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import vandy.mooc.aidl.WeatherData;
import vandy.mooc.aidl.WeatherRequest;
import vandy.mooc.aidl.WeatherResults;
import vandy.mooc.utils.Utils;


public class WeatherAsyncService extends Service {

    public static Intent makeIntent(Context context) {
        return new Intent(context, WeatherSyncService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return weatherRequest;
    }

    final WeatherRequest.Stub weatherRequest = new WeatherRequest.Stub() {

        @Override
        public void getCurrentWeather(String weather, WeatherResults results) throws RemoteException {
            final List<WeatherData> weatherData = Utils.getResults(weather);
            if(!weather.isEmpty()) {
                results.sendResults(weatherData);
            }
        }
    };
}
