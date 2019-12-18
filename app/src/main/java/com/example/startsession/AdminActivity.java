package com.example.startsession;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.startsession.db.controller.AppController;
import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.AppModel;
import com.example.startsession.db.model.ResponseServiceModel;
import com.example.startsession.db.model.UserModel;
import com.example.startsession.fragments.AdminConfigAppFragment;
import com.example.startsession.fragments.AdminConfigUserFragment;
import com.example.startsession.fragments.AdminHomeFragment;
import com.example.startsession.fragments.AdminImportExportFragment;
import com.example.startsession.fragments.BottomActionSheetConexion;
import com.example.startsession.fragments.LoginFragment;
import com.example.startsession.interfaces.SendInfo;
import com.example.startsession.interfaces.UserService;
import com.example.startsession.ui.admin.ViewPagerAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity implements
    AdminHomeFragment.OnFragmentInteractionListener,
    AdminImportExportFragment.OnFragmentInteractionListener,
    AdminConfigUserFragment.OnFragmentInteractionListener, AdminConfigAppFragment.OnFragmentInteractionListener {

    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;
    public static final String SHARED_PREFS = "Preferencias";
    public static final String CLOUD_PREFS = "EsPrimera";
    public static final String CLOUD_USER = "user";
    public static final String CLOUD_PASSWORD = "password";
    private boolean isFirts;

    //Fragments

    AdminHomeFragment homeFragment;
    AdminConfigUserFragment configUserFragment;
    AdminImportExportFragment importExportFragment;
    AdminConfigAppFragment adminConfigAppFragment;
    MenuItem prevMenuItem;

    //bottom
    private UserController userController;
    private AppController appController;
    public Uri rutaArchivo;
    private int VALOR_RETORNO = 1;
    public BottomActionSheetConexion readBottomDialogFragment = BottomActionSheetConexion.newInstance();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin);
        Intent intentService = new Intent(this, BlockService.class);
        stopService(intentService);
        appController = new AppController(getApplicationContext());
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
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
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

    //El Back Press retorna a la dashboard
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            viewPager.setCurrentItem(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        if (item.getItemId() == R.id.wallpaper) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "content://media/internal/images/media"));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Metodo de ejecucio
    public  void Importacion(View view){
        boolean hayConexion=isNetworkAvailable(this);
        if (hayConexion){
            readBottomDialogFragment.show(getSupportFragmentManager(), "bottomactionsheetconexion");
        }else {
            Archivo(view);
        }
    }

    //Metodo para el boton de nube
    public void Nube(View view){
        cargarDatos();
        String [] datos=cargarUserPassword();
        LoadDataCloud(datos[0],datos[1]);
        readBottomDialogFragment.dismiss();
    }

    //Metodo para el boton de archivos
    public void Archivo(View view) {
        boolean hayConexion=isNetworkAvailable(this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        startActivityForResult(Intent.createChooser(intent, "Importación"), VALOR_RETORNO);
        if (hayConexion){
            readBottomDialogFragment.dismiss();
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
        }else if(tabla.equals("user_config_launcher")){
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
        LoginFragment log=new LoginFragment();
        for (int i=1;i<datosM.length;i++)
        {
            UserModel newUser = new UserModel(datosM[i][2],datosM[i][1],datosM[i][3],datosM[i][4],datosM[i][5],datosM[i][6],datosM[i][8],Integer.parseInt(datosM[i][7]),Integer.parseInt(datosM[i][9]),Integer.parseInt(datosM[i][10]));
            JSONArray jsonArray=log.Cursor2JSON(userController.searchUser(newUser));

            if (jsonArray.length()==0){
                long id_user = userController.importTables(tabla,newUser);

                if(id_user == -1){
                    Toast.makeText(this, "Error al importar. Intenta de nuevo", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    //Metodo para importar configuraciones de usuarios
    public  void importConfigLauncher(String configLaunchLeidos, String tabla){
        String [] datos = configLaunchLeidos.split("\",\"|\"\"|\"");
        int columna=7;
        int fila=(datos.length-1)/columna;

        String [][] datosM=vectorToMatrix(fila,columna,datos);

        for (int i=1;i<datosM.length;i++){
            Log.e("Datos",""+datosM[i][0]+" "+datosM[i][1]+" "+datosM[i][2]+" "+datosM[i][3]+" "+datosM[i][5]+" "+datosM[i][6]);
            int id_user=userController.getUserId(datosM[i][0]);
            String app_icon_string = getIcon(datosM[i][2]);

            if (id_user<=0){
                Toast.makeText(this, "Error No se encontro el usuario", Toast.LENGTH_LONG).show();
            }else{
                                //new AppModel(id_user, app_name, app_flag_system, app_icon_string);
                AppModel newApp = new AppModel(id_user,datosM[i][1],datosM[i][2],app_icon_string);
                int []status={Integer.parseInt(datosM[i][5]), Integer.parseInt(datosM[i][6])};
                long id_app=appController.importConfigApps(newApp,status);

                if (id_app==-1){
                    Toast.makeText(this, "Error al importar. Intenta de nuevo", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Metodo para optener el nombre del Drawable de la app
    public String getIcon(String packageFlag){
        String icon="";
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getPackageManager();
        packages = pm.getInstalledApplications(0);

        for (ApplicationInfo packageInfo : packages){
            if (packageInfo.packageName.equals(packageFlag)){
                Drawable ico = packageInfo.loadIcon(getPackageManager());
                icon=ico.toString();
                break;
            }
        }

        return icon;
    }

    //Metodo que ejecuta y da respuesta de la importacion
    public void LoadDataCloud(String user_text,String password_text) {
        boolean[] user_ws = ConectedCloud(user_text, password_text, AdminActivity.this);
        if (user_ws[0]){
            Toast.makeText(getApplication(), "Termino la actualizacion", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplication(), "El usuario no fue encontrado", Toast.LENGTH_SHORT).show();
        }

    }

    //Metodo que conecta con el API para importar los datos
    public boolean[] ConectedCloud(final String user, final String password, final Context context){
        final boolean[] user_ws = {false};

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Consumo de WS
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mobility.sysandweb.com/api/login_admin/TGVvbmFyZG9kaXNlclBpZXJvZGFWaW5jaQ==/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService usrService = retrofit.create(UserService.class);
        Call<ResponseServiceModel> callUser = usrService.getUsers(user,password);
        callUser.enqueue(new Callback<ResponseServiceModel>() {
            @Override
            public void onResponse(Call<ResponseServiceModel> call, Response<ResponseServiceModel> response) {
                ResponseServiceModel responseServiceModel = response.body();
                Log.e("onResponse","" + responseServiceModel.getMessage());
                Toast.makeText(context,responseServiceModel.getMessage(),Toast.LENGTH_LONG).show();
                if(responseServiceModel.isStatus()) {
                    guardarDatos(false,user,password);
                    user_ws[0] = true;
                    int size = responseServiceModel.getLog().size();
                    Log.e("Size ", "" + size);
                    for (int i = 0; i < responseServiceModel.getLog().size(); i++){
                        UserModel userWS = responseServiceModel.getLog().get(i);
                        LoginFragment log=new LoginFragment();
                        UserModel newUser = new UserModel(userWS.getUser(),userWS.getMail(),userWS.getPassword(),userWS.getName(),userWS.getLast_name(),userWS.getMother_last_name(),userWS.getDate_create(),1,1, userWS.getAdmin());
                        JSONArray jsonArray=log.Cursor2JSON(userController.searchUser(newUser));

                        if (jsonArray.length()==0){
                            long id_user = userController.addUser(newUser);
                            Log.e("ID User",""+ id_user+" "+userWS.getConf());
                            String json =userWS.getConf().replace("[","");
                            String jsonC =json.replace("]","");
                            try {
                                JSONObject conf = new JSONObject(jsonC);

                                Log.e("JSON", conf.toString());
                                for (int j=0;j<conf.length();j++){
                                    //AppModel AppWS = (AppModel) conf.get(String.valueOf(j));
                                    LoginFragment login = new LoginFragment();
                                    String app_name=login.getNameApp(conf.getString("app_flag_system").toLowerCase());
                                    String icon=getIcon(conf.getString("app_flag_system").toLowerCase());
                                    AppModel newApp = new AppModel((int) id_user,app_name,conf.get("app_flag_system").toString().toLowerCase(),icon,conf.getInt("active"),conf.getInt("status_ws"));
                                    long id_conf = appController.importConfigAppsWS(newApp);
                                    Log.e("ID Configuracion",""+id_conf);
                                }
                                Toast.makeText(context,"Importacion completa",Toast.LENGTH_LONG).show();
                            } catch (Throwable t) {
                                Log.e("LOG", "Could not parse malformed JSON: \"" + json + "\"");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseServiceModel> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
        return user_ws ;
    }


    /*
     *
     *   Metodos de SHARE PRFERENS
     *
     */

    //Metodo para guardar datos de preferencias
    private void guardarDatos(boolean isFirst,String user,String password) {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(CLOUD_PREFS, isFirst);
        editor.putString(CLOUD_USER,user);
        editor.putString(CLOUD_PASSWORD,password);
        editor.apply();
    }

    //Metodo para cargar datos de preferencias
    public boolean cargarDatos() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return isFirts = preferences.getBoolean(CLOUD_PREFS, true);
    }

    //Metodo para obtener el usuario y contraseña
    public String[] cargarUserPassword(){
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String datos[] = new String[2];
        datos[0]=preferences.getString(CLOUD_USER,"");
        datos[1]=preferences.getString(CLOUD_PASSWORD,"");
        return datos;
    }
}
