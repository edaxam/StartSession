package com.example.startsession.fragments;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
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

    private ScaleAnimation cambioX = new ScaleAnimation(1, 0, 1, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
    private ScaleAnimation cambioY = new ScaleAnimation(0, 1, 1, 1,Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);


    public void Usuarios(ViewGroup view){
        initView(view);
        view.findViewById(R.id.FrameUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuCardFront.getVisibility() == View.VISIBLE) {
                    usuCardFront.startAnimation(cambioX);
                }else{
                    usuCardBack.startAnimation(cambioX);
                }
            }
        });
    }

    private void showCardF(){
        usuCardFront.setVisibility(View.VISIBLE);
        usuCardBack.setVisibility(View.INVISIBLE);
    }

    private void showCardB(){
        usuCardFront.setVisibility(View.INVISIBLE);
        usuCardBack.setVisibility(View.VISIBLE);
    }

    private void initView(ViewGroup view){
        usuCardFront = (LinearLayout) view.findViewById(R.id.UTfront);
        usuCardBack = (LinearLayout) view.findViewById(R.id.UTback);
        showCardF();
        cambioX.setDuration(100);
        cambioY.setDuration(100);

        cambioX.setAnimationListener(new Animation.AnimationListener() {

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
                    usuCardBack.startAnimation(cambioY);
                }else{
                    usuCardBack.setAnimation(null);
                    showCardF();
                    usuCardFront.startAnimation(cambioY);
                }
            }
        });
    }



/*
    public void Lanzamiento(ViewGroup view){
        initViewL(view);
        view.findViewById(R.id.lanzamiento).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lanCardFront.getVisibility() == View.VISIBLE) {
                    lanCardFront.startAnimation(cambioX);
                }else{
                    lanCardBack.startAnimation(cambioX);
                }
            }
        });
    }

    private void showCardFL(){
        lanCardFront.setVisibility(View.VISIBLE);
        lanCardBack.setVisibility(View.INVISIBLE);
    }

    private void showCardBL(){
        lanCardFront.setVisibility(View.INVISIBLE);
        lanCardBack.setVisibility(View.VISIBLE);
    }

    private void initViewL(ViewGroup view){
        lanCardFront = (LinearLayout) view.findViewById(R.id.LTfront);
        lanCardBack = (LinearLayout) view.findViewById(R.id.LTback);
        showCardFL();
        cambioX.setDuration(100);
        cambioY.setDuration(100);

        cambioX.setAnimationListener(new Animation.AnimationListener() {

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
                    lanCardBack.startAnimation(cambioY);
                }else{
                    lanCardBack.setAnimation(null);
                    showCardFL();
                    lanCardFront.startAnimation(cambioY);
                }
            }
        });
    }

    public void Apps(ViewGroup view){
        initViewA(view);
        view.findViewById(R.id.apps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appsCardFront.getVisibility() == View.VISIBLE) {
                    appsCardFront.startAnimation(cambioX);
                }else{
                    appsCardBack.startAnimation(cambioX);
                }
            }
        });
    }

    private void showCardFA(){
        appsCardFront.setVisibility(View.VISIBLE);
        appsCardBack.setVisibility(View.INVISIBLE);
    }

    private void showCardBA(){
        appsCardFront.setVisibility(View.INVISIBLE);
        appsCardBack.setVisibility(View.VISIBLE);
    }

    private void initViewA(ViewGroup view){
        appsCardFront = (LinearLayout) view.findViewById(R.id.ATfront);
        appsCardBack = (LinearLayout) view.findViewById(R.id.ATback);
        showCardF();
        cambioX.setDuration(100);
        cambioY.setDuration(100);

        cambioX.setAnimationListener(new Animation.AnimationListener() {

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
                    appsCardBack.startAnimation(cambioY);
                }else{
                    appsCardBack.setAnimation(null);
                    showCardFA();
                    appsCardFront.startAnimation(cambioY);
                }
            }
        });
    }*/
}
