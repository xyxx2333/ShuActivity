package com.example.shuactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;


import com.example.shuactivity.adapter.GoodsAdapyer;
import com.example.shuactivity.adapter.NewsAdapyer;
import com.example.shuactivity.contants.Constants;
import com.example.shuactivity.domain.News;
import com.example.shuactivity.domain.NewsResultData;
import com.example.shuactivity.tools.HttpTools;
import com.example.shuactivity.tools.JsonTool;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GoodsAdapyer goodsAdapyer;
    private SmartRefreshLayout refreshLayout;
    private int page = 0;
    private int pageSize = 30;



    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = (List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>) msg.obj;

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = this.findViewById(R.id.rv_list);
        refreshLayout = this.findViewById(R.id.srl);

        /**
         * 下拉刷新
         */
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 0;
            getData();
        });

        /**
         * 上拉加载
         */
        refreshLayout.setOnLoadMoreListener(refreshLayout -> getData());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        getData();
        getNewsData();

    }

    public void getNewsData(){
//        HttpTools.getData("http://v.juhe.cn/toutiao/index?&key=aa0c4da3240b00be1e70fa81aefc0aac");
        HttpTools.postData("http://v.juhe.cn/toutiao/index", new HttpTools.HttpBackListener() {
            @Override
            public void onSuccess(String data, int code) {
                try {

                    final NewsResultData newsResultData = JsonTool.jsonToObject(data, NewsResultData.class);

                    if (newsResultData != null && newsResultData.getError_code() == 0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NewsAdapyer goodsAdapyer = new NewsAdapyer(MainActivity.this,newsResultData.getResult().getData());
                                recyclerView.setAdapter(goodsAdapyer);
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (newsResultData != null){
                                    Toast.makeText(MainActivity.this,newsResultData.getReason(),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(MainActivity.this, R.string.data_error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


//                    JSONObject jsonObject = new JSONObject(data);
//                    String result =jsonObject.getString("result");
//
//                    JSONObject jsonObject2 = new JSONObject(result);
//                    JSONArray jsonArray = jsonObject2.getJSONArray("data");
//                    final List<News> news = new ArrayList<>();
//                    for (int i = 0;i<jsonArray.length();i++){
//                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
//                        News news1 = new News();
//                        news1.setUniquekey(jsonObject3.getString("uniquekey"));
//                        news1.setTitle(jsonObject3.getString("title"));
//                        news1.setDate(jsonObject3.getString("date"));
//                        news1.setAuthor_name(jsonObject3.getString("author_name"));
//                        news1.setCategory(jsonObject3.getString("category"));
//                        news1.setUrl(jsonObject3.getString("url"));
//                        news1.setThumbnail_pic_s(jsonObject3.getString("thumbnail_pic_s"));
////                        news1.setThumbnail_pic_s02(jsonObject3.getString("thumbnail_pic_s02"));
////                        news1.setThumbnail_pic_s03(jsonObject3.getString("thumbnail_pic_s03"));
//                        news.add(news1);
//                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(final String erroe, int code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (erroe != null && !erroe.isEmpty()){
                            Toast.makeText(MainActivity.this,erroe,Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, R.string.data_error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void getData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                page ++;
                String clientId = Constants.PDD_CLIENT_ID;
                String clientSecret = Constants.PDD_CLIENT_SECRET;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
                request.setPage(page);
                request.setPageSize(pageSize);
                PddDdkGoodsSearchResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String str = JsonUtil.transferToJson(response);

                List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = response.getGoodsSearchResponse().getGoodsList();


//                Message message = new Message();
//                message.what = 1;
//                message.obj = dataList;
//                handler.sendMessage(message);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dataList != null && dataList.size()>0){
                            if (page == 1){
                                goodsAdapyer = new GoodsAdapyer(MainActivity.this,dataList);
                                recyclerView.setAdapter(goodsAdapyer);
                            }else {
                                if (goodsAdapyer != null){
                                    goodsAdapyer.addAllData(dataList);
                                }
                            }

                            if (dataList.size() < pageSize){
                                refreshLayout.setEnableLoadMore(false); //是否可以继续加载(false)
                            }
                        }else {
                            refreshLayout.setEnableLoadMore(false); //是否可以继续加载(false)
//                          refreshLayout.setEnableRefresh(false);  //是否可以下拉刷新
                        }
                       refreshLayout.finishLoadMore(); //加载完成后关闭
                        refreshLayout.finishRefresh(); //刷新完成后关闭
                    }
                });


            }
        }).start();

    }
}