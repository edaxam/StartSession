package com.example.startsession.ui.admin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.example.startsession.R;
import com.example.startsession.db.model.UserModel;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<UserModel> listUser;


    public void setListUser(List<UserModel> listUser) {
        this.listUser = listUser;
    }

    public UserAdapter(List<UserModel> user){
        this.listUser = user;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rowUser = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_user, viewGroup, false);
        return new MyViewHolder(rowUser);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        UserModel userModel = listUser.get(i);

        String name = userModel.getName();
        String last_name = userModel.getLast_name();
        String mother_last_name = userModel.getMother_last_name();
        String user = userModel.getUser();

        myViewHolder.name.setText(name + " " + last_name + " " + mother_last_name);
        myViewHolder.user.setText(user);
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user, name, last_name, mother_last_name;

        MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.textViewName);
            this.user = itemView.findViewById(R.id.textViewUser);
        }
    }
}
