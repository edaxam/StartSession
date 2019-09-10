package com.example.startsession;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.UserModel;
import com.example.startsession.fragments.BottomActionSheetConexion;
import com.example.startsession.fragments.BottomSheetDialog;

import java.io.BufferedReader;
import java.io.File;


public class BottomActionSheet  extends AppCompatActivity {

    private UserController userController;
    public Uri rutaArchivo;
    private int VALOR_RETORNO = 1;
    public BottomActionSheetConexion readBottomDialogFragment = BottomActionSheetConexion.newInstance();
    public BottomSheetDialog bottomSheetDialog = BottomSheetDialog.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userController = new UserController(getApplicationContext());

        findViewById(R.id.ArchivoC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, VALOR_RETORNO);
                Toast.makeText(getApplicationContext(),"archivo",Toast.LENGTH_SHORT).show();
                readBottomDialogFragment.dismiss();
            }
        });

        findViewById(R.id.ArchivoS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, VALOR_RETORNO);
                Toast.makeText(getApplicationContext(),"archivo",Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
    }
/*
    public void archivo(View view) {
        boolean hayConexion=isNetworkAvailable(getApplicationContext());
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*//*");
        startActivityForResult(intent, VALOR_RETORNO);
        Toast.makeText(getApplicationContext(),"archivo",Toast.LENGTH_SHORT).show();
        if (hayConexion){
            readBottomDialogFragment.dismiss();
        }else {
            bottomSheetDialog.dismiss();
        }
    }*/

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null)
        {
            //Log.e("TAG", data.getStringExtra("Note"));
            if(resultCode == RESULT_OK)
            {
                rutaArchivo = data.getData(); //obtener el uri content
                ImportarDatos(rutaArchivo);
            }
            if (resultCode == RESULT_CANCELED)
            {

            }
        }
    }

    //Metodo para manejar el Archivo lecutura e insersion de datos
    public void ImportarDatos(Uri rutaArchivo){
        //Split para el metodo de lectura del archivo para que sepa tambien en que disco se encuentra el documento y utilize la lectura correspondiente
        String [] path = rutaArchivo.getPath().split("\\/document\\/");

        //guardamos el nombre del archivo en una variable
        String tabla = getNameFile(rutaArchivo);

        //imprimimos el nombre del archivo en un TOAS
        Toast.makeText(getApplicationContext(), tabla,  Toast.LENGTH_SHORT).show();

        //Creamos la variable usuariosLeidos
        String[]usuariosLeidos=LeerArchivo(path[1]).split("\\n");

        //creamos una lista de tipo Usuarios con el nombre de registros para posterior llenarla con los datos del archivo
        //List<UserModel> registros = new ArrayList<UserModel>();

        Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_LONG).show();
        for (int i=1;i<usuariosLeidos.length;i++)
        {
            String[] tupla = usuariosLeidos[i].split("[a-z A-Z 0-9]+");
            UserModel newUser = new UserModel(tupla[2],tupla[1],tupla[3],tupla[4],tupla[5],tupla[6],tupla[8],Integer.parseInt(tupla[7]),Integer.parseInt(tupla[9]), Integer.parseInt(tupla[10]));
            long id_user = userController.importTables(tabla,newUser);

            if(id_user == -1){
                Toast.makeText(getApplicationContext(), "Error al importar. Intenta de nuevo", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Guardado correctamente", Toast.LENGTH_LONG).show();
            }
        }

    }


    public String LeerArchivo(String archivo) {
        //Creamos una variable de tipo StringBuilder para ir recolectando todos los datos
        StringBuilder text = new StringBuilder();

        //Creamos una variable llamada importarD para que sea la que tenga el documento
        File importarD;

        //Creamos una variable llamada Path que tendra la direccion del archivo y que esta ocuparemos pra saber en que disco se encuenta
        String[] path = archivo.split("[A-Z a-z 0-9]+:");

        try {
            //en la variable importarD ponermos el archivo para obtenerolo y leerlo
            importarD = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path[1]);

            //imprime la ruta en la que esta
            //Toast.makeText(bottomActionSheet,""+importarD,  Toast.LENGTH_SHORT).show();

            //Leemos el archivo
            BufferedReader fileReader = new BufferedReader(new java.io.FileReader(importarD));

            //Creamos una variable llamada line para guardar lo el contenido momentaneamente
            String line;

            //esto lo hara hata que ya no encuentre mas contenido
            while ((line = fileReader.readLine()) != null) {
                text.append(line);
            }

            //Cerramos el archivo
            fileReader.close();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error en la Lectura del Archivo", Toast.LENGTH_SHORT).show();
            //SI no se puede encontrar el archivo mostrara el siguiente texto
            //text.append("Error en la Lectura del Archivo");
        }
        //al final regresamos la variable text
        return text.toString();
    }


    //Metodo para obtener el nombre del Archivo
    public String getNameFile(Uri rutaArchivo){

        //Split para eliminar la ruta hasta el disco
        String [] noDisk = rutaArchivo.getPath().split("\\/document\\/[A-Z a-z 0-9]+:");

        //Split para separar por carpetas hasta el archivo
        String [] noFolder = noDisk[1].split("\\/");

        //Split para eliminar la extencion del archivo pero esto como no sabemos en que posision esta el archivo tomamos la longitud de noFolder
        //y le restamos uno para obtener la ubicacion de este y separalo
        String [] nombre = noFolder[noFolder.length-1].split("\\.[A-Z a-z]+");

        //retornamos el nombre en la pocision 0 ya que es el unico valor
        return nombre[0];
    }

}
