package vandy.mooc.operations;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteException;

import java.lang.ref.WeakReference;
import java.sql.SQLOutput;
import java.util.List;

import vandy.mooc.WeatherActivity;
import vandy.mooc.aidl.WeatherCall;
import vandy.mooc.aidl.WeatherData;
import vandy.mooc.aidl.WeatherRequest;
import vandy.mooc.aidl.WeatherResults;
import vandy.mooc.services.WeatherAsyncService;
import vandy.mooc.services.WeatherSyncService;
import vandy.mooc.utils.GenericServiceConnection;
import vandy.mooc.utils.Utils;

/**
 * This class implements all the acronym-related operations defined in
 * the IWeatherOps interface.
 */
public class WeatherOps implements IWeatherOps {

    protected WeakReference<WeatherActivity> mActivity;

    private GenericServiceConnection<WeatherCall> mServiceConnectionSync;
    private GenericServiceConnection<WeatherRequest> mServiceConnectionAsync;

    protected List<WeatherData> mResults;

    private final Handler mDisplayHandler = new Handler();

    private final WeatherResults.Stub mWeatherResult = new WeatherResults.Stub() {

        @Override
        public void sendResults(final List<WeatherData> weatherData) throws RemoteException {
            mDisplayHandler.post(new Runnable() {
                public void run() {
                    mResults = weatherData;
                    mActivity.get().displayResults(weatherData, "Nothing found!");
                }
            });
        }
    };

    public WeatherOps(WeatherActivity activity) {
        mActivity = new WeakReference<>(activity);

        mServiceConnectionSync = new GenericServiceConnection<>(WeatherCall.class);
        mServiceConnectionAsync = new GenericServiceConnection<>(WeatherRequest.class);
    }

    /**
     * Called after a runtime configuration change occurs to finish
     * the initialization steps.
     */
    public void onConfigurationChange(WeatherActivity activity) {

        mActivity = new WeakReference<>(activity);

        updateResultsDisplay();
    }

    /**
     * Display results if any (due to runtime configuration change).
     */
    private void updateResultsDisplay() {
        if (mResults != null)
            mActivity.get().displayResults(mResults, null);
    }

    /**
     * Initiate the service binding protocol.
     */
    @Override
    public void bindService() {

        System.out.println("Binding services");

        if (mServiceConnectionSync.getInterface() == null) {
            mActivity.get().getApplicationContext().bindService( WeatherSyncService.makeIntent(
                mActivity.get()), mServiceConnectionSync, Context.BIND_AUTO_CREATE);
            System.out.println("Bound sync services");
        }

        if (mServiceConnectionAsync.getInterface() == null) {
            mActivity.get().getApplicationContext().bindService(WeatherAsyncService.makeIntent(
                    mActivity.get()), mServiceConnectionAsync, Context.BIND_AUTO_CREATE);
            System.out.println("Bound async services");
        }
    }

    /**
     * Initiate the service unbinding protocol.
     */
    @Override
    public void unbindService() {
        if (mActivity.get().isChangingConfigurations())
            return;

        // Unbind the Async Service if it is connected.
        if (mServiceConnectionAsync.getInterface() != null)
            mActivity.get().getApplicationContext().unbindService(mServiceConnectionAsync);

        // Unbind the Sync Service if it is connected.
        if (mServiceConnectionSync.getInterface() != null)
            mActivity.get().getApplicationContext().unbindService(mServiceConnectionSync);
    }

    public void expandWeatherAsync(String location) {
        final WeatherRequest weatherRequest = mServiceConnectionAsync.getInterface();

        System.out.println("Weather call: " + weatherRequest);
        if (weatherRequest == null)
            return;

        try {
            weatherRequest.getCurrentWeather(location, mWeatherResult);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void expandWeatherSync(String location) {
        final WeatherCall weatherCall = mServiceConnectionSync.getInterface();

        if (weatherCall == null)
            return;

        new AsyncTask<String, Void, List<WeatherData>> () {

            private String mLocation;

            protected List<WeatherData> doInBackground(String... location) {

                System.out.println("AsyncTask kicking off");

                try {
                    mLocation = location[0];
                    System.out.println("Async task with " + mLocation);
                    return weatherCall.getCurrentWeather(mLocation);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(List<WeatherData> weatherData) {
                mResults = weatherData;
                mActivity.get().displayResults(weatherData, "Nothing found?");
            }

        }.execute(location);
    }

}
