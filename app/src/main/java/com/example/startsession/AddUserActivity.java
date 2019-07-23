package com.example.startsession;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.UserModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddUserActivity extends AppCompatActivity {
    private EditText editTextUser, editTextMail,editTextPassword,editTextName,editTextLastName,editTextMotherLastName;
    private String stringUser,stringMail,stringPassword,stringName,stringLastName,stringMotherLastName;
    private UserController userController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userController = new UserController(getApplicationContext());

        editTextUser  = findViewById(R.id.input_user);
        editTextMail = findViewById(R.id.input_mail);
        editTextPassword = findViewById(R.id.input_password);
        editTextName = findViewById(R.id.input_name);
        editTextLastName = findViewById(R.id.input_last_name);
        editTextMotherLastName = findViewById(R.id.input_mother_last_name);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.saving, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                stringUser = editTextUser.getText().toString();
                stringMail = editTextMail.getText().toString();
                stringPassword = editTextPassword.getText().toString();
                stringName = editTextName.getText().toString();
                stringLastName = editTextLastName.getText().toString();
                stringMotherLastName = editTextMotherLastName.getText().toString();

                //Log.e("CAMPOS: ", "Usuario: " + stringUser + "Mail: " + stringMail + "Password: " + stringPassword + "Nombre: " + stringName + "Apellido Paterno : " + stringLastName + "Apellido Materno : " + stringMotherLastName);
                if(stringUser.equals("") || stringMail.equals("") || stringPassword.equals("") || stringName.equals("") || stringLastName.equals("") || stringMotherLastName.equals("") ){
                    Snackbar.make(view, R.string.error_saving, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Toast.makeText(getApplicationContext(),"Todos los campos son requeridos",Toast.LENGTH_LONG).show();
                }
                else{
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String strDate = dateFormat.format(date);

                    UserModel newUser = new UserModel(stringUser,stringPassword,stringMail,stringName,stringLastName,stringMotherLastName,strDate,1,0);
                    long id_user = userController.addUser(newUser);

                    if(id_user == -1){
                        Toast.makeText(getApplicationContext(), "Error al guardar. Intenta de nuevo", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Guardado correctamente", Toast.LENGTH_LONG).show();
                        editTextUser.setText("");
                        editTextMail.setText("");
                        editTextPassword.setText("");
                        editTextName.setText("");
                        editTextLastName.setText("");
                        editTextMotherLastName.setText("");

                        editTextUser.setFocusableInTouchMode(true);
                        editTextUser.requestFocus();
                    }
                }

            }
        });
    }

}
