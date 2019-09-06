package com.example.startsession.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.startsession.R;

import static android.app.Activity.RESULT_OK;

public class BottomActionSheetConexion extends BottomSheetDialogFragment {

    public static BottomActionSheetConexion newInstance() {
        return new BottomActionSheetConexion();
    }
    BottomActionSheet actionSheet = new BottomActionSheet();
    private int VALOR_RETORNO = 1;
    public Uri rutaArchivo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom, container, false);
        // get the views and attach the listener
        view.findViewById(R.id.ArchivoC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/*");
                startActivityForResult(Intent.createChooser(intent, "Busca el archivo"), VALOR_RETORNO);
                dismiss();
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK) && (requestCode == VALOR_RETORNO )) {
            rutaArchivo = data.getData(); //obtener el uri content
            actionSheet.ImportarDatos(rutaArchivo);
        }
    }
}
