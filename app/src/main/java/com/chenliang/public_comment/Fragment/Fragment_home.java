package com.chenliang.public_comment.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenliang.public_comment.CityActivity;
import com.chenliang.public_comment.R;
import com.chenliang.public_comment.utils.MyUtils;
import com.chenliang.public_comment.utils.Shared_Utils;

import java.io.IOException;
import java.util.List;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class Fragment_home extends Fragment implements LocationListener {
    private View mview;
    private TextView topCity;
    private String cityName;
    private LocationManager locationManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview =  inflater.inflate(R.layout.fragment_home, container, false);

        topCity = mview.findViewById(R.id.index_top_city);


        //获取数据并显示
        cityName = Shared_Utils.getCityName(getActivity());

        topCity.setText(cityName);
        topCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 带有返回值的跳转
                startActivityForResult(new Intent(getActivity(), CityActivity.class), MyUtils.RequestCityCode);
            }
        });
        return mview;
    }

    /**
     * 处理跳转的返回值
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyUtils.RequestCityCode && resultCode == Activity.RESULT_OK){
            cityName = data.getStringExtra("city_name");
            topCity.setText(cityName);
        }
    }

    private void checkGPSIsOpen() {
        // 获取当前LocationManager对象
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isOpen) {
            //没有打开，进入GPS设置页面
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 0);
        }
        //开始定位
        startLocataion();
    }

    //使用GPS定位方法
    private void startLocataion() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
    }

    public void onStart(){
        super.onStart();
        //检查GPS模块是否打开
        //checkGPSIsOpen();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1){
                topCity.setText(cityName);
            }
            return false;
        }
    });

    private void updateWithNewLocation(Location location){
        double lat = 0.0,lng =0.0;
        if (location != null){
            lat = location.getAltitude();
            lng = location.getLongitude();
            Log.i(TAG, "经度是" + lat + "维度是" + lng);
        }else{
            cityName = "无法获取城市信息";
        }

        // 通过经纬度获取地址，由于地址会有多个，这个和精确度有关，本实例中定义了最大返回数为2
        List<Address> list = null;
        Geocoder ge = new Geocoder(getActivity());
        try {
            list = ge.getFromLocation(lat, lng, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onCreateView: list" + list.toString());
        if (list != null && list.size() > 0)
            for (int i = 0; i < list.size(); i++) {
                Address ad = list.get(i);
                cityName = ad.getLocality();
                Log.d(TAG, "onCreateView: ad.getLocality()" + cityName);
            }
        //向Handel对象发送空消息
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateWithNewLocation(location);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //保存当前定位城市
        Shared_Utils.putCityName(getActivity(),cityName);
        StopLocation();
    }

    //停止定位
    private void StopLocation(){
        locationManager.removeUpdates(this);
    }
}