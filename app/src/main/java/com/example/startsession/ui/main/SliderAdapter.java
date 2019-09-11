package com.example.startsession.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.startsession.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflarte;

    public int[] lst_image={
            R.drawable.mobility_logo,
            R.drawable.mobility_logo,
            R.drawable.mobility_logo
    };

    public String [] lst_title={
            "",
            "",
            "Configuracion de Pantalla"
    };

    public String [] lst_description={
            "",
            "",
            "Poner el tamaño de la pantalla en pequeña"
    };

    public String [] lst_adress={
            "",
            "",
            "Configuracion > Pantalla > Tamaño de pantalla",

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

        imgslide.setImageResource(lst_image[position]);
        textitle.setText(lst_title[position]);
        subtitle.setText(lst_description[position]);
        route.setText(lst_adress[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }


}
