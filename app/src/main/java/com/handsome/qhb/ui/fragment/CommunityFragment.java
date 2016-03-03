package com.handsome.qhb.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tab.com.handsome.handsome.R;


/**
 * Created by zhang on 2016/2/20.
 */
public class CommunityFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_community,container,false);
        return view;
    }
}
