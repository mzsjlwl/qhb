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
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handsome.qhb.adapter.ProductAdapter;
import com.handsome.qhb.adapter.SliderAdapter;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Slider;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.ImageUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.RequestQueueController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/7.
 */
public class ShopFragment extends Fragment {
    private ViewPager viewPager;
    // 滑动的图片集合
    private List<ImageView> imageViews;
    //滑动图片
    private List<Slider> sliderLists;
    //商品listView
    private PullToRefreshListView mPullRefreshListView;
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
    //轮播时间
    private ScheduledExecutorService scheduledExecutorService;

    //商品分页Json
    private JSONObject pageJson;

    //RequestQueue对象
    private RequestQueue mQueue = RequestQueueController.getInstance();

    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x123) {
                LogUtils.d("0x123", "----->");
                viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
            } else if (msg.what == 0x124) {
                LogUtils.d("0x124", "----->");
                sliderLists = JSON.parseArray(msg.obj.toString(), Slider.class);
                initSliderImage();
                initSliderdots();
            } else if (msg.what == 0x125) {
                LogUtils.d("0x125", "------>");
                productLists = new ArrayList<Product>();
                productLists = JSON.parseArray(msg.obj.toString(), Product.class);
                initProductList();
            } else if (msg.what == 0x126){
                LogUtils.d("0x126","------->");
                List<Product> nextProducts = new ArrayList<Product>();
                nextProducts = JSON.parseArray(msg.obj.toString(),Product.class);
                for(Product product:nextProducts){
                    productLists.add(product);
                }
                initProductList();
            }
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        dots = new ArrayList<View>();
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        dot0 = view.findViewById(R.id.v_dot0);
        dot1 = view.findViewById(R.id.v_dot1);
        dot2 = view.findViewById(R.id.v_dot2);
        dot3 = view.findViewById(R.id.v_dot3);

        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);

        //ListView
        mPullRefreshListView = (PullToRefreshListView ) view.findViewById(R.id.pull_refresh_list);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.BASE_URL + "Slider/getJson", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message msg = new Message();
                        msg.what = 0x124;
                        msg.obj = response;
                        handler.handleMessage(msg);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Config.BASE_URL+"Product/getJson",null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Message msg = new Message();
                        msg.what = 0x125;
                        try {
                            msg.obj = response.getString("product");
                            pageJson = new JSONObject(response.getString("page"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        handler.handleMessage(msg);
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(),error);
            }

        });

        mQueue.add(jsonObjectRequest);
        mQueue.add(jsonObjectRequest1);

        return view;
    }

    public void initSliderImage() {
        imageViews = new ArrayList<ImageView>();
        for (Slider s : sliderLists) {
            ImageView imageView = new ImageView(getActivity());
            //加载图片
            imageView = ImageUtils.imageLoader(RequestQueueController.getInstance(), s.getImage(), imageView);
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

    public void initSliderdots() {
        for (int i = 0; i < dots.size(); i++) {
            dots.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void initProductList() {
        ProductAdapter productAdapter = new ProductAdapter(getActivity(), productLists, R.layout.product_list_items,RequestQueueController.getInstance());
        mPullRefreshListView.setAdapter(productAdapter);
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        LogUtils.e("TAG", "onPullDownToRefresh");
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.BASE_URL+"Product/getJson",null,
                                new Response.Listener<JSONObject>(){
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Message msg = new Message();
                                        msg.what = 0x125;
                                        try {
                                            msg.obj = response.getString("product");
                                            pageJson = new JSONObject(response.getString("page"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        handler.handleMessage(msg);
                                    }
                                },new Response.ErrorListener(){

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG",error.getMessage(),error);
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        LogUtils.e("TAG", "onPullUpToRefresh");
                        //这里写上拉加载更多的任务
                        String url = "";
                        try {
                            url = pageJson.getString("next");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,null,
                                new Response.Listener<JSONObject>(){
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Message msg = new Message();
                                        msg.what = 0x125;
                                        try {
                                            msg.obj = response.getString("product");
                                            //待完善
                                            pageJson =new JSONObject(response.getString("page"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        handler.handleMessage(msg);
                                    }
                                },new Response.ErrorListener(){

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG",error.getMessage(),error);
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                    }
                });
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
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;


        public void onPageSelected(int position) {
            currentItem = position;
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {}

        public void onPageScrolled(int arg0, float arg1, int arg2) {}
    }
}
