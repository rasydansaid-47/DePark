package com.example.depark;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.DigitalClock;
import android.widget.TextView;


public class TimeFragment extends AppCompatActivity {

    private Chronometer meter;
    private long pauseOffset;
    private boolean running;
    final Context context = this;
    double pay = 0;
    long hours;
    String g,e;
    DigitalClock c1;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_time);
        c1 = findViewById(R.id.textClock);
        meter = findViewById(R.id.chronometer);
        t1 = findViewById(R.id.textview1);
        hours = SystemClock.elapsedRealtime() - meter.getBase();

        startChronometer();
        meter.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if(SystemClock.elapsedRealtime() - meter.getBase() < 11700){
                    pay = 2.00;
                    t1.setText("Total fee: RM "+pay);
                }
                else if (SystemClock.elapsedRealtime() - meter.getBase() > 11700){
                    pay = 2.00 + ((hours - 11700));
                    t1.setText("Total fee: RM "+pay);
                }
            }
        });
    }

    public void startChronometer(){
        if(!running){
            meter.setBase(SystemClock.elapsedRealtime());
            meter.start();
            running = true;
        }
    }

    public void pauseChronometer(){
        if(running){
            meter.stop();
            pauseOffset = SystemClock.elapsedRealtime() - meter.getBase();
            running = false;
        }
    }

    public void endChronometer(){
        meter.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}