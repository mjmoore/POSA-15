package vandy.mooc.operations;

import vandy.mooc.WeatherActivity;

public interface IWeatherOps {

    public void bindService();
    public void unbindService();
    public void expandWeatherSync(String location);
    public void expandWeatherAsync(String location);
    public void onConfigurationChange(WeatherActivity activity);
}
