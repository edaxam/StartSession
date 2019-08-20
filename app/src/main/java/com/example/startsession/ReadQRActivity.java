package com.example.startsession;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.example.startsession.db.controller.UserController;
import com.example.startsession.fragments.RegisterFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import io.reactivex.functions.Consumer;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ReadQRActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private UserController userController;

    //camera permission is needed.

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.e("OnCreate","Inicia");

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA) // ask single or multiple permission once
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            // All requested permissions are granted
                            mScannerView = new ZBarScannerView(ReadQRActivity.this);    // Programmatically initialize the scanner view
                            ReadQRActivity.this.setContentView(mScannerView);                // Set the scanner view as the content view
                        } else {
                            // At least one permission is denied
                        }
                    }
                });



    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume","Inicia");
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA) // ask single or multiple permission once
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            // Start camera on resume
                            mScannerView.setResultHandler(ReadQRActivity.this); // Register ourselves as a handler for scan results.
                            mScannerView.startCamera();
                        } else {
                            // At least one permission is denied
                        }
                    }
                });
        //mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        //mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause","Inicia");
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA) // ask single or multiple permission once
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            // Start camera on resume
                            mScannerView.stopCamera();           // Stop camera on pause
                        } else {
                            // At least one permission is denied
                        }
                    }
                });
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        // Do something with the result here

        JSONObject mainObject = null;
        String  user = "";
        String password = "";
        String old_password = "";
        String textUser = "";
        try {
            mainObject = new JSONObject(result.getContents());
            // recoberyqr
            JSONObject uniObject = mainObject.getJSONObject("cmVjb2Jlcnlxcg==");
            user = uniObject.getString("dXNlcg==");
            password = uniObject.getString("cGFzc3dvcmQ=");

            byte[] data = Base64.decode(user, Base64.DEFAULT);
            textUser = new String(data, "UTF-8");

            if(password.equals("dHJ1ZQ==")){
                userController = new UserController(this);
                old_password = userController.getPassword(textUser);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        RegisterFragment.user.setText(textUser);
        RegisterFragment.password.setText(old_password);

        onBackPressed();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}