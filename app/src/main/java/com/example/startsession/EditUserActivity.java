package com.example.startsession;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.UserModel;
import com.github.clans.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditUserActivity extends AppCompatActivity {
    private EditText editTextUser, editTextMail,editTextPassword,editTextName,editTextLastName,editTextMotherLastName;
    private String stringUser,stringMail,stringPassword,stringName,stringLastName,stringMotherLastName;
    private UserController userController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_edit_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        userController = new UserController(getApplicationContext());

        editTextUser  = findViewById(R.id.input_user);
        editTextMail = findViewById(R.id.input_mail);
        editTextPassword = findViewById(R.id.input_password);
        editTextName = findViewById(R.id.input_name);
        editTextLastName = findViewById(R.id.input_last_name);
        editTextMotherLastName = findViewById(R.id.input_mother_last_name);

        editTextUser.setText(intent.getStringExtra("user"));
        editTextMail.setText(intent.getStringExtra("mail"));
        editTextPassword.setText(intent.getStringExtra("password"));
        editTextName.setText(intent.getStringExtra("name"));
        editTextLastName.setText(intent.getStringExtra("last_name"));
        editTextMotherLastName.setText(intent.getStringExtra("mother_last_name"));

        //Log.e("Llega","" + intent.getStringExtra("id_user"));

        final long id_user = (long) Long.parseLong(intent.getStringExtra("id_user"));
        //Log.e("id","" + id_user);

        final CheckBox check_admin = (CheckBox) findViewById(R.id.checkbox_admin);
        if(intent.getStringExtra("admin").equals("1")){
            check_admin.setChecked(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Guardando Cambios", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                stringUser = editTextUser.getText().toString();
                stringMail = editTextMail.getText().toString();
                stringPassword = editTextPassword.getText().toString();
                stringName = editTextName.getText().toString();
                stringLastName = editTextLastName.getText().toString();
                stringMotherLastName = editTextMotherLastName.getText().toString();


                if(stringUser.equals("") || stringMail.equals("") || stringPassword.equals("") || stringName.equals("") || stringLastName.equals("") || stringMotherLastName.equals("") ){
                    Snackbar.make(view, R.string.error_saving, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Toast.makeText(getApplicationContext(),"Todos los campos son requeridos",Toast.LENGTH_LONG).show();
                }
                else{
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String strDate = dateFormat.format(date);


                    int admin = 0;

                    if(check_admin.isChecked()== true){
                        admin = 1;
                    }

                    Log.e("Es admin ","" + admin);
                    UserModel editUser = new UserModel(stringUser,stringMail,stringPassword,stringName,stringLastName,stringMotherLastName,strDate,1,0, id_user, admin);

                    int id_user_up = userController.updateUser(editUser);

                    if(id_user_up == -1){
                        Toast.makeText(getApplicationContext(), "Error al guardar. Intenta de nuevo", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Guardado correctamente", Toast.LENGTH_LONG).show();

                        editTextUser.setFocusableInTouchMode(true);
                        editTextUser.requestFocus();
                        finish();
                    }
                }

            }
        });

        //DELETE
        FloatingActionButton elim = findViewById(R.id.fab_del);
        elim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Eliminar Registro", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                //Log.e("id","" + id_user);
                UserModel elimUser = new UserModel(id_user);
                int id_user_de = userController.deleteUser(elimUser);
                if(id_user_de == -1){
                    Toast.makeText(getApplicationContext(), "Error al eliminar", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Eliminado correctamente", Toast.LENGTH_LONG).show();
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
        });
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intentService = new Intent(getApplicationContext(), BlockService.class);
        startService(intentService);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
}
