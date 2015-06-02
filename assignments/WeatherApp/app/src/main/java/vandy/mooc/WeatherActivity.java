package vandy.mooc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import vandy.mooc.utils.RetainedFragmentManager;


public class WeatherActivity extends Activity {

    private final String TAG = WeatherActivity.class.getName();

    private final RetainedFragmentManager mFragmentManager = new RetainedFragmentManager(
        this.getFragmentManager(), TAG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Setup or retrieve fragment manager
        handleConfigurationChange();
    }

    private void handleConfigurationChange() {

        if(!mFragmentManager.firstTimeIn()) {
            //TODO: Restore state
            return;
        }

        //TODO: Setup state
        return;
    }

}
