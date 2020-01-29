package com.example.startsession.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.startsession.InstructionsActivity;
import com.example.startsession.R;

import static android.content.Context.MODE_PRIVATE;

public class SliderAdapter extends PagerAdapter implements View.OnClickListener {
    public static final String SHARED_PREFS="Preferencias";
    public static final String BTN_LAUNCHER = "inicio";
    public static final String BTN_SCREEN = "pantalla";
    public static final String BTN_ACESIBILITY = "accesibilidad";
    Activity activity;
    Context context;
    LayoutInflater inflarte;
    private View.OnClickListener listener;

    public int[] lst_image = {
            R.drawable.mobility_logo,
            R.drawable.permisos,
            R.drawable.pantalla,
            R.drawable.accesibilidad
    };
    public String[] lst_Nimage = {
            "mobility_logo",
            "permisos",
            "pantalla",
            "accesibilidad"
    };

    public String [] lst_backgound_color={
            "#212121",
            "#F15ABD",
            "#F15ABD",
            "#F15ABD"
    };

    public String [] lst_text_buttons={
            "",
            " Configurar Launcher ",
            " Configurar Pantalla ",
            " Habilitar Accesibilidad "
    };

    public String [] lst_title = {
            "MOBILITY LOCK",
            "Configuracion de Launcher",
            "Configuración de Pantalla",
            "Habilitar Accesibilidad"
    };

    public String [] lst_description = {
            "",
            "Habilitar como aplicacion de inicio o Home",
            "Definir el tamaño de la pantalla en pequeña",
            "Buscar Mobility y habilitar superpocición"
    };

    public String [] lst_adress = {
            "",
            "Aplicaciones > Icono de engran > Aplicación de inicio",
            "Pantalla > Tamaño de pantalla",
            "Accesibilidad > Mobility"

    };


    public  SliderAdapter(Context context,Activity activity){
        this.context=context;
        this.activity=activity;
    }

    @Override
    public int getCount(){
        return lst_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflarte=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view =inflarte.inflate(R.layout.content_slide,container,false);
        view.setOnClickListener(this);

        ImageView imgslide = (ImageView)view.findViewById(R.id.imagen);
        TextView textitle =(TextView)view.findViewById(R.id.titulo);
        TextView subtitle =(TextView)view.findViewById(R.id.txtdescipcion);
        TextView route =(TextView)view.findViewById(R.id.txtruta);
        Button button =(Button) view.findViewById(R.id.btnAccion);

        textitle.setText(lst_title[position]);
        textitle.setTextColor(Color.parseColor(lst_backgound_color[1]));
        subtitle.setText(lst_description[position]);
        route.setText(lst_adress[position]);
        if(isGif(lst_Nimage[position])){
            Glide.with(context).asGif().load(lst_image[position]).into(imgslide);
        }else{
            Glide.with(context).load(lst_image[position]).into(imgslide);
        }
        button.setText(lst_text_buttons[position]);
        button.setBackgroundColor(Color.parseColor(lst_backgound_color[position]));
        //button.setTextColor(Color.argb(0,0 ,0,0));
        button.setTextSize(16);
        button.setPadding(15,15,15,15);
        button.setId(position);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (position==1){
                    Log.e("BTN","TRUE "+BTN_LAUNCHER);
                    guardarDatos(true,BTN_LAUNCHER);
                    ((Activity) context).startActivityForResult(new Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS), 0);
                }else if (position==2){
                    Log.e("BTN","TRUE "+BTN_SCREEN);
                    guardarDatos(true,BTN_SCREEN);
                    ((Activity) context).startActivityForResult(new Intent(android.provider.Settings.ACTION_DISPLAY_SETTINGS), 0);
                }else if (position==3){
                    Log.e("BTN","TRUE "+BTN_ACESIBILITY);
                    guardarDatos(true,BTN_ACESIBILITY);
                    ((Activity) context).startActivityForResult(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    private boolean isGif(String imagen) {
        String extension = "";
        int i = imagen.lastIndexOf('.');
        int p = Math.max(imagen.lastIndexOf('/'), imagen.lastIndexOf('\\'));
        if (i > p) {
            extension = imagen.substring(i+1);
        }
        return extension.trim().equalsIgnoreCase("gif");
    }

    public void guardarDatos(boolean isFirst,String Key){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Key,isFirst);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }
}