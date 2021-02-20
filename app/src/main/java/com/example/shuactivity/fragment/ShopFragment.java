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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.shuactivity.PddCatsActivity;
import com.example.shuactivity.R;
import com.example.shuactivity.adapter.GoodsAdapyer;
import com.example.shuactivity.adapter.PddCatsAdapyer;
import com.example.shuactivity.adapter.ViewPagerAdapter;
import com.example.shuactivity.contants.Constants;
import com.example.shuactivity.domain.PddGoodCat;
import com.example.shuactivity.tools.JsonTool;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddGoodsCatsGetRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddGoodsCatsGetResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

public class ShopFragment extends Fragment {
    @BindView(R.id.cats_rv_list)
    RecyclerView catsRvList;
    @BindView(R.id.viewPager_shop)
    ViewPager viewPagerShop;
    @BindView(R.id.all_cats_img)
    ImageView allCatsImg;


    private Context context;
    private View view;
    private GoodsAdapyer goodsAdapyer;
    private String clientId;
    private String clientSecret;
    private PddCatsAdapyer pddCatsAdapyer;

    private PddGoodsFirstFragment catFragment;

    private List<Fragment> fragmentList;
    private PddGoodsCatFragment pddGoodsCatFragment1,
            pddGoodsCatFragment2,
            pddGoodsCatFragment3,
            pddGoodsCatFragment4,
            pddGoodsCatFragment5,
            pddGoodsCatFragment6,
            pddGoodsCatFragment7,
            pddGoodsCatFragment8;

    private ViewPagerAdapter viewPagerAdapter;
    List<PddGoodCat> catList = new ArrayList<>();
    List<PddGoodCat> catFirstList = new ArrayList<>();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    catList = (List<PddGoodCat>) msg.obj;
                    if (catList.size() > 8) {
                        List<PddGoodCat> catList2 = catList.subList(0, 8);
                        PddGoodCat cat = new PddGoodCat();
                        cat.setCat_id(-1l);
                        cat.setCat_name("全部");
                        catList2.add(0,cat);
                        pddCatsAdapyer = new PddCatsAdapyer(context, catList2);
                        catsRvList.setAdapter(pddCatsAdapyer);
                        pddCatsAdapyer.setPddCatsOnClickListener(new PddCatsAdapyer.PddCatsOnClickListener() {
                            @Override
                            public void catOnClick(int position, PddGoodCat pddGoodCat) {
                                pddCatsAdapyer.updateItem(position);
                                viewPagerShop.setCurrentItem(position);
                                catsRvList.scrollToPosition(position);
                            }
                        });
                        initView(catList2);
                    }
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

        view = inflater.inflate(R.layout.shop_fragment_daluzy, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        clientId = Constants.PDD_CLIENT_ID;
        clientSecret = Constants.PDD_CLIENT_SECRET;

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(HORIZONTAL);
        catsRvList.setLayoutManager(layoutManager);

        getCatData();
        return view;
    }

    private void initView(List<PddGoodCat> catList2) {
        catFragment = new PddGoodsFirstFragment();
        pddGoodsCatFragment1 = new PddGoodsCatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cat", catList2.get(0));
        pddGoodsCatFragment1.setArguments(bundle);

        pddGoodsCatFragment2 = new PddGoodsCatFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("cat", catList2.get(1));
        pddGoodsCatFragment2.setArguments(bundle2);

        pddGoodsCatFragment3 = new PddGoodsCatFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("cat", catList2.get(2));
        pddGoodsCatFragment3.setArguments(bundle3);

        pddGoodsCatFragment4 = new PddGoodsCatFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putSerializable("cat", catList2.get(3));
        pddGoodsCatFragment4.setArguments(bundle4);

        pddGoodsCatFragment5 = new PddGoodsCatFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putSerializable("cat", catList2.get(4));
        pddGoodsCatFragment5.setArguments(bundle5);

        pddGoodsCatFragment6 = new PddGoodsCatFragment();
        Bundle bundle6 = new Bundle();
        bundle6.putSerializable("cat", catList2.get(5));
        pddGoodsCatFragment6.setArguments(bundle6);

        pddGoodsCatFragment7 = new PddGoodsCatFragment();
        Bundle bundle7 = new Bundle();
        bundle7.putSerializable("cat", catList2.get(6));
        pddGoodsCatFragment7.setArguments(bundle7);

        pddGoodsCatFragment8 = new PddGoodsCatFragment();
        Bundle bundle8 = new Bundle();
        bundle8.putSerializable("cat", catList2.get(7));
        pddGoodsCatFragment8.setArguments(bundle8);
        fragmentList = new ArrayList<>();

        fragmentList.add(catFragment);
        fragmentList.add(pddGoodsCatFragment1);
        fragmentList.add(pddGoodsCatFragment2);
        fragmentList.add(pddGoodsCatFragment3);
        fragmentList.add(pddGoodsCatFragment4);
        fragmentList.add(pddGoodsCatFragment5);
        fragmentList.add(pddGoodsCatFragment6);
        fragmentList.add(pddGoodsCatFragment7);
        fragmentList.add(pddGoodsCatFragment8);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPagerShop.setAdapter(viewPagerAdapter);
        viewPagerShop.setCurrentItem(0);
        viewPagerShop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pddCatsAdapyer.updateItem(position);
                catsRvList.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getCatData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddGoodsCatsGetRequest request = new PddGoodsCatsGetRequest();
                request.setParentCatId(0L);
                PddGoodsCatsGetResponse response = null;
                try {
                    response = client.syncInvoke(request);
                    String data = JsonUtil.transferToJson(response);
                    JSONObject jsonObject = JSON.parseObject(data);
                    String listData = jsonObject.getJSONObject("goods_cats_get_response").getString("goods_cats_list");
                    List<PddGoodCat> catList = JsonTool.jsonToList(listData, PddGoodCat.class);

                    Message message = new Message();  //Handler的写法
                    message.what = 2;
                    message.obj = catList;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }


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
    @OnClick({R.id.all_cats_img})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.all_cats_img:
                Intent intent = new Intent(context,PddCatsActivity.class);
                intent.putExtra("cats", (Serializable) catList);
                context.startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
