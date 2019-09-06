package com.example.startsession.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.startsession.db.model.UserModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class BottomActionSheet  extends AppCompatActivity {

    public BottomActionSheetConexion addPhotoBottomDialogFragment =null;
    public BottomSheetDialog bottomSheetDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPhotoBottomDialogFragment= BottomActionSheetConexion.newInstance();
        bottomSheetDialog=BottomSheetDialog.newInstance();
    }

    public void ImportarDatos(Uri rutaArchivo){
        Toast.makeText(this, "HIOLAAA", Toast.LENGTH_SHORT).show();
        String [] pat = rutaArchivo.getPath().split("\\/document\\/");
        String [] pat1 = rutaArchivo.getPath().split("\\/document\\/[A-Z a-z 0-9]+:");
        String [] pat2 = pat1[1].split("\\/");
        String [] nombre = pat2[pat2.length-1].split("\\.[A-Z a-z]+");
        Toast.makeText(this, nombre[0],  Toast.LENGTH_SHORT).show();

        String[]usuariosLeidos=LeerArchivo(pat[1]).split("\\n");
        String[][]usuarios=null;
        for (int i=1;i<=usuariosLeidos.length;i++){
            for (int j=1;j<12;j++){
                usuarios[i]=usuariosLeidos[i].split(",");
                Toast.makeText(this,usuarios[i][j],  Toast.LENGTH_SHORT).show();
            }
        }

        String tabla = nombre[1];
        List<UserModel> registros = new ArrayList<UserModel>();

    }

    public String LeerArchivo(String archivo){
        StringBuilder text = new StringBuilder();
        File importarD;
        String [] pat1 = archivo.split("[A-Z a-z 0-9]+:");
        if (pat1[0]=="primary:")
        {
            try
            {
                BufferedReader fin =
                        new BufferedReader(
                                new InputStreamReader(
                                        openFileInput(pat1[1])));

                text.append(fin.readLine()+"\n");
                fin.close();
            }catch (Exception ex){
                Toast.makeText(this, "Error en la Lectura del Archivo",  Toast.LENGTH_SHORT).show();
                text.append("Error en la Lectura del Archivo");
            }
        } else {
            try
            {
                importarD = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),pat1[1]);
                Toast.makeText(this,""+importarD,  Toast.LENGTH_SHORT).show();
                BufferedReader fileReader = new BufferedReader(new FileReader(importarD));
                String line;
                while ((line=fileReader.readLine())!=null){
                    text.append(line);
                }
                fileReader.close();
            }catch (Exception ex){
                Toast.makeText(this, "Error en la Lectura del Archivo",  Toast.LENGTH_SHORT).show();
                text.append("Error en la Lectura del Archivo");
            }
        }
        return text.toString();
    }
}
