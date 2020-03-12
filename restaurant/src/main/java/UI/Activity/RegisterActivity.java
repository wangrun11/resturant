package UI.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wangrun.restaurant.R;

import net.CommonCallBack;

import bean.User;
import biz.UserBiz;
import okhttp3.Call;
import userinfoholder.UserInfoHolder;
import utils.T;

public class RegisterActivity extends BaseActivity {
private EditText mRegister_phoneNumber_edt,mRegister_psd_edt,mRegister_psd2_edt;
private Button mOk_btn;
private UserBiz mUserBiz=new UserBiz();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpToolBar();
        setTitle("手机注册");
        initView();
        initEvent();
    }

    private void initEvent() {
        mOk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=mRegister_phoneNumber_edt.getText().toString();
                String password=mRegister_psd_edt.getText().toString();
                String repassword=mRegister_psd2_edt.getText().toString();
                //验空
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    T.showToast("账号或者密码不能为空");
                    return;//阻止进程往下进行
                }
                //校验两次密码是否一致
                if(!password.equals(repassword)){
                    T.showToast("两次密码不一致！");
                    return;
                }
                //注意，所有的进度框都可以写到BaseActivity中，所以本类要继承BaseActivity
                startLoadingProgress();//开始登录的进度框
                //点击按钮调用mUserBiz.login方法
                mUserBiz.register(username, password, new CommonCallBack<User>() {
                    
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                        public void onError(Exception e) {
                            stopLoadingProgress();//结束进度框
                            T.showToast(e.getMessage());
                        }


                    @Override
                    public void onSuccess(User response) {
                        stopLoadingProgress();//结束进度框
                        T.showToast("注册成功,用户名为："+response.getUsername());
                        //注册成功后跳转到登录页，并携带账户密码设置到登录页输入框
                        LoginActivity.launch(RegisterActivity.this,response.getUsername(),response.getPassword());
                            finish();
                    }

                });

            }
        });
    }

    private void initView() {
    mRegister_phoneNumber_edt=findViewById(R.id.register_phoneNumber_edt);
    mRegister_psd_edt=findViewById(R.id.register_psd_edt);
    mRegister_psd2_edt=findViewById(R.id.register_psd2_edt);
    mOk_btn=findViewById(R.id.ok_btn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestory();
    }
}