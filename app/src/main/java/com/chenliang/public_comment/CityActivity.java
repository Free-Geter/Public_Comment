package com.chenliang.public_comment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chenliang.public_comment.consts.CONSTS;
import com.chenliang.public_comment.entity.City;
import com.chenliang.public_comment.entity.ResponseObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {

    private ListView cities;
    private List<City> citylist;
    private TextView back,refresh,select_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        cities = findViewById(R.id.ListView_City);
        back = findViewById(R.id.index_city_back);
        refresh = findViewById(R.id.index_city_refresh);

        View view = LayoutInflater.from(this).inflate(R.layout.home_city_search,null);

        new CityDataTask().execute();

        // 添加表头
        cities.addHeaderView(view);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CityDataTask().execute();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                TextView textView = view.findViewById(R.id.city_list_item_name);
                intent.putExtra("city_name",textView.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }


    /**
     * 使用异步任务获取城市的json
     */
    public class CityDataTask extends AsyncTask<Void,Void,List<City>>{
        @Override
        protected List<City> doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(CONSTS.City_Data_URL);
                connection = (HttpURLConnection) url.openConnection();
                //设置请求方法
                connection.setRequestMethod("GET");
                //设置连接超时时间（毫秒）
                connection.setConnectTimeout(10000*10);
                //设置读取超时时间（毫秒）
                connection.setReadTimeout(5000*2);

                int code = connection.getResponseCode();
                // 成功连接
                if (code == HttpURLConnection.HTTP_OK){
                    // 获取输入流
                    new InputStreamReader(connection.getInputStream());
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    // 控制台输出测试
                    Log.d("json_", "doInBackground: " + response.toString());
                    // 调用方法解析Json字符串
                    return parseCityDataJson(response.toString());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
            }

        // 提交时调用此回调函数
        @Override
        protected void onPostExecute(List<City> cities_list) {
            super.onPostExecute(cities_list);

            //使用解析出来的list赋值dataSource
            Log.d("json_", "onPostExecute: " + "get city list success");
            citylist = cities_list;

            // 在ListView中适配显示city信息
            // 1.初始化适配器
            My_adapter adapter = new My_adapter(citylist);
            // 2.为ListView设置适配器
            cities.setAdapter(adapter);
        }

        /**
         * 解析获取到的城市信息json字符串
         * @param json
         * @return list<City>
         */
        public List<City> parseCityDataJson(String json){
            Gson gson = new Gson();

            // 按照ResponseObject类内定义的格式解析json
            Type City_type = new TypeToken<ResponseObject<List<City>>>(){}.getType();
            ResponseObject<List<City>> responseObject = gson.fromJson(json,City_type);

            // 这里调用的是ResponseObject类内的setter函数
            return responseObject.getDatas();
        }
        }


        // 用来保存城市首字母的索引
        private StringBuffer buffer = new StringBuffer();
    // 保存索引值对应城市的名称
    private List<String> first_list = new ArrayList<>();
    /**
     * ListView适配器
     */
    public class My_adapter extends BaseAdapter{

        private List<City> list_cities_data;

        public My_adapter(List<City> list_cities_data) {
            this.list_cities_data = list_cities_data;
        }

        @Override
        public int getCount() {
            // 返回城市list的长度
            return list_cities_data.size();
        }

        @Override
        public Object getItem(int position) {
            // 返回城市list中对应索引的元素
            return list_cities_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        // 初始化控件布局
        {
            View view = null;
            Holder holder;
            // 如果是第一次加载
            if (convertView == null){
                holder = new Holder();
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_city_list_item,parent,false);
                // 缓存图片控件实例
                view.setTag(holder);
            }else{
                holder = (Holder)view.getTag();
            }

            // 数据填充
            City city = list_cities_data.get(position);
            String sort = city.getSkey();
            String name = city.getName();


            // 保证只显示一次城市索引
            if (buffer.indexOf(sort) == -1){
                buffer.append(sort);
                holder.keySort.setText(sort);
                holder.keySort.setVisibility(View.VISIBLE);
            }
            else {
                holder.keySort.setVisibility(View.GONE);
            }
            holder.city_name.setText(name);


            return view;
        }

    }

    public class Holder{
        TextView keySort,city_name;
        Holder(){
             keySort = findViewById(R.id.city_list_item_sort);
             city_name = findViewById(R.id.city_list_item_name);
        }
    }
    }