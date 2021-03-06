package com.example.startsession.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.os.WorkSource;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.startsession.AdminHomeCard;
import com.example.startsession.BlockService;
import com.example.startsession.MainActivity;
import com.example.startsession.R;
import com.example.startsession.db.controller.AppController;
import com.example.startsession.db.controller.UserController;
import com.github.clans.fab.FloatingActionButton;

import java.io.BufferedReader;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserController userController;
    private UserController lazamientosController;
    private AppController appController;

    private OnFragmentInteractionListener mListener;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHomeFragment newInstance(String param1, String param2) {
        AdminHomeFragment fragment = new AdminHomeFragment();
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
        final View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        TextView num_users = (TextView) view.findViewById(R.id.num_users);
        userController = new UserController(getContext());
        int num_user =  userController.users();
        num_users.setText("" + num_user);

        TextView num_lanzamientos = (TextView) view.findViewById(R.id.num_launcher);
        lazamientosController = new UserController(getContext());
        int num_lanzamiento = lazamientosController.lanzamientos();
        num_lanzamientos.setText("" + num_lanzamiento);

        TextView num_apps = (TextView) view.findViewById(R.id.num_app);
        appController = new AppController(getContext());
        int num_app = appController.num_app();
        num_apps.setText("" + num_app);

        view.findViewById(R.id.FrameUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminHomeCard cardU = new AdminHomeCard();
                cardU.Usuarios(view);
             }
        });
        view.findViewById(R.id.lanzamiento).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminHomeCard cardL = new AdminHomeCard();
                cardL.Lanzamiento(view);
            }
        });

        view.findViewById(R.id.apps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminHomeCard cardA = new AdminHomeCard();
                cardA.Apps(view);
            }
        });

        //Exit Mode Admin
        FloatingActionButton fab = view.findViewById(R.id.exit_mode_admi);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        //Exit APP
        FloatingActionButton exitFabApp = view.findViewById(R.id.exit_app);
        exitFabApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Settings.Secure.putString(getContext().getContentResolver(),
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "com.example.startsession.fragments/BlockService");
                Settings.Secure.putString(getContext().getContentResolver(),
                        Settings.Secure.ACCESSIBILITY_ENABLED, "0");*/
                Intent intentService = new Intent(getContext(), BlockService.class);
                getActivity().stopService(intentService);
                Intent homeIntent = new Intent(Intent.ACTION_MAIN, null);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(homeIntent);

            }
        });
        return view;
    }

    private int numUser() {
        int num_user = 0;
        return  num_user;
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

    @Override
    public void onDestroy() {
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }
}
