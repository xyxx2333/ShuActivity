package com.example.shuactivity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shuactivity.LoginActivity;
import com.example.shuactivity.NewMainActivity;
import com.example.shuactivity.R;
import com.example.shuactivity.RegisterActivity;
import com.example.shuactivity.contants.Constants;
import com.example.shuactivity.receive.MyReceive;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsPidGenerateRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsPidGenerateResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineFragment extends Fragment {
    @BindView(R.id.tv_mine)
    TextView tvMine;
    private Context context;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.mine_fragment_daluzy, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        tvMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(NewMainActivity.MAIN_BROADCAST);        //静态广播的action（引用NewMainActivity）
//                intent.setAction("com.shu.ShuActivity.firstBroadcast");        //静态广播的action（AndroidManifest.xml）
//                intent.setClassName(context, "com.example.shuactivity.receive.MyReceive");        //静态广播发送广播时，要注意setClassName为自定义Receive的类。这里把MyReceive传过来,绝对路径
                intent.putExtra("flag","refresh_user_info");
                context.sendOrderedBroadcast(intent,null);          //发送广播
//                createGenerate();
                //发送广播
//                Intent intent = new Intent();
//                intent.setAction(NewMainActivity.MAIN_BROADCAST);
////                intent.setAction("com.daluzy.daluzy_project.daluzy_project.firstBroadcast");
////                intent.setClassName(context, "com.daluzy.daluzy_project.daluzy_project.receive.MyReceive");
//                intent.putExtra("flag","refresh_user_info");
//                context.sendOrderedBroadcast(intent,null);

//                Intent i = new Intent(context, LoginActivity.class);
//                startActivity(i);

                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);

            }
        });
        return view;
    }

//    /**
//     * 生成推广位
//     */
//    private void createGenerate() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String clientId = Constants.PDD_CLIENT_ID;
//                String clientSecret = Constants.PDD_CLIENT_SECRET;
//                PopClient client = new PopHttpClient(clientId, clientSecret);
//
//                PddDdkGoodsPidGenerateRequest request = new PddDdkGoodsPidGenerateRequest();
//                request.setNumber(10L);
//                List<String> pIdNameList = new ArrayList<String>();
//                for (int i=0; i<10;i++){
//                    pIdNameList.add("qinC"+ i);
//                }
//                request.setPIdNameList(pIdNameList);
//                PddDdkGoodsPidGenerateResponse response = null;
//                try {
//                    response = client.syncInvoke(request);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String data = JsonUtil.transferToJson(response);
//                System.out.println(JsonUtil.transferToJson(response));
//            }
//        }).start();
//
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
