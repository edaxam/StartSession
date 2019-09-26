package com.example.startsession;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.startsession.db.controller.UserController;
import com.gigamole.library.PulseView;

public class SplashActivity extends AppCompatActivity {

    private PulseView pulseView;
    private int id_user;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pulseView = (PulseView)findViewById(R.id.pv);

        userController = new UserController(this);
        id_user = userController.getLastUserActive();

        final Intent intent;

        if(id_user == 0){
            intent = new Intent (this, InstructionsActivity.class);
        }else
        {
            intent = new Intent (this, LauncherActivity.class);
        }



        //InstructionsActivity activity = new InstructionsActivity();


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
