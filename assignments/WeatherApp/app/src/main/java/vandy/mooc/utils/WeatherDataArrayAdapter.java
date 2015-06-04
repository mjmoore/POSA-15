package vandy.mooc.utils;

import java.util.List;

import vandy.mooc.aidl.WeatherData;

import vandy.mooc.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeatherDataArrayAdapter extends ArrayAdapter<WeatherData> {

    public WeatherDataArrayAdapter(Context context) {
        super(context, R.layout.activity_weather);
    }

    public WeatherDataArrayAdapter(Context context, List<WeatherData> objects) {
        super(context, R.layout.activity_weather, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final WeatherData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.activity_weather, parent, false);
        }

        final TextView output = (TextView) convertView.findViewById(R.id.textView);
        output.setText(data.toString());

        return convertView;
    }
}
