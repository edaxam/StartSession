package com.example.startsession.ui.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.startsession.R;

public class DialogAddUser extends AppCompatDialogFragment {
    private EditText user;
    private EditText mail;
    private EditText password;
    private DialogAddUserListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_user,null);

        user = view.findViewById(R.id.input_user);
        mail = view.findViewById(R.id.input_mail);
        password = view.findViewById(R.id.input_password);

        builder.setView(view)
                .setTitle(R.string.new_user)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail_string = mail.getText().toString();
                        String user_string = user.getText().toString();
                        String password_string = password.getText().toString();

                        //listener.applyTexts(mail_string,user_string,password_string);
                    }
        });




        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        try {
            listener = (DialogAddUserListener) context;
        }catch (ClassCastException e){
            a =(Activity) context;
        }

    }

    public interface DialogAddUserListener{
        void applyTexts(String mail,String user, String password);
    }
}
