package com.example.startsession;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.startsession.ui.main.SliderAdapter;

public class InstructionsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "Preferencias";
    public static final String LAUNCH_INSTRUCCIONS = "Instrucciones";
    private boolean muestra;
    public int width;
    private int REQUEST_ACCES_FINE = 0;

    private ViewPager viewPager;
    private SliderAdapter myAdapter;
    public Button btnNext;
    public Button btnSkip;
    public LinearLayout linearLayout;

    private LinearLayout dots_layout;
    private ImageView[] dots;
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.KILL_BACKGROUND_PROCESSES,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.REORDER_TASKS,
                            Manifest.permission.GET_TASKS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.SET_WALLPAPER,
                            Manifest.permission.INTERNET,
                            Manifest.permission.WRITE_SECURE_SETTINGS,
                            Manifest.permission.WRITE_SETTINGS,
                            Manifest.permission.PACKAGE_USAGE_STATS
                    },REQUEST_ACCES_FINE);
        }
        //changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALARMS);
        cargarDatos();
        if (!muestra) {
            loadHome();
        }

        setContentView(R.layout.activity_instructions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        myAdapter = new SliderAdapter(this);
        viewPager.setAdapter(myAdapter);

        linearLayout = (LinearLayout) findViewById(R.id.slidelinearlayout);
        dots_layout = (LinearLayout) findViewById(R.id.dotsLayout);

        btnNext = (Button) findViewById(R.id.btnnext);
        btnSkip = (Button) findViewById(R.id.btnskip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHome();
                guardarDatos(false);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextSlide();
            }
        });

        crearteDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                crearteDots(position);
                if (position == myAdapter.lst_title.length - 1) {
                    btnNext.setText("INICIAR");
                    btnSkip.setVisibility(View.INVISIBLE);
                } else {
                    btnNext.setText("SIGUIENTE");
                    btnSkip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    //Metodo para los puntos de proceso
    private void crearteDots(int current_position) {
        if (dots_layout != null) {
            dots_layout.removeAllViews();
        }

        dots = new ImageView[myAdapter.lst_image.length];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            if (i == current_position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            dots_layout.addView(dots[i], params);
        }
    }

    //Metodo para cargar el login
    private void loadHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //Metodo para mostrar la siguiente ventana
    private void loadNextSlide() {
        int next_slide = viewPager.getCurrentItem() + 1;
        if (next_slide < myAdapter.lst_image.length) {
            viewPager.setCurrentItem(next_slide);
        } else {
            loadHome();
            guardarDatos(false);
        }
    }

    //Metodo para guardar datos de preferencias
    private void guardarDatos(boolean isFirst) {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LAUNCH_INSTRUCCIONS, isFirst);
        editor.apply();
    }

    //Metodo para cargar datos de preferencias
    public boolean cargarDatos() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return muestra = preferences.getBoolean(LAUNCH_INSTRUCCIONS, true);
    }

    //Metodo para verificacion de Resolucion
    private void displayResolution() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels
    }


    protected void changeInterruptionFiler(int interruptionFilter){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){ // If api level minimum 23
            /*
                boolean isNotificationPolicyAccessGranted ()
                    Checks the ability to read/modify notification policy for the calling package.
                    Returns true if the calling package can read/modify notification policy.
                    Request policy access by sending the user to the activity that matches the
                    system intent action ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS.

                    Use ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED to listen for
                    user grant or denial of this access.

                Returns
                    boolean

            */
            // If notification policy access granted for this package
            if(mNotificationManager.isNotificationPolicyAccessGranted()){
                /*
                    void setInterruptionFilter (int interruptionFilter)
                        Sets the current notification interruption filter.

                        The interruption filter defines which notifications are allowed to interrupt
                        the user (e.g. via sound & vibration) and is applied globally.

                        Only available if policy access is granted to this package.

                    Parameters
                        interruptionFilter : int
                        Value is INTERRUPTION_FILTER_NONE, INTERRUPTION_FILTER_PRIORITY,
                        INTERRUPTION_FILTER_ALARMS, INTERRUPTION_FILTER_ALL
                        or INTERRUPTION_FILTER_UNKNOWN.
                */

                // Set the interruption filter
                mNotificationManager.setInterruptionFilter(interruptionFilter);
            }else {
                /*
                    String ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                        Activity Action : Show Do Not Disturb access settings.
                        Users can grant and deny access to Do Not Disturb configuration from here.

                    Input : Nothing.
                    Output : Nothing.
                    Constant Value : "android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS"
                */
                // If notification policy access not granted for this package
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }

}
