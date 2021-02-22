package com.example.shuactivity.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private  MyBinder myBinder = new MyBinder();
    private  MyReceive myServiceReceive ;
    public   static String  MYSERVICE_BC ="myservice_bc";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyService>>","onCreate");

        //广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MYSERVICE_BC);
        myServiceReceive = new MyReceive();
        registerReceiver(myServiceReceive, intentFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyService>>","onStartCommand");
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyService>>","onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("MyService>>","onUnbind");
        return super.onUnbind(intent);

    }
    class MyReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String flag = intent.getStringExtra("flag");
            if (flag != null) {
                switch (flag){
                    case "myservice_bc":
                        longTimeDo();
                        break;
                }
            }
        }
    }
    public void longTimeDo(){
        Log.i("MyService>>","longTimeDo");
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i= 0;
                while (i<10000){
                    i++;
                    try {
                        Thread.sleep(1000);
                        Log.i("MyService>>","我走了 " + i + " 步");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MyService>>","onBind");

        return myBinder;
    }

    public class MyBinder extends Binder{
        public MyService getService() {
            return MyService.this;
        }
    }
}
