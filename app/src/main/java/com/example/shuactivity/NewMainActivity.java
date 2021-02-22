package com.example.shuactivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.shuactivity.adapter.NewsAdapyer;
import com.example.shuactivity.adapter.ViewPagerAdapter;
import com.example.shuactivity.domain.NewsResultData;
import com.example.shuactivity.fragment.JokeFragment;
import com.example.shuactivity.fragment.MineFragment;
import com.example.shuactivity.fragment.NewsFragment;
import com.example.shuactivity.fragment.ShopFragment;
import com.example.shuactivity.receive.MyReceive;
import com.example.shuactivity.service.MyService;
import com.example.shuactivity.tools.HttpTools;
import com.example.shuactivity.tools.JsonTool;

import com.example.shuactivity.utils.ToastUtils;
import com.example.shuactivity.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewMainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.first_radio)
    RadioButton firstRadio;
    @BindView(R.id.type_radio)
    RadioButton typeRadio;
    @BindView(R.id.shopping_car_radio)
    RadioButton shoppingCarRadio;
    @BindView(R.id.mine_radio)
    RadioButton mineRadio;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    List<Fragment> fragmentList = new ArrayList<>();
    ViewPagerAdapter viewPagerAdapter;
    private ShopFragment shopFragment;
    private JokeFragment jokeFragment;
    private MineFragment mineFragment;
    private NewsFragment newsFragment;
    private MyReceive mainReceive;
    public static String MAIN_BROADCAST = "com.shu.main_broadcast";  //广播名称
    private ServiceConnection serviceConnection;

    private MyService myService;
    @Override
    public void initView() {
        contentView(R.layout.new_main_activity_daluzy);
        ButterKnife.bind(this);

        //启动Service
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("MyService>>", "连接成功：" + name);
                MyService.MyBinder myBinder = (MyService.MyBinder) service;
                myService = myBinder.getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("MyService>>","断开连接");
            }
        };
//        bindService(intent,serviceConnection,BIND_AUTO_CREATE);


        initData();
        setTitle("优惠券商城");
        radioGroup.check(R.id.first_radio);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MAIN_BROADCAST);
        mainReceive = new MyReceive();
        registerReceiver(mainReceive,intentFilter); //注册


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        radioGroup.check(R.id.first_radio);
                        break;
                    case 1:
                        radioGroup.check(R.id.type_radio);
                        break;
                    case 2:
                        radioGroup.check(R.id.shopping_car_radio);
                        break;
                    case 3:
                        radioGroup.check(R.id.mine_radio);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.first_radio:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.type_radio:
                        Intent intent1 = new Intent(MyService.MYSERVICE_BC);
                        intent1.putExtra("flag","myservice_bc");
                        sendBroadcast(intent1);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.shopping_car_radio:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.mine_radio:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });
    }


//    class MainReceive extends BroadcastReceiver{
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String flag = intent.getStringExtra("flag"); //主要接受与广播信息
//
//            assert flag != null;
//            switch (flag){
//                case "refresh_user_info":
//                    ToastUtils.showTextToast(context,"刷新用户信息");
//                    break;
//            }
//        }
//    }


    private void initData() {
        shopFragment = new ShopFragment();
        newsFragment = new NewsFragment();
        jokeFragment = new JokeFragment();
        mineFragment = new MineFragment();
        fragmentList.add(shopFragment);
        fragmentList.add(newsFragment);
        fragmentList.add(jokeFragment);
        fragmentList.add(mineFragment);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(viewPagerAdapter); //viewPager绑定Adapter
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mainReceive);        //注销
        Intent intent = new Intent(NewMainActivity.this, MyService.class);
        unbindService(serviceConnection);
    }
}
