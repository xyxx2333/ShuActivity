package com.example.shuactivity.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shuactivity.R;
import com.example.shuactivity.domain.PddGoodCat;

import java.util.ArrayList;
import java.util.List;

public class AllCatsAdapyer extends BaseAdapter {
    private Context context;
    private List<PddGoodCat> dataList = new ArrayList<>();

    public AllCatsAdapyer(Context context,List<PddGoodCat> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(context,R.layout.cats_item_daluzy,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(dataList.get(position).getCat_name());
        return convertView;
    }

    class ViewHolder{
        TextView textView;
        public ViewHolder(View view){
            textView = view.findViewById(R.id.tv_cat_name);
        }
    }
}
