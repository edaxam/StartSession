package com.example.startsession;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

        pulseView.startPulse();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //que hacer despues de 5 segundos
                loadActivity();
            }
        }, 5000);
    }

    public void loadActivity(){
        userController = new UserController(this);
        id_user = userController.getLastUserActive();
        //Log.e("HOLA",""+id_user);
        if(id_user == 0){
            Intent intent = new Intent (SplashActivity.this, InstructionsActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent (SplashActivity.this, LauncherActivity.class);
            intent.putExtra("id_user",""+id_user);
            //Log.e("HOLA",""+id_user);
            startActivity(intent);
        }

    }

}
