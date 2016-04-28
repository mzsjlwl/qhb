package com.handsome.qhb.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.handsome.qhb.adapter.SliderAdapter;

import java.util.ArrayList;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/4/26.
 */
public class IndexActivity extends BaseActivity {

    private ViewPager viewPager;
    private List<ImageView> imageViews = new ArrayList<ImageView>();
    // 当前图片的索引号
    private int currentItem = 0;
    // 轮播图片的那些点
    private List<View> dots = new ArrayList<View>();
    private View dot0;
    private View dot1;
    private View dot2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        dot0 = findViewById(R.id.v_dot0);
        dot1 = findViewById(R.id.v_dot1);
        dot2 = findViewById(R.id.v_dot2);
        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);

        ImageView imageView1= new ImageView(this);
        imageView1.setBackgroundResource(R.mipmap.index2);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView imageView2= new ImageView(this);
        imageView2.setBackgroundResource(R.mipmap.index1);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView imageView3= new ImageView(this);
        imageView3.setBackgroundResource(R.mipmap.index3);
        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);

        // 设置填充ViewPager页面的适配器
        viewPager.setAdapter(new SliderAdapter(imageViews));

        // 设置一个监听器，当ViewPager中的页面改变时调用
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    private void setGuided(){
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.clear();
        editor.putString("active", "1");
        editor.commit();
    }
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        public void onPageSelected(int position) {
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {}

        public void onPageScrolled(int arg0, float arg1, int arg2) {}
    }
}
