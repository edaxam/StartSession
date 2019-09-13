package com.example.startsession;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.gigamole.library.PulseView;

public class SplashActivity extends AppCompatActivity {

    private PulseView pulseView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pulseView = (PulseView)findViewById(R.id.pv);

        final Intent intent = new Intent (this, InstructionsActivity.class);

        InstructionsActivity activity = new InstructionsActivity();


        pulseView.startPulse();

        //activity.cargarDatos();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //que hacer despues de 10 segundos
                startActivity(intent);
            }
        }, 5000);
    }
}
