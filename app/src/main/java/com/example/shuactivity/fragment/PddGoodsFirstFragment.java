package com.example.shuactivity.fragment;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shuactivity.PddGoodsListActivity;
import com.example.shuactivity.R;
import com.example.shuactivity.adapter.GoodsAdapyer;
import com.example.shuactivity.adapter.PddCatsIconAdapyer;
import com.example.shuactivity.contants.Constants;
import com.example.shuactivity.domain.PddGoodCat;
import com.example.shuactivity.tools.CatData;
import com.example.shuactivity.tools.JsonTool;
import com.example.shuactivity.utils.MyGridLayoutManager;
import com.example.shuactivity.utils.MyLinearLayoutManager;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PddGoodsFirstFragment extends Fragment {


    @BindView(R.id.shop_goods_list)
    RecyclerView shopGoodsList;
    @BindView(R.id.shop_srl)
    SmartRefreshLayout shopSrl;
    @BindView(R.id.shop_cat_list)
    RecyclerView shopCatList;
    
    private Context context;
    private View view;
    private GoodsAdapyer goodsAdapyer;
    private String clientId;
    private String clientSecret;
    private PddGoodCat cat;
    private int page = 0;
    private int pageSize = 30;
    private PddCatsIconAdapyer catsIconAdapyer;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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

        view = inflater.inflate(R.layout.pdd_goods_first_page, container, false);
        ButterKnife.bind(this, view);

        context = getContext();


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

        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(context);
        linearLayoutManager.setvScroll(false);
        shopGoodsList.setLayoutManager(linearLayoutManager);

        List<PddGoodCat> pddGoodCats = JsonTool.jsonToList(CatData.getCatData(), PddGoodCat.class);
        for (PddGoodCat pddGoodCat : pddGoodCats) {
            switch (pddGoodCat.getCat_name()) {
                case "男装":
                    pddGoodCat.setIcon(R.drawable.manwear);
                    break;
                case "手机":
                    pddGoodCat.setIcon(R.drawable.phone);
                    break;
                case "女装":
                    pddGoodCat.setIcon(R.drawable.womenwear);
                    break;
                case "女鞋":
                    pddGoodCat.setIcon(R.drawable.womenshoes);
                    break;
                case "腕表眼镜":
                    pddGoodCat.setIcon(R.drawable.watches);
                    break;
                case "童装":
                    pddGoodCat.setIcon(R.drawable.childrenwear);
                    break;
                case "箱包皮具":
                    pddGoodCat.setIcon(R.drawable.bages);
                    break;
                case "居家日用":
                    pddGoodCat.setIcon(R.drawable.daily);
                    break;
            }
        }

        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(context,4);
        gridLayoutManager.setvScroll(false);
        gridLayoutManager.sethScroll(false);
        shopCatList.setLayoutManager(gridLayoutManager);

        catsIconAdapyer = new PddCatsIconAdapyer(context, pddGoodCats);
        shopCatList.setAdapter(catsIconAdapyer);
        initData();

        catsIconAdapyer.setPddCatsOnClickListener(new PddCatsIconAdapyer.PddCatsOnClickListener() {
            @Override
            public void catOnClick(int position, PddGoodCat pddGoodCat) {
                //跳转产品列表
                Intent intent = new Intent(context, PddGoodsListActivity.class);
                intent.putExtra("cat",pddGoodCat);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                page++;
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
