package com.example.shuactivity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shuactivity.R;
import com.example.shuactivity.WebViewActivity;
import com.example.shuactivity.domain.News;
import com.example.shuactivity.tools.ImageLoadTool;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapyer extends RecyclerView.Adapter<NewsAdapyer.ViewHodler> {
    private Context context;
    private List<News> dataList = new ArrayList<>();
    public NewsAdapyer(Context context, List<News> dataList){
    this.context = context;
    this.dataList = dataList;
    }

    public void addAllData(List<News> dataListUpdate){
        if (dataListUpdate != null && dataListUpdate.size()>0){
            dataList.addAll(dataListUpdate);
            notifyItemRangeChanged(dataList.size() - dataListUpdate.size(),dataListUpdate.size());
        }
    }


    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_ites_daluzy,parent,false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        final News item = dataList.get(position);
        ImageLoadTool.imageLoad(context,item.getThumbnail_pic_s(),holder.ivGoodsImage);
        //传入context上下文
        //被加载图像的Url地址，大多数情况下，一个字符串代表一个网络图片的URL
        //图片最终要展示的地方

        holder.tvGoodsName.setText(item.getTitle());
        holder.tvGoodsDes.setText(item.getAuthor_name());

        holder.tvGoodCoupon.setText(item.getCategory());
        holder.tvGoodsSales.setText("");

        holder.tvGoodsEndTime.setText(item.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url",item.getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHodler extends RecyclerView.ViewHolder{
        private ImageView ivGoodsImage;
        private TextView tvGoodsName;
        private TextView tvGoodsDes;
        private TextView tvGoodCoupon;
        private TextView tvGoodsSales;
        private TextView tvGoodsEndTime;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            ivGoodsImage = itemView.findViewById(R.id.iv_goods_image);
            tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvGoodsDes = itemView.findViewById(R.id.tv_goods_des);
            tvGoodCoupon = itemView.findViewById(R.id.tv_goods_coupon);
            tvGoodsSales = itemView.findViewById(R.id.tv_goods_sales);
            tvGoodsEndTime = itemView.findViewById(R.id.tv_goods_end_time);
        }
    }
}
