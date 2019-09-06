package com.example.startsession.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.startsession.R;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_SHORT;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    public static BottomSheetDialog newInstance() {
        return new BottomSheetDialog();
    }
    BottomActionSheet actionSheet = new BottomActionSheet();
    private int VALOR_RETORNO = 1;
    public Uri rutaArchivo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        // get the views and attach the listener
        view.findViewById(R.id.ArchivoS).setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(getContext(), ""+rutaArchivo.getPath(), LENGTH_SHORT).show();
            actionSheet.ImportarDatos(rutaArchivo);
        }
    }
}
