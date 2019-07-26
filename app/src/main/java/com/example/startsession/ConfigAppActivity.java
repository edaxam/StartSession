package com.example.startsession;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startsession.db.controller.AppController;
import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.AppModel;
import com.example.startsession.db.model.UserModel;
import com.example.startsession.ui.admin.AppAdapter;

import java.util.ArrayList;
import java.util.List;

public class ConfigAppActivity extends AppCompatActivity {
    private TextView userName, mailPassword;
    private List<AppModel> installedApps;
    private AppController appController;
    ListView userInstalledApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        final int id_user = Integer.parseInt(intent.getStringExtra("id_user"));


        userName  = findViewById(R.id.user_full_name);
        mailPassword  = findViewById(R.id.mail_password);

        String stringUserName = "ID: " + intent.getStringExtra("id_user") + " - " + intent.getStringExtra("user") + " - "
                + intent.getStringExtra("name") + " "
                + intent.getStringExtra("last_name") + " "
                + intent.getStringExtra("mother_last_name");

        String stringMailPasword = intent.getStringExtra("mail") + " || " + intent.getStringExtra("password") ;

        userName.setText(stringUserName);
        mailPassword.setText(stringMailPasword);


        userInstalledApps = (ListView)findViewById(R.id.recyclerViewApp);

        installedApps = getInstalledApps();
        AppAdapter installedAppAdapter = new AppAdapter(getApplicationContext(), installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);


        appController = new AppController(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Error al guardar. Intenta de nuevo", Toast.LENGTH_LONG).show();
                CheckBox cb;
                ListView mainListView = userInstalledApps;
                for (int x = 0; x<mainListView.getChildCount();x++){

                    cb = (CheckBox)mainListView.getChildAt(x).findViewById(R.id.rowCheckBox);
                    if(cb.isChecked()){
                        AppModel appSelected = installedApps.get(x);

                        String app_name = appSelected.getApp_name();
                        String app_flag_system = appSelected.getApp_flag_system();
                        String app_icon_string = appSelected.getApp_icon_string();

                        AppModel newApp = new AppModel(id_user, app_name, app_flag_system, app_icon_string);
                        long id_app = appController.addApp(newApp);
                        Log.e("RESPONSE","Id :" + id_app);
                        if(id_app  == -1){
                            Toast.makeText(getApplicationContext(), "Error al guardar. Intenta de nuevo", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Guardado correctamente", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }


    private List<AppModel> getInstalledApps() {
        List<AppModel> res = new ArrayList<AppModel>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                String appFlag = p.applicationInfo.packageName;
                //getPackageManager().getLaunchIntentForPackage(ApplicationInfo info)

                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                res.add(new AppModel(appName, appFlag, icon));
            }
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }
}
