package com.example.startsession.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.startsession.BottomActionSheet;
import com.example.startsession.R;

public class BottomActionSheetConexion extends BottomSheetDialogFragment {

    public static BottomActionSheetConexion newInstance() {
        return new BottomActionSheetConexion();
    }

    private BottomActionSheet actionSheet;
    private int VALOR_RETORNO = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom, container, false);
       actionSheet = new BottomActionSheet();
        // get the views and attach the listener
        view.findViewById(R.id.ArchivoC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"conexion", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/*");
                startActivityForResult(intent, VALOR_RETORNO);
                dismiss();
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        Toast.makeText(getContext(),"Datos",Toast.LENGTH_SHORT).show();
        if (requestCode == VALOR_RETORNO && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            uri = resultData.getData();
            actionSheet.ImportarDatos(uri);

        }
    }
}
