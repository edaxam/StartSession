package com.example.startsession.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.startsession.MainActivity;
import com.example.startsession.R;
import com.example.startsession.ReadQRActivity;
import com.example.startsession.db.DBHelper;
import com.example.startsession.db.controller.AppController;
import com.example.startsession.ui.admin.CSVWriter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminImportExportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminImportExportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminImportExportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AppController appController;

    private OnFragmentInteractionListener mListener;

    public AdminImportExportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminImportExportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminImportExportFragment newInstance(String param1, String param2) {
        AdminImportExportFragment fragment = new AdminImportExportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_import_export, container, false);
        CardView exportar = (CardView) view.findViewById(R.id.exportar);
        exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxPermissions permissions = new RxPermissions(getActivity());
                permissions.setLogging(true);
                permissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                ExportDatabaseCSVTask export_db =  new ExportDatabaseCSVTask();
                                boolean status =  export_db.doInBackground();
                                if(status){
                                    Toast.makeText(getContext(), "Exportado Correctamente!", Toast.LENGTH_SHORT).show();
                                    Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage("com.cyanogenmod.filemanager");
                                    startActivity(launchIntent);
                                }else{
                                    Toast.makeText(getContext(), "Exportación Fallida", Toast.LENGTH_SHORT).show();
                                }
                                //LogUtils.error(TAG, "checkPermission22--:" + aBoolean);
                            }
                        });
                //new ExportDatabaseCSVTask();
            }
        });

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {

        private final ProgressDialog dialog = new ProgressDialog(getContext());
        DBHelper dbhelper;

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exportando Base de Datos...");
            this.dialog.show();
            dbhelper = new DBHelper(getContext());
        }

        protected Boolean doInBackground(final String... args) {

            Log.e("Args: ", "" + args);

            File exportDir = new File(Environment.getExternalStorageDirectory(), "/csvs/");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            String[] tables = {"user", "user_config_launcher", "user_history"};
            Log.e("Tamanio", "" + tables.length);
            for (int j=0; j<tables.length; j++) {
                File file_user = new File(exportDir,  tables[j]+".csv");

                try {
                    file_user.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file_user));

                    appController = new AppController(getContext());
                    Cursor curCSV = appController.exportTablas(tables[j]);

                    csvWrite.writeNext(curCSV.getColumnNames());
                    while (curCSV.moveToNext()) {
                        String arrStr[] = null;
                        String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                        for (int i = 0; i < curCSV.getColumnNames().length; i++) {
                            mySecondStringArray[i] = curCSV.getString(i);
                        }
                        csvWrite.writeNext(mySecondStringArray);
                    }
                    csvWrite.close();
                    curCSV.close();

                }
                catch (IOException e) {
                    return false;
                }
            }
            return true;

        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                Toast.makeText(getContext(), "Exportado Correctamente!", Toast.LENGTH_SHORT).show();
                //ShareGif();
            } else {
                Toast.makeText(getContext(), "Exportación Fallida", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
