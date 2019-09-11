package com.example.startsession;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.startsession.ui.main.SliderAdapter;

public class InstructionsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS="PreferenciasLaunch";
    public static final String LAUNCH_INSTRUCCIONS="Preferencias";
    private boolean muestra;
    public int width;

    private ViewPager viewPager;
    private SliderAdapter myAdapter;
    public Button btnNext;
    public Button btnSkip;

    private LinearLayout dots_layout;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cargarDatos();
        if (!muestra){
            loadHome();
        }

        setContentView(R.layout.activity_instructions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        myAdapter=new SliderAdapter(this);
        viewPager.setAdapter(myAdapter);
        dots_layout = (LinearLayout)findViewById(R.id.dotsLayout);

        btnNext = (Button)findViewById(R.id.btnnext);
        btnSkip = (Button)findViewById(R.id.btnskip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHome();
                guardarDatos(false);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextSlide();
            }
        });

        crearteDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                crearteDots(position);
                if (position==myAdapter.lst_title.length-1){
                    btnNext.setText("START");
                    btnSkip.setVisibility(View.INVISIBLE);
                }else{
                    btnNext.setText("NEXT");
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //Metodo para los puntos de proceso
    private void crearteDots(int current_position) {
        if (dots_layout != null) {
            dots_layout.removeAllViews();
        }

        dots=new ImageView[myAdapter.lst_image.length];

        for (int i=0;i<dots.length;i++){
            dots[i]=new ImageView(this);
            if (i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            dots_layout.addView(dots[i],params);
        }
    }

    //Metodo para cargar el login
    private void loadHome(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    //Metodo para mostrar la siguiente ventana
    private void loadNextSlide(){
        int next_slide=viewPager.getCurrentItem()+1;
        if (next_slide<myAdapter.lst_image.length){
            viewPager.setCurrentItem(next_slide);
        } else {
            loadHome();
            //guardarDatos(false);
        }
    }

    //Metodo para guardar datos de preferencias
    public void guardarDatos(boolean isFirst){
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LAUNCH_INSTRUCCIONS,isFirst);
        editor.apply();
    }

    //Metodo para cargar datos de preferencias
    public void cargarDatos(){
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        muestra=preferences.getBoolean(LAUNCH_INSTRUCCIONS,true);
    }

    //Metodo para verificacion de Resolucion
    public void displayResolution(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels
    }
}
