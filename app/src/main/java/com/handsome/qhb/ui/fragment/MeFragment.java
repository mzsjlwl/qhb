package com.handsome.qhb.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.handsome.qhb.application.ApplicationManager;
import com.handsome.qhb.ui.activity.LoginActivity;

import org.jivesoftware.smack.XMPPConnection;

import tab.com.handsome.handsome.R;

public class MeFragment extends Fragment implements View.OnClickListener {

    private Button bt_loginOut;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        bt_loginOut = (Button) view.findViewById(R.id.id_bt_loginOut);
        bt_loginOut.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_bt_loginOut:
                loginOut(ApplicationManager.getXMPPConnection(this.getActivity()));
                Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                startActivity(intent);
                this.getActivity().finish();
                break;
        }
    }

    /**
     * 退出登录
     * @param connection
     * @return
     */
    private boolean loginOut(XMPPConnection connection) {
        try{
            connection.disconnect();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
