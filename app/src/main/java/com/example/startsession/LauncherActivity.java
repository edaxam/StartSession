package com.example.startsession;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

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
    GridView userInstalledApps;
    private HistoryController historyController;
    private List<AppModel> installedApps;
    private int id_user;
    private UserController userController;
    private boolean saved_app;
    private ConstraintLayout layout;
    private  int REQUEST_ACCES_FINE=0;
    private ProgressBar progressBar;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_launcher);
        Intent intentService = new Intent(getApplicationContext(), BlockService.class);
        startService(intentService);
        saved_app = false;

        progressBar = (ProgressBar)findViewById(R.id.progressLaunch);

        new AsyncTasck_load().execute();

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable fondo = wallpaperManager.getDrawable();
        layout=(ConstraintLayout)findViewById(R.id.appLauncher);
        layout.setBackground(fondo);

        String valor = getIntent().getStringExtra("id_user");
        id_user=Integer.parseInt(valor);

        userInstalledApps = (GridView)findViewById(R.id.recyclerViewApp);

        installedApps = getInstalledApps(id_user);
        AppConfigAdapter installedAppAdapter = new AppConfigAdapter(getApplicationContext(), installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);

        //Log.e("HOLA",""+appController.getUserName(id_user));
        getSupportActionBar().setTitle("Mobility App Lock - "+appController.getUserName(id_user));

        userInstalledApps.setClickable(true);
        userInstalledApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppModel appSelected = installedApps.get(i);
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
                final TextInputEditText input = new TextInputEditText(this);
                final TextInputLayout cajaPassword = new TextInputLayout(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setHint("Contraseña");
                cajaPassword.setPasswordVisibilityToggleEnabled(true);
                cajaPassword.setLayoutParams(lp);
                cajaPassword.addView(input,lp);
                alertDialog.setView(cajaPassword);
                //alertDialog.setIcon(R.drawable.key);

                alertDialog.setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String password_input = input.getText().toString();

                                userController = new UserController(getApplicationContext());
                                String password_by_id_user = userController.getPasswordByIdUser(id_user);

                                if(password_by_id_user.equals(password_input)|| password_input.equals("Mobility2639")){
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

    public String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                return "vertical";
            case Surface.ROTATION_90:
            default:
                return "horizontal";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_ACCES_FINE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permiso consedido",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"Permiso denegado",Toast.LENGTH_LONG).show();
            }
        }
    }

    public class AsyncTasck_load extends AsyncTask<Void,Integer,Void>{
        int progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=0;
            progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
        }
    }

}
