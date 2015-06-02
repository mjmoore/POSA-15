package vandy.mooc.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import vandy.mooc.aidl.WeatherCall;
import vandy.mooc.aidl.WeatherData;

public class WeatherSync extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final WeatherCall.Stub mWeatherCall = new WeatherCall.Stub() {

        @Override
        public List<WeatherData> getCurrentWeather(String location) throws RemoteException {

            return null;
        }
    };

}
