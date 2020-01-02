package com.example.startsession.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startsession.AdminActivity;
import com.example.startsession.ConfigAppActivity;
import com.example.startsession.LauncherActivity;
import com.example.startsession.R;
import com.example.startsession.db.controller.UserController;
import com.example.startsession.db.model.UserModel;
import com.example.startsession.interfaces.ClickListener;
import com.example.startsession.ui.admin.RecyclerViewItemClickListener;
import com.example.startsession.ui.admin.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminConfigAppFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AdminConfigAppFragment.OnFragmentInteractionListener mListener;

    /**
     * Mobility
     */
    private List<UserModel> listUser;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private UserController userController;
    SwipeRefreshLayout pullToRefresh;




    public AdminConfigAppFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminConfigAppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminConfigAppFragment newInstance(String param1, String param2) {
        AdminConfigAppFragment fragment = new AdminConfigAppFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_config_app, container, false);
        userController = new UserController(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewUsers);

        listUser = new ArrayList<>();
        userAdapter = new UserAdapter(listUser);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userAdapter);



        EditText searchs=(EditText)view.findViewById(R.id.input_user);
        searchs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("RESP",""+s);
                reloadListUser(s.toString());
            }
        });

        reloadListUser("");

        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadListUser("");
                pullToRefresh.setRefreshing(false);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserModel userSelected = listUser.get(position);

                Intent intent = new Intent(getActivity(), ConfigAppActivity.class);
                intent.putExtra("id_user",""+userSelected.getId_user());
                intent.putExtra("user",userSelected.getUser());
                intent.putExtra("mail",userSelected.getMail());
                intent.putExtra("password",userSelected.getPassword());
                intent.putExtra("name",userSelected.getName());
                intent.putExtra("last_name",userSelected.getLast_name());
                intent.putExtra("mother_last_name",userSelected.getMother_last_name());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                UserModel userSelected = listUser.get(position);
                Intent intent = new Intent(getContext(), LauncherActivity.class);
                intent.putExtra("id_user","" + userSelected.getId_user());
                startActivity(intent);
            }
        }));


        return view ;
    }

    private void reloadListUser(String seachrUser) {
        Log.e("RESP",seachrUser);
        if (userAdapter == null) return;
        listUser = userController.getUsersAdmin(seachrUser);
        userAdapter.setListUser(listUser);
        userAdapter.notifyDataSetChanged();
    }
/*
    private void setListUser (){
        listUser = userController.getUsersAdmin("");
        userAdapter.setListUser(listUser);
        userAdapter.notifyDataSetChanged();
    }
*/
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AdminConfigAppFragment.OnFragmentInteractionListener) {
            mListener = (AdminConfigAppFragment.OnFragmentInteractionListener) context;
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
}
