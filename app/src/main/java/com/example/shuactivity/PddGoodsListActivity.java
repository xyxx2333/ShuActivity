package com.example.shuactivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shuactivity.adapter.GoodsAdapyer;
import com.example.shuactivity.adapter.PddCatsIconAdapyer;
import com.example.shuactivity.contants.Constants;
import com.example.shuactivity.domain.PddGoodCat;
import com.example.shuactivity.tools.CatData;
import com.example.shuactivity.tools.JsonTool;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PddGoodsListActivity extends BaseActivity {


    private GoodsAdapyer goodsAdapyer;
    private String clientId;
    private String clientSecret;
    private PddGoodCat cat;
    private int page = 0;
    private int pageSize = 30;

    @BindView(R.id.shop_goods_list)
    RecyclerView shopGoodsList;
    @BindView(R.id.shop_srl)
    SmartRefreshLayout shopSrl;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = (List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>) msg.obj;
                    if (dataList != null && dataList.size() > 0) {
                        if (page == 1) {
                            goodsAdapyer = new GoodsAdapyer(context, dataList);
                            shopGoodsList.setAdapter(goodsAdapyer);
                        } else {
                            if (goodsAdapyer != null) {
                                goodsAdapyer.addAllData(dataList);
                            }
                        }

                        if (dataList.size() < pageSize) {
                            shopSrl.setEnableLoadMore(false); //是否可以继续加载(false)
                        }
                    } else {
                        shopSrl.setEnableLoadMore(false); //是否可以继续加载(false)
//                          refreshLayout.setEnableRefresh(false);  //是否可以下拉刷新
                    }
                    shopSrl.finishLoadMore(); //加载完成后关闭
                    shopSrl.finishRefresh(); //刷新完成后关闭
                    break;
            }

        }
    };


    @Override
    public void initView() {
        contentView(R.layout.pdd_goods_cat_list_activitiy);
        ButterKnife.bind(this);
        //设置标题
        setTitle("所有分类");
        //显示返回键
        showBackImage();
        cat = (PddGoodCat) getIntent().getSerializableExtra("cat");
        setTitle(cat.getCat_name());
        showBackImage();
        clientId = Constants.PDD_CLIENT_ID;
        clientSecret = Constants.PDD_CLIENT_SECRET;
        /**
         * 下拉刷新
         */
        shopSrl.setOnRefreshListener(refreshLayout -> {
            page = 0;
            initData();
        });

        /**
         * 上拉加载
         */
        shopSrl.setOnLoadMoreListener(refreshLayout -> initData());

        shopGoodsList.setLayoutManager(new LinearLayoutManager(context));
        initData();

    }




    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                page++;
                PopClient client = new PopHttpClient(clientId, clientSecret);
                PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
                request.setCatId(cat.getCat_id());
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


                Message message = new Message();  //Handler的写法
                message.what = 1;
                message.obj = dataList;
                handler.sendMessage(message);

//                ((Activity)context).runOnUiThread(new Runnable() {  //runOnUiThread的写法
//                    @Override
//                    public void run() {
//                        if (dataList != null && dataList.size()>0){
//                            if (page == 1){
//                                goodsAdapyer = new GoodsAdapyer(MainActivity.this,dataList);
//                                recyclerView.setAdapter(goodsAdapyer);
//                            }else {
//                                if (goodsAdapyer != null){
//                                    goodsAdapyer.addAllData(dataList);
//                                }
//                            }
//
//                            if (dataList.size() < pageSize){
//                                refreshLayout.setEnableLoadMore(false); //是否可以继续加载(false)
//                            }
//                        }else {
//                            refreshLayout.setEnableLoadMore(false); //是否可以继续加载(false)
////                          refreshLayout.setEnableRefresh(false);  //是否可以下拉刷新
//                        }
//                        refreshLayout.finishLoadMore(); //加载完成后关闭
//                        refreshLayout.finishRefresh(); //刷新完成后关闭
//                    }
//                });


            }
        }).start();
    }
}
