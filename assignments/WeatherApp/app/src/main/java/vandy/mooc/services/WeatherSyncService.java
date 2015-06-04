package vandy.mooc.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import vandy.mooc.aidl.WeatherCall;
import vandy.mooc.aidl.WeatherData;
import vandy.mooc.utils.Utils;

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
        public List<WeatherData> getCurrentWeather(String location) throws RemoteException {

            System.out.println("Weather call sync");
            return Utils.getResults(location);
        }
    };

}
