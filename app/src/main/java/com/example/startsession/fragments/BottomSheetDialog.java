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

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private BottomActionSheet actionSheet ;
    private int VALOR_RETORNO = 1;


    public static BottomSheetDialog newInstance() {
        return new BottomSheetDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        actionSheet = new BottomActionSheet();
        // get the views and attach the listener
        view.findViewById(R.id.ArchivoS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"noConexion",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/*");
                startActivityForResult(intent, VALOR_RETORNO);
                dismiss();
            }
        });
        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(),"sinconexion Resultado",Toast.LENGTH_SHORT).show();
        if ((resultCode == RESULT_OK) && (requestCode == VALOR_RETORNO )) {
            Uri rutaArchivo = data.getData(); //obtener el uri content
            try {
                actionSheet.ImportarDatos(rutaArchivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
