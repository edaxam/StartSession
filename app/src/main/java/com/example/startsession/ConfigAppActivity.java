package com.example.startsession;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.startsession.db.controller.AppController;
import com.example.startsession.db.model.AppModel;
import com.example.startsession.ui.admin.AppAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ConfigAppActivity extends AppCompatActivity {
    private TextView userName, mailPassword;
    private List<AppModel> installedApps;
    private AppController appController;
    private ListView userInstalledApps;
    private ShimmerRecyclerView shimmer_recycler_view;
    public int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_config_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        shimmer_recycler_view = findViewById(R.id.shimmer_recycler_view);

        setSupportActionBar(toolbar);
        Intent intentService = new Intent(this, BlockService.class);
        stopService(intentService);

        Intent intent = getIntent();

        id_user = Integer.parseInt(intent.getStringExtra("id_user"));


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

        new AsyncTasckLoadApps().execute();


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
//Insert
                        AppModel newApp = new AppModel(id_user, app_name, app_flag_system, app_icon_string);
                        long id_app = appController.addApp(newApp);
                        Log.e("RESPONSE","Id :" + id_app);
                        if(id_app  == -1){
                            Toast.makeText(getApplicationContext(), "Error al guardar '"+app_name+"' Intenta de nuevo", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Cambio Guardado",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }


    private List<AppModel> getInstalledApps(int id_user) {
        List<AppModel> resSN = new ArrayList<AppModel>();
        List<AppModel> resS = new ArrayList<AppModel>();
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
                if (!appFlag.equals("com.example.startsession")) {
                    resS.add(new AppModel(appName, appFlag, icon, app_active));
                }
                Collections.sort(resS,new sortAlphabetically());

            } else {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                String appFlag = p.applicationInfo.packageName;
                //getPackageManager().getLaunchIntentForPackage(ApplicationInfo info)
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                appController = new AppController(getApplicationContext());
                AppModel loginUser = new AppModel(id_user,appFlag);
                boolean app_active = appController.appActiveByUser(loginUser);
                if (!appFlag.equals("com.example.startsession")) {
                    resSN.add(new AppModel(appName, appFlag, icon, app_active));
                }
                Collections.sort(resSN,new sortAlphabetically());
            }
        }
        resS.add(0,new AppModel(0,"Aplicaiones de Usuario","",""));
        resSN.add(0,new AppModel(0,"Aplicaiones del Sistema","",""));
        resS.addAll(resSN);
        return resS;
    }


    private class sortAlphabetically implements Comparator<AppModel>{
        @Override
        public int compare(AppModel o1, AppModel o2) {
            return o1.getApp_name().compareTo(o2.getApp_name());
        }
    }


    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public class AsyncTasckLoadApps extends AsyncTask<Void,Integer,Void>{
        int progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=0;
            shimmer_recycler_view.showShimmerAdapter();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (progress<100){
                progress++;
                publishProgress(progress);
                SystemClock.sleep(20);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //shimmer_recycler_view.setVisibility(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            installedApps = getInstalledApps(id_user);
            AppAdapter installedAppAdapter = new AppAdapter(getApplicationContext(), installedApps);
            userInstalledApps.setAdapter(installedAppAdapter);
            appController = new AppController(getApplicationContext());
            appController.afterInsert(id_user);

            shimmer_recycler_view.setVisibility(View.GONE);
            userInstalledApps.setVisibility(View.VISIBLE);
        }
    }
}
