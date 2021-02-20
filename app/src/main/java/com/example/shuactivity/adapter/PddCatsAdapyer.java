package com.example.shuactivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shuactivity.R;
import com.example.shuactivity.domain.PddGoodCat;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PddCatsAdapyer extends RecyclerView.Adapter<PddCatsAdapyer.ViewHodler> {

    private Context context;
    private PddCatsOnClickListener pddCatsOnClickListener;
    private List<PddGoodCat> dataList = new ArrayList<>();

    private int lastIndex = 0;

    public void setPddCatsOnClickListener(PddCatsOnClickListener pddCatsOnClickListener) {
        this.pddCatsOnClickListener = pddCatsOnClickListener;
    }

    public PddCatsAdapyer(Context context, List<PddGoodCat> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cats_item_daluzy, parent, false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        PddGoodCat cat = dataList.get(position);
        if (cat.isSelected()){
            holder.llLine.setVisibility(View.VISIBLE);
        }else {
            holder.llLine.setVisibility(View.GONE);
        }
        holder.tvCatName.setText(cat.getCat_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pddCatsOnClickListener.catOnClick(position,cat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_cat_name)
        TextView tvCatName;
        @BindView(R.id.ll_line)
        LinearLayout llLine;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void updateItem(int position){
        PddGoodCat pddGoodCat = dataList.get(lastIndex);
        pddGoodCat.setSelected(false);
        notifyItemChanged(lastIndex,pddGoodCat);

        PddGoodCat pddGoodCatNow = dataList.get(position);
        pddGoodCatNow.setSelected(true);
        notifyItemChanged(position,pddGoodCatNow);


        lastIndex = position;
    }

    public interface PddCatsOnClickListener{
        void catOnClick(int position,PddGoodCat pddGoodCat);
    }
}
