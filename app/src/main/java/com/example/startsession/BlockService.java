package com.example.startsession;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.rvalerio.fgchecker.AppChecker;

public class BlockService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("AccessibilityService","Inicio ");
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {


            AppChecker appChecker = new AppChecker();

            String packageName = appChecker.getForegroundApp(getApplicationContext());
            /*if ( packageName.equals("com.android.settings")){
                KillApplication(packageName);
            }*/
            Toast.makeText(getApplicationContext(), "Intent PAc" + packageName, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInterrupt() {

    }


    public void KillApplication(String KillPackage)
    {
        ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(startMain);



        am.killBackgroundProcesses(KillPackage);
        Toast.makeText(getBaseContext(),"Process Killed : " + KillPackage  ,Toast.LENGTH_LONG).show();
    }

}
