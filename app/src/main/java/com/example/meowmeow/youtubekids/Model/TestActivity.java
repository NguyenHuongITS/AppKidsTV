package com.example.meowmeow.youtubekids.Model;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.example.meowmeow.youtubekids.R;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "";
    private static int value = Sensor.TYPE_PROXIMITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        SensorManager sensorManager =
                (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor proximitySensor =
                sensorManager.getDefaultSensor(value);
        if(proximitySensor == null) {
            Log.e(TAG, "Proximity sensor not available.");
            finish(); // Close app
        }
        // Create listener
        SensorEventListener proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here
                if(sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    // Detected something nearby

                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                    Toast.makeText(TestActivity.this,"You: "+ proximitySensor.getMaximumRange(), Toast.LENGTH_SHORT).show();
                } else {
                    // Nothing is nearby
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

// Register it, specifying the polling interval in
// microseconds
        sensorManager.registerListener(proximitySensorListener,
                proximitySensor, 2 * 1000 * 1000);
    }
}
