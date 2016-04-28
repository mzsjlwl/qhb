package com.handsome.qhb.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.adapter.ProductAdapter;
import com.handsome.qhb.adapter.SliderAdapter;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Slider;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.UserDAO;
import com.handsome.qhb.listener.MyListener;
import com.handsome.qhb.listener.OnRefreshListener;
import com.handsome.qhb.ui.activity.GwcActivity;
import com.handsome.qhb.ui.activity.LoginActivity;
import com.handsome.qhb.utils.HttpUtils;
import com.handsome.qhb.utils.ImageUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.NetworkImageUtils;
import com.handsome.qhb.utils.UserInfo;
import com.handsome.qhb.widget.RefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/7.
 */
public class ShopFragment extends Fragment implements MyListener{
    //实现轮播的viewPager
    private ViewPager viewPager;
    // 滑动的图片集合
    private List<ImageView> imageViews;
    //滑动图片
    private List<Slider> sliderLists = new ArrayList<Slider>();
    //商品listView
    private RefreshListView rListView ;
    //商品列表
    private List<Product> productLists = new ArrayList<Product>();
    //商品Adapter
    private ProductAdapter productAdapter;
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
    public ScheduledExecutorService scheduledExecutorService;
    //商品分页Json
    private JSONObject pageJson;
    //商品页数
    private int page;
    //当前页
    private int curpage;
    //商品下一页
    private String nextpage;
    //加载轮播图片消息
    private Message msg1 = new Message();
    //加载商品信息消息
    private Message msg2 = new Message();
    private Intent i ;
    //购物车列表
    private List<Product> shopCarList = new ArrayList<Product>();
    private Map<String, String> map;

    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == Config.SLIDER_PICTURE) {
                LogUtils.d("0x123", "----->");
                viewPager.setCurrentItem(currentItem,true);// 切换当前显示的图片
            } else if (msg.what == Config.INIT_SLIDER_PICTURE) {
                LogUtils.d("0x124", "----->");
                initSliderImage();
                initSliderdots();
            } else if (msg.what == Config.INIT_PRODUCT) {
                LogUtils.d("0x125", "------>");
                initProductList();
            }else if(msg.what==Config.REFERSH_PRODUCT){
                LogUtils.d("0x126","------>");
                initProductList();
                rListView.hideHeaderView();
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        map = new HashMap<String, String>();
        map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
        map.put("token", String.valueOf(UserInfo.getInstance().getToken()));



        //异步加载轮播图片
        HttpUtils.request(getActivity(), Config.GETSLIDER_URL, this, map,Config.GETSLIDER_TAG);
        //异步加载商品图片
        HttpUtils.request(getActivity(), Config.GETPRODUCT_URL, this, map, Config.GETPRODUCT_TAG);
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
                    i = new Intent(getActivity(), LoginActivity.class);
                    //启动登录activity
                    startActivity(i);
                    //关闭mainactivity
                    getActivity().finish();
                } else {
                    List<Product> shopCarList = new ArrayList<Product>();
                    //防止商品为加载就点击，出现空指针异常
                    if(productLists==null){
                        return;
                    }
                    for (Product p : productLists) {
                        if (p.getNum() > 0) {
                            shopCarList.add(p);
                        }
                    }
                    i = new Intent(getActivity(), GwcActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("shopCarList", (Serializable) shopCarList);
                    i.putExtras(b);
                    getActivity().setIntent(i);
                    startActivity(i);
                }
            }
        });
        //ListView
        rListView = (RefreshListView)view.findViewById(R.id.refreshlistview);
        //获取sharePreference中的product与slider
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        if(productLists.size()==0) {
            if (!sharedPreferences.getString("product", "").equals("")) {
                LogUtils.e("sd=========product============", "=======>");
                productLists = MyApplication.getGson().fromJson(sharedPreferences.getString("product", ""), new TypeToken<List<Product>>() {
                }.getType());

                initProductList();
            }
        }
        if(sliderLists.size()==0){
            if(!sharedPreferences.getString("slider","").equals("")){
                LogUtils.e("sd=========slider============","=======>");
                sliderLists = MyApplication.getGson().fromJson(sharedPreferences.getString("slider",""),new TypeToken<List<Slider>>(){}.getType());
                initSliderImage();
                initSliderdots();
            }
        }


        //当前Fragment不可见后,重新加载轮播图片和商品项
        if (msg1.obj!=null) {
            if (msg1.obj.toString().equals("0")) {
                handler.handleMessage(msg1);
                handler.handleMessage(msg2);
            } else {
                msg1.obj = 0;
            }
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(productLists!=null&&productLists.size()!=0){
            String TAG = getActivity().getIntent().getStringExtra("TAG");
            if(TAG!=null&&TAG.equals("ClearGWC")){
                clearShopCar();
                Message msg = new Message();
                msg.what = Config.INIT_PRODUCT;
                handler.handleMessage(msg);
            }
        }
    }

    public void initSliderImage() {
        imageViews = new ArrayList<ImageView>();
        for (Slider s : sliderLists) {
            ImageView imageView = new ImageView(getActivity());
            //加载图片
            imageView =  ImageUtils.imageLoader(MyApplication.getmQueue(), s.getImage(), imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }
        // 设置填充ViewPager页面的适配器
        viewPager.setAdapter(new SliderAdapter(imageViews));

        // 设置一个监听器，当ViewPager中的页面改变时调用
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    public void initSliderdots() {
        for (int i = 0; i < dots.size(); i++) {
            dots.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void initProductList() {
        for(Product p :productLists){
            LogUtils.e("product=======>",p.toString());
        }
        productAdapter = new ProductAdapter(getActivity(), productLists, R.layout.product_list_items,MyApplication.getmQueue());

        rListView.setAdapter(productAdapter);
        rListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                HttpUtils.request(getActivity(), Config.GETPRODUCT_URL, ShopFragment.this, map, Config.REFRESHPRODUCT_TAG);
            }

            @Override
            public void onLoadingMore() {
                if (page==curpage) {
                    rListView.hideFooterView();
                    return;
                }
                HttpUtils.request(getActivity(), nextpage.substring(Config.BASE_URL.length()), ShopFragment.this, map, Config.LOADMOREPRODUCT_TAG);
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
        MyApplication.getmQueue().cancelAll(Config.GETPRODUCT_TAG);
        MyApplication.getmQueue().cancelAll(Config.GETSLIDER_TAG);
        super.onStop();
    }

    @Override
    public void dataController(String response, int tag) {
        try {

            switch (tag) {
                case Config.REFRESHPRODUCT_TAG:
                    JSONObject jsonObjectdata1 = new JSONObject(response);
                    Message msg = new Message();
                    msg.what = Config.REFERSH_PRODUCT;
                    productLists = MyApplication.getGson().fromJson(jsonObjectdata1.getString("products"),new TypeToken<List<Product>>(){}.getType());
                    pageJson = new JSONObject(jsonObjectdata1.getString("page"));
                    page = Integer.valueOf(pageJson.getString("nums"));
                    nextpage = pageJson.getString("next");
                    curpage =Integer.valueOf(pageJson.getString("cur"));
                    handler.handleMessage(msg);
                    break;
                case Config.GETSLIDER_TAG:
                    msg1.what = Config.INIT_SLIDER_PICTURE;
                    msg1.obj = 1;
                    sliderLists.clear();
                    sliderLists = MyApplication.getGson().fromJson(response, new TypeToken<List<Slider>>() {
                    }.getType());
                    handler.handleMessage(msg1);
                    break;
                case Config.GETPRODUCT_TAG:
                    JSONObject jsonObjectdata2 = new JSONObject(response);
                    msg2.what = Config.INIT_PRODUCT;
                    msg2.obj = 1;
                    productLists.clear();
                    //服务器端获取的product
                    productLists = MyApplication.getGson().fromJson(jsonObjectdata2.getString("products"), new TypeToken<List<Product>>() {
                    }.getType());
                    addShopCar();

                    //存储到activity中
                    getActivity().getIntent().putExtra("products",MyApplication.getGson().toJson(productLists));
                    pageJson = new JSONObject(jsonObjectdata2.getString("page"));
                    nextpage = pageJson.getString("next");
                    curpage =Integer.valueOf(pageJson.getString("cur"));
                    //存储到activity中
                    getActivity().getIntent().putExtra("next", pageJson.getString("next"));
                    page =Integer.valueOf(pageJson.getString("nums"));
                    getActivity().getIntent().putExtra("page", pageJson.getString("nums"));
                    handler.handleMessage(msg2);
                case Config.LOADMOREPRODUCT_TAG:
                    JSONObject jsonObjectdata3 = new JSONObject(response);

                    if(jsonObjectdata3.getString("products")==""){
                        rListView.hideFooterView();
                        return;
                    }
                    List<Product> nextProducts = new ArrayList<Product>();
                    nextProducts = MyApplication.getGson().fromJson(jsonObjectdata3.getString("products"), new TypeToken<List<Product>>() {
                    }.getType());
                    for(Product product:nextProducts){
                        productLists.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                    rListView.setSelection(productLists.size() - nextProducts.size());

                    pageJson =new JSONObject(jsonObjectdata3.getString("page"));
                    nextpage = pageJson.getString("next");
                    curpage = Integer.valueOf(pageJson.getString("cur"));
                    page = Integer.valueOf(pageJson.getString("nums"));
                    rListView.hideFooterView();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 换行切换任务
     */
    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.sendEmptyMessage(Config.SLIDER_PICTURE); // 通过Handler切换图片
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



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        List<Product> shopCarList = new ArrayList<Product>();
        if(productLists!=null){
            for(Product p:productLists){
                if(p.getNum()>0){
                    shopCarList.add(p);
                }
            }
            String product = MyApplication.getGson().toJson(shopCarList);
            if(UserInfo.getInstance()!=null) {
                if(UserDAO.find(MyApplication.getSQLiteDatabase(),UserInfo.getInstance().getUid())!=null){
                    UserDAO.update(MyApplication.getSQLiteDatabase(),UserInfo.getInstance().getUid(),product);
                }else{
                    UserDAO.insert(MyApplication.getSQLiteDatabase(),UserInfo.getInstance().getUid(), product);
                }

            }
        }
        if(scheduledExecutorService!=null){
            // 当Activity不可见的时候停止切换
            scheduledExecutorService.shutdown();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        SharedPreferences.Editor editor =getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE).edit();
        editor.clear();
        editor.putString("user", MyApplication.getGson().toJson(UserInfo.getInstance()));
        editor.putString("slider",MyApplication.getGson().toJson(sliderLists));
        editor.putString("product", MyApplication.getGson().toJson(productLists));
        editor.commit();
        LogUtils.e("shopFragment============>","destroy");
    }

    //获取本地购物车信息后填充商品数量
    public void addShopCar(){
        //本地获取的购物车
        if(UserInfo.getInstance()!=null){
            shopCarList = MyApplication.getGson().fromJson(UserDAO.find(MyApplication.getSQLiteDatabase(), UserInfo.getInstance().getUid()), new TypeToken<List<Product>>() {
            }.getType());
            if(shopCarList!=null){
                for(int i= 0;i<shopCarList.size();i++){
                    for(int j = 0;j<productLists.size();j++){
                        if(shopCarList.get(i).getPid()==productLists.get(j).getPid()){
                            productLists.get(j).setNum(shopCarList.get(i).getNum());
                        }
                    }
                }
            }
        }
    }
    public void clearShopCar(){
        if(productLists!=null&&productLists.size()!=0){
            for(int i = 0;i<productLists.size();i++){
                productLists.get(i).setNum(0);
            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.e("hiddenchanger=============================","======");
        if(scheduledExecutorService!=null){
            scheduledExecutorService.shutdown();
        }
        if(hidden){
            LogUtils.e("hidden","=============================");
        }else{
            onStartSlider();
        }
    }
}
