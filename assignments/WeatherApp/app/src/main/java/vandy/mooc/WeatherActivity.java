package vandy.mooc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import vandy.mooc.aidl.WeatherData;
import vandy.mooc.operations.IWeatherOps;
import vandy.mooc.operations.WeatherOps;
import vandy.mooc.utils.RetainedFragmentManager;


public class WeatherActivity extends Activity {

    private final String TAG = WeatherActivity.class.getName();

    private View view;

    private RelativeLayout layout;

    private EditText mLocation;
    private Switch syncSwitch;
    private Button fetchButton;
    private TextView outputText;

    private Boolean aSync = false;

    private IWeatherOps weatherOps;

    private final RetainedFragmentManager mFragmentManager = new RetainedFragmentManager(
        this.getFragmentManager(), TAG);

    public void displayResults(List<WeatherData> weatherData, String reason) {
        System.out.println("Returned weather to UI");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        setupView();

        //Setup or retrieve fragment manager
        handleConfigurationChange();
    }

    private void handleConfigurationChange() {

        if(!mFragmentManager.firstTimeIn()) {
            setupAndBindWeatherOps();
            return;
        }

        weatherOps = mFragmentManager.get("weatherOps");
        if(weatherOps == null) {
            setupAndBindWeatherOps();
        } else {
            weatherOps.onConfigurationChange(this);
        }

        //TODO: Setup state
        return;
    }

    private void setupAndBindWeatherOps() {
        weatherOps = new WeatherOps(this);
        mFragmentManager.put("weatherOps", weatherOps);
        weatherOps.bindService();
        return;
    }

    private void setupView() {
        layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_weather, null);
        mLocation = (EditText) findViewById(R.id.locationInput);
        syncSwitch = (Switch) findViewById(R.id.asyncSwitch);
        fetchButton = (Button) findViewById(R.id.fetchButton);
        outputText = (TextView) findViewById(R.id.textView);

        syncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("Changing sync state to " + isChecked);
                aSync = isChecked;
            }
        });

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = mLocation.getText().toString();


                if(location == null || location.isEmpty())
                    location = "Nashville";

                System.out.println("Sync: " + aSync);
                System.out.println("Fetching weather for "  + location);

                if(aSync) {
                    weatherOps.expandWeatherAsync(location);
                } else {
                    weatherOps.expandWeatherSync(location);
                }
            }
        });

    }
}
