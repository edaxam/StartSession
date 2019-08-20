package com.example.startsession;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.startsession.fragments.AdminConfigAppFragment;
import com.example.startsession.fragments.AdminConfigUserFragment;
import com.example.startsession.fragments.AdminHomeFragment;
import com.example.startsession.fragments.AdminImportExportFragment;
import com.example.startsession.ui.admin.ViewPagerAdapter;

public class AdminActivity extends AppCompatActivity implements
        AdminHomeFragment.OnFragmentInteractionListener,
        AdminImportExportFragment.OnFragmentInteractionListener,
        AdminConfigUserFragment.OnFragmentInteractionListener, AdminConfigAppFragment.OnFragmentInteractionListener{
    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;


    //Fragments

    AdminHomeFragment homeFragment;
    AdminConfigUserFragment configUserFragment;
    AdminImportExportFragment importExportFragment;
    AdminConfigAppFragment adminConfigAppFragment;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intentService = new Intent(this, BlockService.class);
        stopService(intentService);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_config_user:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_config_app:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.navigation_import_export:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*  //Disable ViewPager Swipe
       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        */

        setupViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment=new AdminHomeFragment();
        configUserFragment=new AdminConfigUserFragment();
        adminConfigAppFragment =new AdminConfigAppFragment();
        importExportFragment=new AdminImportExportFragment();
        adapter.addFragment(homeFragment);
        adapter.addFragment(configUserFragment);
        adapter.addFragment(adminConfigAppFragment);
        adapter.addFragment(importExportFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intentService = new Intent(this, BlockService.class);
        stopService(intentService);
        Log.e("Servcio","Detenido");
    }
}
