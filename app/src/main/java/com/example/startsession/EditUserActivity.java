package com.example.startsession;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditUserActivity extends AppCompatActivity {
    private EditText editTextUser, editTextMail,editTextPassword,editTextName,editTextLastName,editTextMotherLastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        editTextUser  = findViewById(R.id.input_user);
        editTextMail = findViewById(R.id.input_mail);
        editTextPassword = findViewById(R.id.input_password);
        editTextName = findViewById(R.id.input_name);
        editTextLastName = findViewById(R.id.input_last_name);
        editTextMotherLastName = findViewById(R.id.input_mother_last_name);

        editTextUser.setText(intent.getStringExtra("user"));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    protected void onResume() {

        super.onResume();
    }
}
