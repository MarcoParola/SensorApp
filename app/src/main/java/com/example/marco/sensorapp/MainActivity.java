package com.example.marco.sensorapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private boolean inizializzato;
    private SensorManager sensorManager;
    private Sensor accelerometro;
    private final float rumore = (float) 0.4;
    TextView asseX;
    TextView asseY;
    TextView asseZ;
    private float ultimaX, ultimaY, ultimaZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inizializzato = false;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        asseX = (TextView) findViewById(R.id.tv_asse_x);
        asseY = (TextView) findViewById(R.id.tv_asse_y);
        asseZ = (TextView) findViewById(R.id.tv_asse_z);

    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometro, SensorManager.SENSOR_DELAY_NORMAL); // dichiaro il listener che si mette in ascolto dell'accelerometro
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        String csv_string = "";

        if (!inizializzato) {
            ultimaX = x;
            ultimaY = y;
            ultimaZ = z;
            asseX.setText("Asse X: 0.0");
            asseY.setText("Asse Y: 0.0");
            asseZ.setText("Asse Z: 0.0");
            inizializzato = true;
            csv_string = String.valueOf(x) + ";" + String.valueOf(y) + ";" + String.valueOf(z) + ";" ;
        } else {
            float deltaX = Math.abs(ultimaX - x);
            float deltaY = Math.abs(ultimaY - y);
            float deltaZ = Math.abs(ultimaZ - z);

            if (deltaX > rumore) {
                asseX.setText("Asse X: " + String.valueOf(deltaX));
                csv_string = String.valueOf(deltaX) + ";";
            }
            else {
                asseX.setText("Asse X: 0.0");
                csv_string = "0.0;";
            }
            if (deltaY > rumore) {
                asseY.setText("Asse Y: " + String.valueOf(deltaY));
                csv_string += String.valueOf(deltaY) + ";";
            }
            else{
                asseY.setText("Asse Y: 0.0");
                csv_string += "0.0;";
            }

            if (deltaZ > rumore){
                asseZ.setText("Asse Z: " + String.valueOf(deltaZ));
                csv_string += String.valueOf(deltaZ) + ";";
            }
            else{
                asseZ.setText("Asse Z: 0.0");
                csv_string += "0.0;";
            }


            ultimaX = x;
            ultimaY = y;
            ultimaZ = z;

        }
/*

    // SCRITTURA DEI DATI SU SU UN FILE

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOCUMENTS), "log.csv");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fw = new FileOutputStream(file.getAbsoluteFile(), true);
            PrintWriter bw = new PrintWriter(fw);
            if (x > 0 || y > 0 || z > 0)
                bw.println(csv_string);
            bw.close();
            fw.close();
            Log.i("CSV", csv_string);

        } catch (IOException e) {
            Log.e("", e.getLocalizedMessage(), e);
        }
*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
