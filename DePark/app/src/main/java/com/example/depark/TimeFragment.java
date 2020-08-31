package com.example.depark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class TimeFragment extends AppCompatActivity {

    Chronometer meter;
    double i=0;
    TextClock c1;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_time);
        c1 = (TextClock) findViewById(R.id.textClock);
        meter = (Chronometer) findViewById(R.id.chronometer);
        t1 = (TextView) findViewById(R.id.textview1);

        meter.setFormat("Time: %S");
        meter.setBase(SystemClock.elapsedRealtime());
        meter.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) <= 11700) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    i = 3.00;
                    t1.setText("RM"+i);
                    Toast.makeText(TimeFragment.this, "Bing!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 11700) {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        i = 3.00 + 1.00;
                        t1.setText("RM"+i);
                        Toast.makeText(TimeFragment.this, "Bing!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        
    }
}