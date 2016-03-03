package com.handsome.qhb.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tab.com.handsome.handsome.R;

public class CycleFragment extends Fragment {

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //填充布局
        view = inflater.inflate(R.layout.fragment_cycle, container, false);
        return view;
    }
}