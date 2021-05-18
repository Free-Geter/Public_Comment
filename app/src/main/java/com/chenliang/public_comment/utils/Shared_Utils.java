package com.chenliang.public_comment.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

//实现标记的写入和读取
public class Shared_Utils {
    private static final String FILE_NAME = "dianping";
    private static final String MODE_NAME = "welcome";

    // 获取Boolean类型的值
    public static boolean getWelcomeBoolean(Context context){
        return context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).getBoolean(MODE_NAME,false);
    }

    // 写入boolean类型的值
    public static void putWelcomeBoolean (Context context,boolean isFirst){
        @SuppressLint("WrongConstant") SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME,Context.MODE_APPEND).edit();
        editor.putBoolean(MODE_NAME,isFirst);
        editor.commit();
    }

    // 获取一个String类型的数据
    public static void putCityName(Context context,String CityName){
        @SuppressLint("WrongConstant") SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME,Context.MODE_APPEND).edit();
        editor.putString("cityName",CityName);
        editor.commit();
    }

    // 写入一个String类型的数据
    public static String getCityName(Context context){
        return context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).getString("cityName","选择城市");
    }
}
