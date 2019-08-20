package com.example.startsession;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class BlockService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {




        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            Log.e("TYPE_VIEW_CLICKED_gPN","" + event.getPackageName());
            Log.e("TYPE_VIEW_CLICKED_getCN","" + event.getClassName());


        }

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ) {
            if ( event.getPackageName().equals("com.android.settings") ){
                KillApplication("" + event.getPackageName());
            }
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
        
    }

}
