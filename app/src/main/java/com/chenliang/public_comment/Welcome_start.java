package com.chenliang.public_comment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.chenliang.public_comment.utils.Shared_Utils;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome_start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_start);

//        //使用handler 实现延时跳转
//        new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                return false;
//            }
//        }).sendEmptyMessageDelayed(0,3000);// 延时3秒执行

        Timer timer = new Timer();
        timer.schedule(new Task(),3000);
    }

    class Task extends TimerTask{

        @Override
        public void run() {
            //判断是否第一次启动APP
            if (Shared_Utils.getWelcomeBoolean(getBaseContext())){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
            //第一次启动APP，就跳转到guide页面
            else{
                startActivity(new Intent(Welcome_start.this,WelcomeGuideAct.class));
                Shared_Utils.putWelcomeBoolean(getBaseContext(),true);
            }
            finish();
        }
    }
}