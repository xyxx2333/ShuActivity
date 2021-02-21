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
import com.example.shuactivity.domain.Joke;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.ViewHolder> {


    private Context context;
    private List<Joke> dataList = new ArrayList<>();

    public JokesAdapter(Context context, List<Joke> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void addAllData(List<Joke> dataList1){
        dataList.addAll(dataList);
        notifyItemRangeChanged(dataList.size() -dataList1.size(),dataList1.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.joke_item_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvJakeContent.setText(dataList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


     class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_jake_content)
        TextView tvJakeContent;
        @BindView(R.id.ll_line)
        LinearLayout llLine;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
