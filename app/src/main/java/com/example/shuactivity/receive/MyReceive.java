package com.example.shuactivity.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.shuactivity.utils.ToastUtils;

public class MyReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String flag = intent.getStringExtra("flag"); //主要接受与广播信息

        assert flag != null;
        switch (flag){
            case "refresh_user_info":
                ToastUtils.showTextToast(context,"刷新用户信息");
                break;
        }
    }
}
