package com.example.startsession.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startsession.AdminActivity;
import com.example.startsession.LauncherActivity;
import com.example.startsession.R;
import com.example.startsession.db.controller.AppController;
import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.AppModel;
import com.example.startsession.db.model.ResponseServiceModel;
import com.example.startsession.db.model.UserModel;
import com.example.startsession.interfaces.SendInfo;
import com.example.startsession.interfaces.UserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CircularProgressButton circularProgressButton;

    private EditText user;
    private String userText;
    private EditText password;
    private String passwordText;
    private OnFragmentInteractionListener mListener;
    private UserController userController;
    private AppController appController;
    UserService userService;

    public static final String SHARED_PREFS = "Preferencias";
    public static final String CLOUD_PREFS = "EsPrimera";
    public static final String CLOUD_USER = "user";
    public static final String CLOUD_PASSWORD = "password";
    private boolean isFirts;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        circularProgressButton = (CircularProgressButton)view.findViewById(R.id.btn_login);

        user   = (EditText) view.findViewById(R.id.input_user);
        password = (EditText) view.findViewById(R.id.input_password);

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                userText = user.getText().toString();
                passwordText = password.getText().toString();
                Toast.makeText(getActivity(),"Conectando ...",Toast.LENGTH_SHORT).show();
                @SuppressLint("StaticFieldLeak") AsyncTask<String,String,String> inicioSession = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... voids) {
                        String result = "";
                        try {
                            Thread.sleep(3000);
                            if(userText.equals("") || passwordText.equals("")){
                                result="fail";
                            }else{
                                result="done";
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.equals("done")){
                            //circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));

                            UserModel userModel = validationLogin(userText,passwordText);
                            Log.e("LOGIN","User: " + userText + " Password: " + passwordText + " id_user: " + userModel.getId_user());

                            if( userModel.getId_user() != 0){
                                if(userModel.getId_user() > 0 && userModel.getAdmin() != 1){
                                    Intent intent = new Intent(getActivity(), LauncherActivity.class);
                                    intent.putExtra("id_user","" + userModel.getId_user());
                                    startActivity(intent);
                                    getActivity().finish();
                                    circularProgressButton.revertAnimation();
                                }
                                else{
                                    Intent intent = new Intent(getActivity(), AdminActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    circularProgressButton.revertAnimation();
                                }
                            }
                            else{
                                Toast.makeText(getActivity(),"Usuario o contraseña Incorrectos",Toast.LENGTH_LONG).show();
                                circularProgressButton.revertAnimation();
                            }
                        }else {
                            Toast.makeText(getActivity(),"Usuario o contraseña VACIOS",Toast.LENGTH_LONG).show();
                            circularProgressButton.revertAnimation();
                        }
                    }
                };
                circularProgressButton.startAnimation();
                inicioSession.execute();
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

    public UserModel validationLogin(String user, String password){

        cargarDatos();

        boolean[]user_ws={false};
        if ((!user.equals("root") && !password.equals("Mobility2639"))){
            if (!isFirts){
                String[] datos=cargarUserPassword();
                if (datos[0].equals(user)&&datos[1].equals(password)){
                    user_ws[0] = ConectedCloud(user,password,getContext());
                }else {
                    user_ws[0] = UreSecure(user,password);
                }
            }else {
                user_ws[0] = ConectedCloud(user,password,getContext());
            }
        }

        userController = new UserController(getContext());
        appController = new AppController(getContext());
        UserModel loginUser = new UserModel(user,password);
        UserModel id_user = userController.login(loginUser);

        // Root Access
        if((user.equalsIgnoreCase("root") && password.equals("Mobility2639")) || user_ws[0] ){
            id_user.setId_user(-1989);
        }

        return id_user;
    }

    public String getIcon(String packageFlag){
        String icon="";
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getActivity().getPackageManager();
        packages = pm.getInstalledApplications(0);

        for (ApplicationInfo packageInfo : packages){
            if (packageInfo.packageName.equals(packageFlag)){
                Drawable ico = packageInfo.loadIcon(getActivity().getPackageManager());
                icon=ico.toString();
                break;
            }
        }

        return icon;
    }

    public String getNameApp(String packageFlag){
        String appName="";
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getActivity().getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages){
            if (packageInfo.packageName.equals(packageFlag)){
                appName = packageInfo.loadIcon(getActivity().getPackageManager()).toString();
            }
        }
        return appName;
    }

    public boolean ConectedCloud(final String user, final String password, final Context context){
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
                    Log.e("Size:", "" + size);
                    for (int i = 0; i < responseServiceModel.getLog().size(); i++){
                        UserModel userWS = responseServiceModel.getLog().get(i);
                        //UserModel newUser = new UserModel(stringUser,stringMail,stringPassword,stringName,stringLastName,stringMotherLastName,strDate,1,0, admin);
                        UserModel newUser = new UserModel(userWS.getUser(),userWS.getMail(),userWS.getPassword(),userWS.getName(),userWS.getLast_name(),userWS.getMother_last_name(),userWS.getDate_create(),1,1, userWS.getAdmin());
                         boolean existe = userController.searchUser(newUser);
                        if (!existe){
                            long id_user = userController.addUser(newUser);
                            Log.e("ID User",""+ id_user+" "+userWS.getConf());
                            String json =userWS.getConf().replace("[","");
                            String jsonC =json.replace("]","");
                            try {
                                JSONObject conf = new JSONObject(jsonC);
                                Log.e("JSON", conf.toString());
                                for (int j=0;j<conf.length();j++){
                                    String app_name=getNameApp(conf.getString("app_flag_system").toLowerCase());
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
        return user_ws[0];
    }

    public boolean UreSecure(final String user, final String password){
        final boolean[] user_ws = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Esta seguro de esta acción");
        builder.setTitle("Importacion desde la nube");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                user_ws[0]=SendData(user,password);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
        return user_ws[0];
    }

    public boolean SendData(final String user , final String password){

        final boolean[] user_ws = {false};
        Cursor cursor=appController.exportTablas("user");
        JSONArray usuarios = Cursor2JSON(cursor);
        Log.e("JSON USUARIOS",""+usuarios);
        JSONArray usuarioConf=ConfigOnUser(usuarios);
        Log.e("JSON Finale",""+usuarioConf);

        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit.Builder  retrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://192.168.15.2/Roberto/Mobility-app/api/load_admin/UGVkYXpvYWxhbWJyZWNvbXBsZXRhbGF0YWJsYQ==/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);
        Retrofit retrofit = retrofitBuilder.build();
        SendInfo sendInfo = retrofit.create(SendInfo.class);

        Call<ResponseBody> callableResponse=sendInfo.savePost(user,password,usuarioConf);
        callableResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                appController.clearDatabases("user_config_launcher");
                appController.clearDatabases("user");
                user_ws[0] = ConectedCloud(user,password,getContext());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error de conexion","Algun dato es incorrecto");
            }
        });
    return  user_ws[0];
    }
    
    public JSONArray Cursor2JSON(Cursor cursor){
        JSONArray usuarios = new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),cursor.getString(i));
                    } catch (Exception e) {
                        Log.d("Error de conversion", e.getMessage());
                    }
                }
            }
            usuarios.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return usuarios;
    }

    public JSONArray ConfigOnUser(JSONArray usuario){
        for (int i=0;i<usuario.length();i++){
            try {
                Log.e("USUARIO-Mial",""+usuario.getJSONObject(i).getInt("id_user"));
                Cursor cursor = appController.searchAppConfigByID(usuario.getJSONObject(i).getInt("id_user"));
                JSONArray config = Cursor2JSON(cursor);
                usuario.getJSONObject(i).put("conf",config);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return usuario;
    }
    


    /*
     *
     *   Metodos de SHARE PRFERENS
     *
     */

    //Metodo para guardar datos de preferencias
    private void guardarDatos(boolean isFirst,String user,String password) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(CLOUD_PREFS, isFirst);
        editor.putString(CLOUD_USER,user);
        editor.putString(CLOUD_PASSWORD,password);
        editor.apply();
    }

    //Metodo para cargar datos de preferencias
    public boolean cargarDatos() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return isFirts = preferences.getBoolean(CLOUD_PREFS, true);
    }

    //Metodo para obtener el usuario y contraseña
    public String[] cargarUserPassword(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String datos[] = new String[2];
        datos[0]=preferences.getString(CLOUD_USER,"");
        datos[1]=preferences.getString(CLOUD_PASSWORD,"");
        return datos;
    }
}
