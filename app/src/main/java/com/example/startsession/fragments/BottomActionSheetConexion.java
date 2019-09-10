package com.example.startsession.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.startsession.R;

public class BottomActionSheetConexion extends BottomSheetDialogFragment {

    public static BottomActionSheetConexion newInstance() {
        return new BottomActionSheetConexion();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom, container, false);
        return view;
    }
}
