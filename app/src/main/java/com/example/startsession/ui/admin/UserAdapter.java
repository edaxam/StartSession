package com.example.startsession.ui.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.example.startsession.AddUserActivity;
import com.example.startsession.AdminActivity;
import com.example.startsession.EditUserActivity;
import com.example.startsession.LauncherActivity;
import com.example.startsession.R;
import com.example.startsession.db.model.UserModel;
import com.example.startsession.fragments.AdminConfigUserFragment;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<UserModel> listUser;


    public void setListUser(List<UserModel> listUser) {
        this.listUser = listUser;
    }

    public UserAdapter(List<UserModel> user) {
        this.listUser = user;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rowUser = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_user, viewGroup, false);
        return new MyViewHolder(rowUser);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        UserModel userModel = listUser.get(i);

        String name = userModel.getName();
        final String last_name = userModel.getLast_name();
        String mother_last_name = userModel.getMother_last_name();
        String user = userModel.getUser();

        myViewHolder.name.setText(name + " " + last_name + " " + mother_last_name);
        myViewHolder.user.setText(user);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userSelected = listUser.get(i);
                configUser(userSelected,view);
            }
        });
        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UserModel userSelected = listUser.get(i);
                startLauncher(userSelected,view);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public void startLauncher(UserModel userSelected,View v){
        Intent intent = new Intent(v.getContext(), LauncherActivity.class);
        intent.putExtra("id_user","" + userSelected.getId_user());
        v.getContext().startActivity(intent);
    }

    public void configUser(UserModel userSelected,View v){
        Toast.makeText(v.getContext(),"Cargando ..." ,Toast.LENGTH_SHORT).show();
        Log.e("Es admin",""+userSelected.getAdmin());
        Intent intent = new Intent(v.getContext(), EditUserActivity.class);
        intent.putExtra("id_user","" + userSelected.getId_user());
        intent.putExtra("user",userSelected.getUser());
        intent.putExtra("mail",userSelected.getMail());
        intent.putExtra("password",userSelected.getPassword());
        intent.putExtra("name",userSelected.getName());
        intent.putExtra("last_name",userSelected.getLast_name());
        intent.putExtra("mother_last_name",userSelected.getMother_last_name());
        intent.putExtra("admin",""+userSelected.getAdmin());

        v.getContext().startActivity(intent);
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
