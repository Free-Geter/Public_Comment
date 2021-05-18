package com.chenliang.public_comment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chenliang.public_comment.Fragment.Fragment_home;
import com.chenliang.public_comment.Fragment.Fragment_my;
import com.chenliang.public_comment.Fragment.Fragment_search;
import com.chenliang.public_comment.Fragment.Fragment_tuan;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup group;
    private RadioButton rb;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        group = findViewById(R.id.main_button_tabs);
        rb = findViewById(R.id.main_home);
        fragmentManager = getSupportFragmentManager();

        group.setOnCheckedChangeListener(this);

        // 设置默认选中
        rb.setChecked(true);
        changeFragment(new Fragment_home(),false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.main_home:
                changeFragment(new Fragment_home(),true);
                break;
            case R.id.main_my:
                changeFragment(new Fragment_my(),true);
                break;
            case R.id.main_search:
                changeFragment(new Fragment_search(),true);
                break;
            case R.id.main_tuan:
                changeFragment(new Fragment_tuan(),true);
                break;
            default:
                break;
        }
    }

    //Fragment 切换
    public void changeFragment(Fragment fragment,boolean isInit){
        // 开启Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content,fragment);
        if (!isInit) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}