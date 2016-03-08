package com.handsome.qhb.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.handsome.qhb.adapter.RoomAdapter;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.RequestQueueController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/7.
 */
public class HallFragment extends Fragment {

    private ListView lv_room;
    private static List<Room> list = new ArrayList<Room>();
    private RoomAdapter roomAdapter;
    private RequestQueue requestQueue = RequestQueueController.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hall,container,false);
        lv_room = (ListView) view.findViewById(R.id.id_lv_room);

        StringRequest request = new StringRequest(Request.Method.POST, Config.FINDROOM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONArray array = new JSONArray(s);
                    for (int i=0;i<array.length();i++){
                        Room room = new Gson().fromJson(array.get(i).toString(),Room.class);
                        list.add(room);
                    }
                    roomAdapter = new RoomAdapter(HallFragment.this.getActivity(),list,R.layout.hall_list_items, requestQueue);
                    lv_room.setAdapter(roomAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(request);

        return view;
    }
}
