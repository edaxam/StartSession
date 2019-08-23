package com.example.startsession;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_config_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intentService = new Intent(this, BlockService.class);
        stopService(intentService);

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

        installedApps = getInstalledApps(id_user);
        AppAdapter installedAppAdapter = new AppAdapter(getApplicationContext(), installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);

        //userInstalledApps.setOnItemClickListener();

        appController = new AppController(getApplicationContext());
        appController.afterInsert(id_user);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb;
                //Log.e("Num APPS",""+userInstalledApps.getAdapter().getCount());
                int tam =userInstalledApps.getAdapter().getCount();
                for (int x = 0; x< tam;x++){
                    if (installedApps.get(x).isChecked()) {
                        AppModel appSelected = installedApps.get(x);

                        String app_name = appSelected.getApp_name();
                        String app_flag_system = appSelected.getApp_flag_system();
                        String app_icon_string = appSelected.getApp_icon_string();

                        AppModel newApp = new AppModel(id_user, app_name, app_flag_system, app_icon_string);
                        long id_app = appController.addApp(newApp);
                        Log.e("RESPONSE","Id :" + id_app);
                        if(id_app  == -1){
                            Toast.makeText(getApplicationContext(), "Error al guardar '"+app_name+"' Intenta de nuevo", Toast.LENGTH_LONG).show();
                        }
                        else{
                            //Toast.makeText(getApplicationContext(),"Cambio Guardado",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }


    private List<AppModel> getInstalledApps(int id_user) {
        List<AppModel> res = new ArrayList<AppModel>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            //if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                String appFlag = p.applicationInfo.packageName;
                //getPackageManager().getLaunchIntentForPackage(ApplicationInfo info)

                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());


                appController = new AppController(getApplicationContext());
                AppModel loginUser = new AppModel(id_user,appFlag);
                boolean app_active = appController.appActiveByUser(loginUser);
                if (!appFlag.equals("com.example.startsession")) {
                    res.add(new AppModel(appName, appFlag, icon, app_active));
                }
            //}
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
