package com.example.startsession.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.startsession.R;

public class AdminHomeCard extends AppCompatActivity {
    private LinearLayout usuCardFront;
    private LinearLayout usuCardBack;

    private ScaleAnimation cambioX = new ScaleAnimation(1, 0, 1, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
    private ScaleAnimation cambioY = new ScaleAnimation(0, 1, 1, 1,Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_home);
        ViewGroup root =(ViewGroup)getWindow().getDecorView().findViewById(R.id.FrameUser);
        LinearLayout linearLayout =(LinearLayout)root.getChildAt(0);
        initView();
        FrameLayout frameLayout=(FrameLayout)linearLayout.getChildAt(1);
        frameLayout.setOnClickListener(new View.OnClickListener() {
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

    public void Inicios(){
        initView();
        FrameLayout frameLayout=(FrameLayout)findViewById(R.id.FrameUser);
        frameLayout.setOnClickListener(new View.OnClickListener() {
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

    private void initView(){
        usuCardFront = (LinearLayout) findViewById(R.id.UTfront);
        usuCardBack = (LinearLayout) findViewById(R.id.UTback);
        showCardF();
        cambioX.setDuration(500);
        cambioY.setDuration(500);

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
}
