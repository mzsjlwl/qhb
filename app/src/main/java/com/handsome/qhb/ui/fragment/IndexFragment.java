package com.handsome.qhb.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;


import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.handsome.qhb.adapter.ProductAdapter;
import com.handsome.qhb.adapter.SliderAdapter;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Slider;
import com.handsome.qhb.utils.ImageUtils;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tab.com.handsome.handsome.R;


/**
 * Created by zhang on 2016/2/20.
 */
public class IndexFragment extends Fragment {

    private ViewPager viewPager;
    // 滑动的图片集合
    private List<ImageView> imageViews;
    //滑动图片
    private List<Slider> sliderLists;
    //商品listView
    private ListView lv_products;
    private GridView gv_products;
    //商品列表
    private List<Product> productLists;
    // 当前图片的索引号
    private int currentItem = 0;
    // 图片标题正文的那些点
    private List<View> dots;
    private View dot0;
    private View dot1;
    private View dot2;
    private View dot3;

    private ScheduledExecutorService scheduledExecutorService;

    private RequestQueue mQueue;

    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
           if(msg.what==0x123){
               Log.d("0x123", "----->");
               viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
           }
           else if(msg.what==0x124){
               Log.d("0x124", "----->");
                   sliderLists = JSON.parseArray(msg.obj.toString(), Slider.class);
               initSliderImage();
               initSliderdots();
           }else if(msg.what==0x125){
               Log.d("0x125", "------>");
               productLists = JSON.parseArray(msg.obj.toString(),Product.class);
               initProductList();
           }
        };
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_index,container,false);
        dots = new ArrayList<View>();
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        dot0 =view.findViewById(R.id.v_dot0);
        dot1 = view.findViewById(R.id.v_dot1);
        dot2 = view.findViewById(R.id.v_dot2);
        dot3 = view.findViewById(R.id.v_dot3);

        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        //ListView
        // lv_products = (ListView) view.findViewById(R.id.lv_products);
        //GridView
        gv_products = (GridView) view.findViewById(R.id.lv_products);

        mQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://192.168.0.110:8033/QHB/slider.json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message msg = new Message();
                        msg.what=0x124;
                        try {
                            msg.obj=response.getString("slider");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        handler.handleMessage(msg);

                        Message msg1 = new Message();
                        msg1.what = 0x125;

                        try {
                            msg1.obj = response.getString("products");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        handler.handleMessage(msg1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(jsonObjectRequest);
//        String sliderString= null;
//        String productsString = null;
//        try {
//            sliderString = MyApplication.getIndexJSONObject().getString("slider").toString();
//            productsString = MyApplication.getIndexJSONObject().getString("products").toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        sliderLists =JSON.parseArray(sliderString,Slider.class);
//        productLists = JSON.parseArray(productsString,Product.class);
//        initSliderImage();
//        initSliderdots();
//        initProductList();
        return view;
    }

    public void initSliderImage(){
        imageViews = new ArrayList<ImageView>();
        for(Slider s:sliderLists) {
            ImageView imageView = new ImageView(getActivity());
            //加载图片
            imageView = ImageUtils.imageLoader(mQueue, s.getImage(), imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }
        // 设置填充ViewPager页面的适配器
        viewPager.setAdapter(new SliderAdapter(imageViews));
        //启动滑动
        onStartSlider();
        // 设置一个监听器，当ViewPager中的页面改变时调用
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }
    public void initSliderdots(){
        for(int i = 0 ;i<dots.size();i++){

            dots.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void initProductList(){
        ProductAdapter productAdapter = new ProductAdapter(getActivity(),productLists,R.layout.product_list_items1,mQueue);
        //lv_products.setAdapter(productAdapter);
        gv_products.setAdapter(productAdapter);
    }

    public void onStartSlider() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
    }

    @Override
    public void onStop() {
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    /**
     * 换行切换任务
     *
     */
    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.sendEmptyMessage(0x123); // 通过Handler切换图片
            }
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }
        public void onPageScrollStateChanged(int arg0) {

        }
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }
}
