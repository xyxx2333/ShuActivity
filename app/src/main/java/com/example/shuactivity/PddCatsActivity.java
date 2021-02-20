package com.example.shuactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.donkingliang.labels.LabelsView;
import com.example.shuactivity.adapter.AllCatsAdapyer;
import com.example.shuactivity.domain.PddGoodCat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PddCatsActivity extends BaseActivity {

    @BindView(R.id.cats_gv_list)
    GridView catsGvList;
    @BindView(R.id.cats_labels_list)
    LabelsView catsLabelsList;
    private List<PddGoodCat> catList = new ArrayList<>();
    private AllCatsAdapyer allCatsAdapyer;

    @Override
    public void initView() {
        contentView(R.layout.activity_pdd_cats);
        ButterKnife.bind(this);
        //设置标题
        setTitle("所有分类");
        //显示返回键
        showBackImage();
        catList = (List<PddGoodCat>) getIntent().getSerializableExtra("cats");
//        allCatsAdapyer = new AllCatsAdapyer(context, catList);
//        catsGvList.setAdapter(allCatsAdapyer);

        catsLabelsList.setLabels(catList, new LabelsView.LabelTextProvider<PddGoodCat>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, PddGoodCat data) {
                return data.getCat_name();
            }
        });

        //标签的点击监听
        catsLabelsList.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                //label是被点击的标签，data是标签所对应的数据，position是标签的位置。
                Intent intent = new Intent(context,PddGoodsListActivity.class);
                PddGoodCat goodCat = (PddGoodCat) data;
                intent.putExtra("cat",goodCat);
                startActivity(intent);
            }
        });

    }



}