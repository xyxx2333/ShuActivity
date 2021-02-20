package com.example.shuactivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.print.PageRange;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shuactivity.domain.User;
import com.example.shuactivity.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;



/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_email)
    EditText etUserEmail;
    @BindView(R.id.et_user_password_again)
    EditText etUserPasswordAgain;
    @BindView(R.id.tv_register_btn)
    TextView tvRegisterBtn;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;

    @Override
    public void initView() {
        contentView(R.layout.register_page_activity);
        ButterKnife.bind(this);
        setTitle("注册");
        showBackImage();
        tvRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUp();

            }
        });
    }

    /**
     * 账号密码注册
     */
    private void signUp() {
        String userName = etUserName.getText().toString().trim();   //trim()去前后空格
        String userPhone = etUserPhone.getText().toString().trim();   //trim()去前后空格
        String userEmail = etUserEmail.getText().toString().trim();   //trim()去前后空格
        String userPassword = etUserPassword.getText().toString().trim();   //trim()去前后空格
        String userPasswordAgain = etUserPasswordAgain.getText().toString().trim();   //trim()去前后空格

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("注册");
        progressDialog.setMessage("注册中稍等5秒...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(10000);
                    if (userName.isEmpty()){
                        ToastUtils.showTextToast(context,"请输入用户名称");
                        return;
                    }
                    if (userPhone.isEmpty()){
                        ToastUtils.showTextToast(context,"请输入用户电话号码");
                        return;
                    }
                    if (userEmail.isEmpty()){
                        ToastUtils.showTextToast(context,"请输入用户邮箱");
                        return;
                    }
                    if (userPassword.isEmpty()){
                        ToastUtils.showTextToast(context,"请输入用户密码");
                        return;
                    }
                    if (!userPassword.equals(userPasswordAgain)){
                        ToastUtils.showTextToast(context,"两次密码不一致，请重新输入");
                        return;
                    }
                    final User user = new User();
                    user.setUsername(userName );
                    user.setPassword(userPassword);
                    user.setEmail(userEmail);
                    user.setMobilePhoneNumber(userPhone);
                    user.setAge(18);
                    user.setGender(0);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
//                    Snackbar.make(view, "注册成功", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(context,LoginActivity.class);    //注册成功后跳登录页面
                                startActivity(intent);
                                ToastUtils.showTextToast(context,"注册成功");
                            } else {
//                    Snackbar.make(view, "尚未失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                                ToastUtils.showTextToast(context,e.getMessage());
                            }
                        }
                    });
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        };
        thread.start();

    }

}
