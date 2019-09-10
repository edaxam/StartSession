package com.example.startsession;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.example.startsession.R;

public class AdminHomeCard extends AppCompatActivity {
    private LinearLayout usuCardFront;
    private LinearLayout usuCardBack;

    private LinearLayout lanCardFront;
    private LinearLayout lanCardBack;

    private LinearLayout appsCardFront;
    private LinearLayout appsCardBack;

    private ScaleAnimation cambioXU = new ScaleAnimation(1, 0, 1, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
    private ScaleAnimation cambioYU = new ScaleAnimation(0, 1, 1, 1,Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);


    private ScaleAnimation cambioXL = new ScaleAnimation(1, 0, 1, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
    private ScaleAnimation cambioYL = new ScaleAnimation(0, 1, 1, 1,Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    private ScaleAnimation cambioXA = new ScaleAnimation(1, 0, 1, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
    private ScaleAnimation cambioYA = new ScaleAnimation(0, 1, 1, 1,Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    public void Usuarios(View view){
        roteUsuarios(view);
        if (usuCardFront.getVisibility() == View.VISIBLE) {
            usuCardFront.startAnimation(cambioXU);
        }else{
            usuCardBack.startAnimation(cambioXU);
        }
    }

    private void showCardF(){
        usuCardFront.setVisibility(View.VISIBLE);
        usuCardBack.setVisibility(View.INVISIBLE);
    }

    private void showCardB(){
        usuCardFront.setVisibility(View.INVISIBLE);
        usuCardBack.setVisibility(View.VISIBLE);
    }

    private void roteUsuarios(View view){
        usuCardFront = (LinearLayout) view.findViewById(R.id.UTfront);
        usuCardBack = (LinearLayout) view.findViewById(R.id.UTback);
        showCardF();
        cambioXU.setDuration(100);
        cambioYU.setDuration(100);

        cambioXU.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (usuCardFront.getVisibility() == View.VISIBLE) {
                    usuCardFront.setAnimation(null);
                    showCardB();
                    usuCardBack.startAnimation(cambioYU);
                }else{
                    usuCardBack.setAnimation(null);
                    showCardF();
                    usuCardFront.startAnimation(cambioYU);
                }
            }
        });
    }

    public void Lanzamiento(View view){
        roteLanzamientos(view);
        if (lanCardFront.getVisibility() == View.VISIBLE) {
            lanCardFront.startAnimation(cambioXL);
        }else{
            lanCardBack.startAnimation(cambioXL);
        }
    }

    private void showCardFL(){
        lanCardFront.setVisibility(View.VISIBLE);
        lanCardBack.setVisibility(View.INVISIBLE);
    }

    private void showCardBL(){
        lanCardFront.setVisibility(View.INVISIBLE);
        lanCardBack.setVisibility(View.VISIBLE);
    }

    private void roteLanzamientos(View view){
        lanCardFront = (LinearLayout) view.findViewById(R.id.LTfront);
        lanCardBack = (LinearLayout) view.findViewById(R.id.LTback);
        showCardFL();
        cambioXL.setDuration(100);
        cambioYL.setDuration(100);

        cambioXL.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (lanCardFront.getVisibility() == View.VISIBLE) {
                    lanCardFront.setAnimation(null);
                    showCardBL();
                    lanCardBack.startAnimation(cambioYL);
                }else{
                    lanCardBack.setAnimation(null);
                    showCardFL();
                    lanCardFront.startAnimation(cambioYL);
                }
            }
        });
    }


    public void Apps(View view){
        roteApps(view);
        if (appsCardFront.getVisibility() == View.VISIBLE) {
            appsCardFront.startAnimation(cambioXA);
        }else{
            appsCardBack.startAnimation(cambioXA);
        }
    }

    private void showCardFA(){
        appsCardFront.setVisibility(View.VISIBLE);
        appsCardBack.setVisibility(View.INVISIBLE);
    }

    private void showCardBA(){
        appsCardFront.setVisibility(View.INVISIBLE);
        appsCardBack.setVisibility(View.VISIBLE);
    }

    private void roteApps(View view){
        appsCardFront = (LinearLayout) view.findViewById(R.id.ATfront);
        appsCardBack = (LinearLayout) view.findViewById(R.id.ATback);
        showCardF();
        cambioXA.setDuration(100);
        cambioYA.setDuration(100);

        cambioXA.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (appsCardFront.getVisibility() == View.VISIBLE) {
                    appsCardFront.setAnimation(null);
                    showCardBA();
                    appsCardBack.startAnimation(cambioYA);
                }else{
                    appsCardBack.setAnimation(null);
                    showCardFA();
                    appsCardFront.startAnimation(cambioYA);
                }
            }
        });
    }
}
