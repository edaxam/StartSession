package com.example.startsession;


import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.startsession.db.controller.AppController;
import com.example.startsession.db.controller.HistoryController;
import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.AppModel;
import com.example.startsession.db.model.HistoryModel;

import com.example.startsession.ui.user.AppConfigAdapter;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class LauncherActivity extends AppCompatActivity {
    private AppController appController;
    ListView userInstalledApps;
    private HistoryController historyController;
    private List<AppModel> installedApps;
    private int id_user;
    private UserController userController;
    private boolean saved_app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intentService = new Intent(getApplicationContext(), BlockService.class);
        startService(intentService);
        saved_app = false;

        userController = new UserController(this);
        id_user = userController.getLastUserActive();


        if(id_user == 0){
            Intent intent_login = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent_login);
            finish();
        }

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
                //Toast.makeText(getApplicationContext(),"Item:" + i + " Flag" + appSelected.getApp_flag_system(),Toast.LENGTH_SHORT).show();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appSelected.getApp_flag_system());
                if (launchIntent != null) {

                    //INSERT user_history
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String strDate = dateFormat.format(date);
                    appController = new AppController(getApplicationContext());
                    int id_config = appController.getIdConfigByUser(id_user);

                    historyController = new HistoryController(getApplicationContext());
                    HistoryModel historyModel = new HistoryModel(id_user, id_config, strDate, 1);
                    long id_history = historyController.addHistory(historyModel);
                    if(id_history == -1){
                        saved_app = false ;
                    }
                    else{
                        saved_app = true;
                    }

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
            //if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                String appFlag = p.applicationInfo.packageName;

                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());

                appController = new AppController(getApplicationContext());
                AppModel loginUser = new AppModel(id_user,appFlag);

                boolean app_active = appController.appActiveByUser(loginUser);

                if(app_active){
                    res.add(new AppModel(appName, appFlag, icon));
                }


            //}
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launcher_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit_launcher:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("CONTRASEÑA");
                alertDialog.setMessage("Por favor ingresa tu contraseña");

                final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                //alertDialog.setIcon(R.drawable.key);

                alertDialog.setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String password_input = input.getText().toString();

                                userController = new UserController(getApplicationContext());
                                String password_by_id_user = userController.getPasswordByIdUser(id_user);

                                if(password_by_id_user.equals(password_input)){
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Contraseña erronea",Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                alertDialog.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        if(!saved_app){
            activityManager.moveTaskToFront(getTaskId(), 0);
        }
        saved_app = false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intentService = new Intent(getApplicationContext(), BlockService.class);
        startService(intentService);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Do nothing or catch the keys you want to block
        return false;
    }



}
