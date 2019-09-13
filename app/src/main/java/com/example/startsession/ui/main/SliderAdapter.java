package com.example.startsession.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.startsession.R;

public class SliderAdapter extends PagerAdapter implements View.OnClickListener {

    Context context;
    LayoutInflater inflarte;
    private View.OnClickListener listener;

    public int[] lst_image = {
            R.drawable.mobility_logo,
            R.drawable.mobility_logo,
            R.drawable.mobility_logo,
            R.drawable.mobility_logo
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
            "Configuración > Aplicaciones > Icono de engran(Ajustes) > Aplicación de inicio",
            "Configuración > Pantalla > Tamaño de pantalla",
            "Configuración > Accesibilidad > Mobility"

    };


    public  SliderAdapter(Context context){
        this.context=context;
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
        //LinearLayout layout=(LinearLayout)view.findViewById(R.id.slidelinearlayout);

        ImageView imgslide = (ImageView)view.findViewById(R.id.imagen);
        TextView textitle =(TextView)view.findViewById(R.id.titulo);
        TextView subtitle =(TextView)view.findViewById(R.id.txtdescipcion);
        TextView route =(TextView)view.findViewById(R.id.txtruta);
        final Button button =(Button) view.findViewWithTag("btnAccion");
        //Button button = new Button (context);

        textitle.setText(lst_title[position]);
        textitle.setTextColor(Color.parseColor(lst_backgound_color[1]));
        subtitle.setText(lst_description[position]);
        route.setText(lst_adress[position]);
        imgslide.setImageResource(lst_image[position]);
        button.setText(lst_text_buttons[position]);
        button.setBackgroundColor(Color.parseColor(lst_backgound_color[position]));
        //button.setTextColor(Color.argb(0,0 ,0,0));
        button.setTextSize(16);
        button.setPadding(15,15,15,15);
        button.setId(position);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.i("TAG", "The index is " + position);
                //Log.i("TAG","The id is "+button.getId());
                if (position==1){
                   // Log.i("TAG","LAUNCHER");
                    ((Activity) context).startActivityForResult(new Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS), 0);
                }else if (position==2){
                    //Log.i("TAG","PANTALLA");
                    ((Activity) context).startActivityForResult(new Intent(android.provider.Settings.ACTION_DISPLAY_SETTINGS), 0);
                }else if (position==3){
                    //Log.i("TAG","ACCESIBLIDAD");
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

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }
}