package com.example.startsession;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.startsession.db.controller.AppController;
import com.example.startsession.db.model.AppModel;
import com.example.startsession.ui.user.AppConfigAdapter;


import java.util.ArrayList;
import java.util.List;


public class LauncherActivity extends AppCompatActivity {
    private AppController appController;
    ListView userInstalledApps;
    private List<AppModel> installedApps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Intent intent = getIntent();
        int id_user = Integer.parseInt(intent.getStringExtra("id_user"));


        userInstalledApps = (ListView)findViewById(R.id.recyclerViewApp);

        installedApps = getInstalledApps(id_user);
        AppConfigAdapter installedAppAdapter = new AppConfigAdapter(getApplicationContext(), installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);

        userInstalledApps.setClickable(true);
        userInstalledApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //UserModel userSelected = listUser.get(position);
                AppModel appSelected = installedApps.get(i);
                Toast.makeText(getApplicationContext(),"Item:" + i + " Flag" + appSelected.getApp_flag_system(),Toast.LENGTH_SHORT).show();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appSelected.getApp_flag_system());
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });

    }


    private List<AppModel> getInstalledApps(int id_user) {
        List<AppModel> res = new ArrayList<AppModel>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                String appFlag = p.applicationInfo.packageName;

                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());

                appController = new AppController(getApplicationContext());
                AppModel loginUser = new AppModel(id_user,appFlag);

                boolean app_active = appController.appActiveByUser(loginUser);

                if(app_active){
                    res.add(new AppModel(appName, appFlag, icon));
                }


            }
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }
}
