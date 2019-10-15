package com.example.startsession.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import com.example.startsession.interfaces.UserService;

import org.json.JSONObject;

import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
                @SuppressLint("StaticFieldLeak") AsyncTask<String,String,String> demoDownload = new AsyncTask<String, String, String>() {
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
                demoDownload.execute();

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

    private UserModel validationLogin(String user, String password){
        final boolean[] user_ws = {false};

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Consumo de WS
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.15.2/Roberto/Mobility-app/api/login_admin/TGVvbmFyZG9kaXNlclBpZXJvZGFWaW5jaQ==/")
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
                Toast.makeText(getContext(),responseServiceModel.getMessage(),Toast.LENGTH_LONG).show();
                if(responseServiceModel.isStatus()) {
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
                                    //AppModel AppWS = (AppModel) conf.get(String.valueOf(j));
                                    String app_name=getNameApp(conf.getString("app_flag_system").toLowerCase());
                                    String icon=getIcon(conf.getString("app_flag_system").toLowerCase());
                                    AppModel newApp = new AppModel((int) id_user,app_name,conf.get("app_flag_system").toString().toLowerCase(),icon,conf.getInt("active"),conf.getInt("status_ws"));
                                    long id_conf = appController.importConfigAppsWS(newApp);
                                    Log.e("ID Configuracion",""+id_conf);
                                }
                                Toast.makeText(getContext(),"Importacion completa",Toast.LENGTH_LONG).show();
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

        userController = new UserController(getContext());
        appController = new AppController(getContext());
        UserModel loginUser = new UserModel(user,password);
        UserModel id_user = userController.login(loginUser);

        // Root Access
        if((user.equalsIgnoreCase("root") && password.equalsIgnoreCase("Mobility2639")) || user_ws[0] ){
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
}
