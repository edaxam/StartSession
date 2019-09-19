package com.example.startsession;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.UserModel;
import com.example.startsession.fragments.AdminConfigAppFragment;
import com.example.startsession.fragments.AdminConfigUserFragment;
import com.example.startsession.fragments.AdminHomeFragment;
import com.example.startsession.fragments.AdminImportExportFragment;
import com.example.startsession.fragments.BottomActionSheetConexion;
import com.example.startsession.fragments.BottomSheetDialog;
import com.example.startsession.ui.admin.ViewPagerAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;

import io.reactivex.functions.Consumer;

public class AdminActivity extends AppCompatActivity implements
        AdminHomeFragment.OnFragmentInteractionListener,
        AdminImportExportFragment.OnFragmentInteractionListener,
        AdminConfigUserFragment.OnFragmentInteractionListener, AdminConfigAppFragment.OnFragmentInteractionListener{
    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments

    AdminHomeFragment homeFragment;
    AdminConfigUserFragment configUserFragment;
    AdminImportExportFragment importExportFragment;
    AdminConfigAppFragment adminConfigAppFragment;
    MenuItem prevMenuItem;

    //bottom
    private UserController userController;
    public Uri rutaArchivo;
    private int VALOR_RETORNO = 1;
    private  int REQUEST_ACCES_FINE=0;
    public BottomActionSheetConexion readBottomDialogFragment = BottomActionSheetConexion.newInstance();
    public BottomSheetDialog bottomSheetDialog = BottomSheetDialog.newInstance();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin);
        Intent intentService = new Intent(this, BlockService.class);
        stopService(intentService);
        userController = new UserController(getApplicationContext());
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_config_user:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_config_app:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.navigation_import_export:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*  //Disable ViewPager Swipe
       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        */
        setupViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment=new AdminHomeFragment();
        configUserFragment=new AdminConfigUserFragment();
        adminConfigAppFragment =new AdminConfigAppFragment();
        importExportFragment=new AdminImportExportFragment();
        adapter.addFragment(homeFragment);
        adapter.addFragment(configUserFragment);
        adapter.addFragment(adminConfigAppFragment);
        adapter.addFragment(importExportFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intentService = new Intent(this, BlockService.class);
        stopService(intentService);
        Log.e("Servcio","Detenido");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.wallpaper:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "content://media/internal/images/media"));
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Metodo de ejecucio
    public  void Importacion(View view){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_ACCES_FINE);
        }
        boolean hayConexion=isNetworkAvailable(this);
        if (hayConexion){
            readBottomDialogFragment.show(getSupportFragmentManager(), "bottomactionsheetconexion");
            //Toast.makeText(getContext(),"Si hay conexion", LENGTH_SHORT).show();
        }else {
            bottomSheetDialog.show(getSupportFragmentManager(), "bottomsheetdialog");
            //Toast.makeText(getContext(),"No hay conexion", LENGTH_SHORT).show();
        }
    }

    //Metodo para el boton de archivos
    public void archivo(View view) {
        boolean hayConexion=isNetworkAvailable(this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, VALOR_RETORNO);
        if (hayConexion){
            readBottomDialogFragment.dismiss();
        }else {
            bottomSheetDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_ACCES_FINE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permiso consedido",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"Permiso denegado",Toast.LENGTH_LONG).show();
            }
        }
    }

    //Metodo que atrapa el objeto que seleccionamos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null)
        {
            //Log.e("TAG", data.getStringExtra("Note"));
            if(resultCode == RESULT_OK)
            {
                rutaArchivo = data.getData(); //obtener el uri content
                ImportarDatos(rutaArchivo,this);
            }
            if (resultCode == RESULT_CANCELED)
            {

            }
        }
    }

    //Metodo para la verificacion de conexion a internet
    public boolean isNetworkAvailable(@NotNull Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    //Metodo para manejar el Archivo lecutura e insersion de datos
    public void ImportarDatos(@NotNull Uri rutaArchivo,Context context){
        //Split para el metodo de lectura del archivo para que sepa tambien en que disco se encuentra el documento y utilize la lectura correspondiente
        String [] path = rutaArchivo.getPath().split("\\/document\\/");

        //guardamos el nombre del archivo en una variable
        String tabla = getNameFile(rutaArchivo);

        //Creamos la variable usuariosLeidos
        String datosLeidos=readFile(path[1],context);

        if (tabla.equals("user")){
            importUsers(datosLeidos,tabla);
        }else {
            importConfigLauncher(datosLeidos,tabla);
        }

    }

    //Metodo de Lectura de archivo
    public String readFile(@NotNull String archivo, Context context) {
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
            Toast.makeText(context, "Error en la Lectura del Archivo", Toast.LENGTH_SHORT).show();
            //SI no se puede encontrar el archivo mostrara el siguiente texto
            //text.append("Error en la Lectura del Archivo");
        }
        //al final regresamos la variable text
        return text.toString();
    }

    //Metodo para obtener el nombre del Archivo
    public String getNameFile(@NotNull Uri rutaArchivo){

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

    //Metodo para convertir los datos a una matriz
    public String [][] vectorToMatrix(int row, int colum,String []data){
        String [][] datosArreglo = new String[row][colum];
        int h =1;
        for (int i=0;i<row;i++){
            for (int j=0;j<colum;j++){
                datosArreglo[i][j]=data[h];
                h++;
            }
        }
        return  datosArreglo;
    }
    
    //Metodo para importar solo usuarios
    public void importUsers(String usuariosLeidos,String tabla){
        String [] datos = usuariosLeidos.split("\",\"|\"\"|\"");
        int columna=12;
        int fila=(datos.length-1)/columna;

        String [][] datosM=vectorToMatrix(fila,columna,datos);

        for (int i=1;i<datosM.length;i++)
        {
            UserModel newUser = new UserModel(datosM[i][2],datosM[i][1],datosM[i][3],datosM[i][4],datosM[i][5],datosM[i][6],datosM[i][8],Integer.parseInt(datosM[i][7]),Integer.parseInt(datosM[i][9]),Integer.parseInt(datosM[i][10]));
            long id_user = userController.importTables(tabla,newUser);

            if(id_user == -1){
                Toast.makeText(this, "Error al importar. Intenta de nuevo", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_LONG).show();
            }
        }

    }

    //Metodo para importar configuraciones de usuarios
    public  void importConfigLauncher(String configLaunchLeidos, String tabla){
        String [] datos = configLaunchLeidos.split("\",\"|\"\"|\"");
        int columna=8;
        int fila=(datos.length-1)/columna;

        String [][] datosM=vectorToMatrix(fila,columna,datos);

        for (int i=1;i<datosM.length;i++)
        {
/*
            if(id_user == -1){
                Toast.makeText(this, "Error al importar. Intenta de nuevo", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_LONG).show();
            }*/
        }
    }
}
