package com.handsome.qhb.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.adapter.ProductAdapter;
import com.handsome.qhb.adapter.SliderAdapter;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Slider;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.listener.OnRefreshListener;
import com.handsome.qhb.ui.activity.GwcActivity;
import com.handsome.qhb.ui.activity.LoginActivity;
import com.handsome.qhb.utils.ImageUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.RequestQueueController;
import com.handsome.qhb.utils.UserInfo;
import com.handsome.qhb.widget.RefreshListView;

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
    //实现轮播的viewPager
    private ViewPager viewPager;
    // 滑动的图片集合
    private List<ImageView> imageViews;
    //滑动图片
    private List<Slider> sliderLists;
    //商品listView
    private RefreshListView rListView;
    //商品列表
    private List<Product> productLists;
    //购物车
    private ImageButton shopCar;
    // 当前图片的索引号
    private int currentItem = 0;
    // 轮播图片的那些点
    private List<View> dots;
    private View dot0;
    private View dot1;
    private View dot2;
    private View dot3;
    //轮播时间
    private ScheduledExecutorService scheduledExecutorService;

    //商品分页Json
    private JSONObject pageJson;
    //商品页数
    private int page;
    //商品下一页
    private String nextpage;

    //Gson解析
    private Gson gson = new Gson();

    //加载轮播图片消息
    private Message msg1 = new Message();
    //加载商品信息消息
    private Message msg2 = new Message();

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
                initSliderImage();
                initSliderdots();
            } else if (msg.what == 0x125) {
                LogUtils.d("0x125", "------>");
                initProductList();
                rListView.hideHeaderView();
            } else if (msg.what == 0x126){
                LogUtils.d("0x126","------->");
                initProductList();
                rListView.hideFooterView();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //异步加载轮播图片
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Config.BASE_URL + "Slider/getJson", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        msg1.what = 0x124;
                        msg1.obj = 1;
                        try {
                            sliderLists = gson.fromJson(response.getString("slider"), new TypeToken<List<Slider>>() {}.getType());
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
        //异步加载商品图片
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Config.BASE_URL+"Product/getJson",null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        msg2.what = 0x125;
                        msg2.obj = 1;
                        try {
                            productLists = new ArrayList<Product>();
                            productLists = gson.fromJson(response.getString("products"), new TypeToken<List<Product>>() {}.getType());
                            pageJson = new JSONObject(response.getString("page"));
                            nextpage = pageJson.getString("next");
                            page =Integer.valueOf(pageJson.getString("nums"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        handler.handleMessage(msg2);
                        
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(),error);
            }

        });
        mQueue.add(jsonObjectRequest1);
        mQueue.add(jsonObjectRequest2);
    }

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

        shopCar = (ImageButton) view.findViewById(R.id.ib_shopcar);

        shopCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserInfo.getInstance() == null) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getActivity(), GwcActivity.class);
                    startActivity(i);
                }
            }
        });
        //ListView
        rListView = (RefreshListView)view.findViewById(R.id.refreshlistview);

        //当前Fragment不可见后,重新加载轮播图片和商品项
        if (msg1.obj!=null) {
            if (msg1.obj.toString() == "0") {
                handler.handleMessage(msg1);
                handler.handleMessage(msg2);
            } else {
                msg1.obj = 0;
            }
        }
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
        final ProductAdapter productAdapter = new ProductAdapter(getActivity(), productLists, R.layout.product_list_items,RequestQueueController.getInstance());
        rListView.setAdapter(productAdapter);
        rListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.BASE_URL+"Product/getJson",null,
                        new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response) {
                                Message msg = new Message();
                                msg.what = 0x125;
                                try {
                                    msg.obj = response.getString("products");
                                    pageJson = new JSONObject(response.getString("page"));
                                    LogUtils.e("===",pageJson.getString("next"));
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
            public void onLoadingMore() {
                if(page<=1){
                    rListView.hideFooterView();
                    return;
                }else{
                    page--;
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(nextpage,null,
                        new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response) {
                                Message msg = new Message();
                                msg.what = 0x126;
                                try {
                                    if(response.getString("products")==""){
                                        return;
                                    }
                                    List<Product> nextProducts = new ArrayList<Product>();
                                    nextProducts = gson.fromJson(response.getString("products"), new TypeToken<List<Product>>() {}.getType());
                                    for(Product product:nextProducts){
                                        productLists.add(product);
                                    }
                                    pageJson =new JSONObject(response.getString("page"));
                                    nextpage = pageJson.getString("next");
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
