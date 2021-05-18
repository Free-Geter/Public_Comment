package com.chenliang.public_comment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class WelcomeGuideAct extends AppCompatActivity {
    private static final String TAG = "PagerAdapter";
    private Button btn;
    private ViewPager pager;
    private List<View> img_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);

        btn = findViewById(R.id.welcome_guide_btn);
        pager = findViewById(R.id.welcome_pager);

        initViewPager();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    public void initViewPager(){
        img_list = new ArrayList<View>();
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.new_comer_guide_fg_1);
        img_list.add(iv);
        ImageView iv2 = new ImageView(this);
        iv.setImageResource(R.drawable.new_comer_guide_fg_2);
        img_list.add(iv2);
        ImageView iv3 = new ImageView(this);
        iv.setImageResource(R.drawable.new_comer_guide_fg_3);
        img_list.add(iv3);
        ImageView iv4 = new ImageView(this);
        iv.setImageResource(R.drawable.new_comer_guide_fg_4);
        img_list.add(iv4);

        pager.setAdapter(new MyPagerAdapter());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3){
                    btn.setVisibility(View.VISIBLE);
                }
                else
                    btn.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // ViewPager 适配器
    class MyPagerAdapter extends PagerAdapter {

        // 计算需要多少item显示
        @Override
        public int getCount() {
            return img_list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        // 初始化item实例的方法
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem: " + position);
            container.addView(img_list.get(position));
            return img_list.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(img_list.get(position));
        }
    }
}