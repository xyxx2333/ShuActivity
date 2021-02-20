package com.example.shuactivity.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shuactivity.R;
import com.example.shuactivity.adapter.NewsAdapyer;
import com.example.shuactivity.domain.NewsResultData;
import com.example.shuactivity.tools.HttpTools;
import com.example.shuactivity.tools.JsonTool;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment {
    private int page = 0;
    private int pageSize = 30;
    @BindView(R.id.news_list)
    RecyclerView newsList;
    @BindView(R.id.news_srl)
    SmartRefreshLayout newsSrl;
    private Context context;
    private View view;
    NewsResultData newsResultData;
    NewsAdapyer goodsAdapyer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
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
        view = inflater.inflate(R.layout.news_fragment_daluzy, container, false);
        ButterKnife.bind(this, view);
        /**
         * 下拉刷新
         */
        newsSrl.setOnRefreshListener(refreshLayout -> {
            page = 0;
            initData();
        });

        /**
         * 上拉加载
         */
        newsSrl.setOnLoadMoreListener(refreshLayout -> initData());

        newsList.setLayoutManager(new LinearLayoutManager(context));

        initData();
        return view;
    }

    public void initData(){
        page++;
        HttpTools.postData("http://v.juhe.cn/toutiao/index", new HttpTools.HttpBackListener() {
            @Override
            public void onSuccess(String data, int code) {
                try {

                    newsResultData = JsonTool.jsonToObject(data, NewsResultData.class);

                    if (newsResultData != null && newsResultData.getError_code() == 0){
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (newsResultData != null && newsResultData.getResult().getData().size()>0){
                                    if (page == 1) {
                                         goodsAdapyer = new NewsAdapyer(context, newsResultData.getResult().getData());
                                        newsList.setAdapter(goodsAdapyer);
                                    }else {
                                        if (goodsAdapyer != null){
                                            goodsAdapyer.addAllData(newsResultData.getResult().getData());
                                        }
                                    }
                                    if (newsResultData.getResult().getData().size()<pageSize){
                                        newsSrl.setEnableLoadMore(false);
                                    }
                                }else {
                                    newsSrl.setEnableLoadMore(false);
                                }

                            }
                        });
                    }else {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newsSrl.setEnableLoadMore(false);
                                if (newsResultData != null){
                                    Toast.makeText(context,newsResultData.getReason(),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, R.string.data_error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                newsSrl.finishRefresh();
                newsSrl.finishLoadMore();

            }

            @Override
            public void onError(final String erroe, int code) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (erroe != null && !erroe.isEmpty()){
                            Toast.makeText(context,erroe,Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, R.string.data_error,Toast.LENGTH_SHORT).show();
                        }
                        newsSrl.setEnableLoadMore(false);
                        newsSrl.finishRefresh();
                        newsSrl.finishLoadMore();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
