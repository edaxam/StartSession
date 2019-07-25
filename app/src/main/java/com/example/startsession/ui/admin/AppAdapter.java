package com.example.startsession.ui.admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startsession.R;
import com.example.startsession.db.model.AppModel;

import java.util.List;

public class AppAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<AppModel> listStorage;


    public AppAdapter(Context context, List<AppModel> customizedListView) {
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.row_app, parent, false);

            listViewHolder.checkInListView = (CheckBox) convertView.findViewById(R.id.rowCheckBox);
            listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.list_app_name);
            listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.app_icon);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getApp_name());
        listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getApp_icon());
        listViewHolder.checkInListView.setTag(position);

        listViewHolder.checkInListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean newState = !listStorage.get(position).isChecked();
                listStorage.get(position).setChecked(newState);
            }
        });


        listViewHolder.checkInListView.setChecked(listStorage.get(position).isChecked());
        return convertView;
    }

    static class ViewHolder{
        CheckBox checkInListView;
        TextView textInListView;
        ImageView imageInListView;
    }
}