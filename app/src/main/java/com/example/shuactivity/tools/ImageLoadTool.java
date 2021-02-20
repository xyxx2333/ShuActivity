package com.example.shuactivity.tools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

/**
 * 图片加载封装类
 */
public class ImageLoadTool {

    public static void imageLoad(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
//        Picasso.with(context).load(url).into(imageView);
    }
}
