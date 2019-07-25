package com.example.startsession.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startsession.AddUserActivity;
import com.example.startsession.AdminActivity;
import com.example.startsession.EditUserActivity;
import com.example.startsession.MainActivity;
import com.example.startsession.R;
import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.UserModel;
import com.example.startsession.ui.admin.DialogAddUser;
import com.example.startsession.ui.admin.UserAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminConfigUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminConfigUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminConfigUserFragment extends Fragment implements DialogAddUser.DialogAddUserListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    /**
    * Mobility
    */
    private List<UserModel> listUser;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private UserController userController;
    SwipeRefreshLayout pullToRefresh;




    public AdminConfigUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminConfigUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminConfigUserFragment newInstance(String param1, String param2) {
        AdminConfigUserFragment fragment = new AdminConfigUserFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_config_user, container, false);

        userController = new UserController(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewUsers);

        listUser = new ArrayList<>();
        userAdapter = new UserAdapter(listUser);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userAdapter);

        reloadListUser();

        FloatingActionButton fab = view.findViewById(R.id.fab_add_user);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddUserActivity.class);
                startActivity(intent);
            }
        });

        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadListUser();
                pullToRefresh.setRefreshing(false);
            }
        });


        final GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                try {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                    Log.e("Event click", String.valueOf(mGestureDetector.onTouchEvent(motionEvent)));
                    if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                        int position = recyclerView.getChildAdapterPosition(child);
                        UserModel userSelected = listUser.get(position);

                        Toast.makeText(getContext(),"user ID: "+ userSelected.getId_user() + "Nombre: " + userSelected.getName() ,Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), EditUserActivity.class);
                        intent.putExtra("id_user","" + userSelected.getId_user());
                        intent.putExtra("user",userSelected.getUser());
                        intent.putExtra("mail",userSelected.getMail());
                        intent.putExtra("password",userSelected.getPassword());
                        intent.putExtra("name",userSelected.getName());
                        intent.putExtra("last_name",userSelected.getLast_name());
                        intent.putExtra("mother_last_name",userSelected.getMother_last_name());

                        startActivity(intent);

                        return true;
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });


        return view ;
    }

    private void reloadListUser() {
        if (userAdapter == null) return;
        listUser = userController.getUsers();
        userAdapter.setListUser(listUser);
        userAdapter.notifyDataSetChanged();
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

    public void openAddUserDialog(){
        DialogAddUser dialogAddUser = new DialogAddUser();
        dialogAddUser.show(getFragmentManager() ,"Nuevo Usuario");
    }

    @Override
    public void applyTexts(String mail, String user, String password) {
        Toast.makeText(getContext(), "Guardando ... Mail :  " + mail + " Usuario: " + user + " Contraseña: " + password,Toast.LENGTH_LONG).show();
    }



}
