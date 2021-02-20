package com.example.shuactivity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.shuactivity.R;
import com.example.shuactivity.adapter.GoodsAdapyer;
import com.example.shuactivity.adapter.PddCatsAdapyer;
import com.example.shuactivity.contants.Constants;
import com.example.shuactivity.domain.PddGoodCat;
import com.example.shuactivity.tools.JsonTool;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddGoodsCatsGetRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddGoodsCatsGetResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

public class PddGoodsCatFragment extends Fragment {
    @BindView(R.id.shop_goods_list)
    RecyclerView shopGoodsList;
    @BindView(R.id.shop_srl)
    SmartRefreshLayout shopSrl;
    private Context context;
    private GoodsAdapyer goodsAdapter;
    private View view;
    private int page = 0;
    private int pageSize = 30;

    private String clientId;
    private String clientSecret;
    private PddGoodCat cat;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = (List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>) msg.obj;
                    if (dataList != null && dataList.size() > 0) {
                        if (page == 1) {
                            goodsAdapter = new GoodsAdapyer(context, dataList);
                            shopGoodsList.setAdapter(goodsAdapter);
                        } else {
                            if (goodsAdapter != null) {
                                goodsAdapter.addAllData(dataList);
                            }
                        }
                        if (dataList.size() < pageSize) {
                            shopSrl.setEnableLoadMore(false);
                        }
                    } else {
                        shopSrl.setEnableLoadMore(false);
                    }
                    shopSrl.finishLoadMore();
                    shopSrl.finishRefresh();
                    break;

            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.pdd_goods_cat_list, container, false);
        ButterKnife.bind(this, view);

        context = getContext();
        assert getArguments() != null;
        cat = (PddGoodCat) getArguments().getSerializable("cat");
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
        shopSrl.setOnLoadMoreListener(refreshLayout -> {
            initData();
        });

        shopGoodsList.setLayoutManager(new LinearLayoutManager(context));
        initData();
        return view;
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
                String data = JsonUtil.transferToJson(response);
                List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = response.getGoodsSearchResponse().getGoodsList();
                Message message = new Message();
                message.what = 1;
                message.obj = dataList;
                handler.sendMessage(message);

            }
        }).start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
