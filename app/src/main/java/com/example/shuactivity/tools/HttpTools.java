package com.example.shuactivity.tools;


import android.util.Log;

import com.example.shuactivity.contants.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpTools {
    public static void getData(final String path, final HttpBackListener backListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();//处理字符串
                try {
                    //创建URL
                    URL url = new URL(path);
                    //获取HttpURLConnection对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //请求超时时间5000毫秒
                    connection.setConnectTimeout(9000);
                    //读取超时的时间
                    connection.setReadTimeout(9000);

                    //connection请求方式
                    connection.setRequestMethod("GET");
                    //请求的数据结构
                    connection.setRequestProperty("Content-type","application/x-java-serialized-object");
                    //HttpURLConnection发起连接
                    connection.connect();


                    //判断连接请求成功的话，处理数据
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        String temp;
                        while ((temp = bufferedReader.readLine()) != null){
                            sb.append(temp);
                        }
                        bufferedReader.close();
                        backListener.onSuccess(sb.toString(),connection.getResponseCode());
                    }else {
                        backListener.onError(connection.getResponseMessage(),connection.getResponseCode());
                    }
                    connection.disconnect();//断开连接
                    Log.i("qin1",sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void postData(final String path, final HttpBackListener backListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();//处理字符串
                try {
                    //创建URL
                    URL url = new URL(path);
                    //获取HttpURLConnection对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //请求超时时间5000毫秒
                    connection.setConnectTimeout(9000);
                    //读取超时的时间
                    connection.setReadTimeout(9000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setDefaultUseCaches(false);
                    //connection请求方式
                    connection.setRequestMethod("POST");



                    //HttpURLConnection发起连接
                    connection.connect();

                    String body = "key="+Constants.NEWS_APPKEY;
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
                    writer.write(body);
                    writer.close();


                    //判断连接请求成功的话，处理数据
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        String temp;
                        while ((temp = bufferedReader.readLine()) != null){
                            sb.append(temp);
                        }
                        bufferedReader.close();
                        backListener.onSuccess(sb.toString(),connection.getResponseCode());
                    }else {
                        backListener.onError(connection.getResponseMessage(),connection.getResponseCode());
                    }
                    connection.disconnect();//断开连接
                    Log.i("qin2",sb.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface HttpBackListener{
        void onSuccess(String data,int code);
        void onError(String erroe,int code);
    }
}
