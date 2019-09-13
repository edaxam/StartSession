package com.example.startsession.ui.main;

import android.content.Context;
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

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflarte;

    public int[] lst_image = {
            R.drawable.mobility_logo,
            R.drawable.mobility_logo,
            R.drawable.mobility_logo,
            R.drawable.mobility_logo
    };

    public Button[]lst_buttons={

    };

    public String [] lst_text_buttons={
            "",
            "Configurar Launcher",
            "Configurar Pantalla",
            "Habilitar Accesibilidad"
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
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflarte=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view =inflarte.inflate(R.layout.content_slide,container,false);

        //LinearLayout layout=(LinearLayout)view.findViewById(R.id.slidelinearlayout);

        ImageView imgslide = (ImageView)view.findViewById(R.id.imagen);
        TextView textitle =(TextView)view.findViewById(R.id.titulo);
        TextView subtitle =(TextView)view.findViewById(R.id.txtdescipcion);
        TextView route =(TextView)view.findViewById(R.id.txtruta);
        Button button =(Button) view.findViewById(R.id.bntAccion);

        textitle.setText(lst_title[position]);
        subtitle.setText(lst_description[position]);
        route.setText(lst_adress[position]);
        imgslide.setImageResource(lst_image[position]);
        button.setText(lst_text_buttons[position]);


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }


}
