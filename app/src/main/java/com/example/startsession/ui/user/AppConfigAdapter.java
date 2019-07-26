package com.example.startsession.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.startsession.R;
import com.example.startsession.db.model.AppModel;


import java.util.List;

public class AppConfigAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<AppModel> listStorage;


    public AppConfigAdapter(Context context, List<AppModel> customizedListView) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        AppConfigAdapter.ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new AppConfigAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.row_app_launcher, parent, false);

            listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.list_app_name);
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.app_icon);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (AppConfigAdapter.ViewHolder) convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getApp_name());
        listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getApp_icon());

        return convertView;
    }

    static class ViewHolder {
        TextView textInListView;
        ImageView imageInListView;
    }
}
