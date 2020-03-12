package UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wangrun.restaurant.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import net.CommonCallBack;
import bean.User;
import biz.UserBiz;
import okhttp3.Call;
import okhttp3.CookieJar;
import userinfoholder.UserInfoHolder;
import utils.T;

public class LoginActivity extends BaseActivity {
private EditText mAccEdt,mPsdEdt;
private Button mLoadBtn;
private TextView mRegisterTxt;
private UserBiz mUserBiz=new UserBiz();//获取UserBiz对象，使用其调用登录方法
private static final String KEY_USERNAME="key_username";
private static final String KEY_PASSWORD="key_password";

    @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar =(CookieJarImpl)OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();//初始化视图
        initEvent();//事件
        initIntent(getIntent());
    }

    private void initEvent() {
        mLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=mAccEdt.getText().toString();
                String password=mPsdEdt.getText().toString();
                //验空
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    T.showToast("账号或者密码不能为空");
                    return;//阻止进程往下进行
                }
                //注意，所有的进度框都可以写到BaseActivity中，所以本类要继承BaseActivity
                startLoadingProgress();//开始登录的进度框
                //点击按钮调用mUserBiz.login方法
                mUserBiz.login(username, password, new CommonCallBack<User>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        stopLoadingProgress();//结束进度框
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onSuccess(User response) {
                        stopLoadingProgress();//结束进度框
                        T.showToast("登录成功!");
                        UserInfoHolder.getInstance().setUser(response);
                        toOrderActivity();
                    }

                });

            }
        });
        mRegisterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });
    }
    public static void launch(Context context, String username, String password) {
            Intent intent=new Intent(context,LoginActivity.class);
            //为防止出现两个登录页,消除栈顶
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(KEY_USERNAME,username);
            intent.putExtra(KEY_PASSWORD,password);
            context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        if(intent==null){
            return;
        }
        String username=intent.getStringExtra(KEY_USERNAME);
        String password=intent.getStringExtra(KEY_PASSWORD);
        //校验
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            return;
        }
        mAccEdt.setText(username);
        mPsdEdt.setText(password);
    }

    private void toRegisterActivity() {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    private void toOrderActivity() {
        startActivity(new Intent(LoginActivity.this,OrderActivity.class));

    }

    private void initView() {
        mAccEdt=findViewById(R.id.acc_edt);
        mPsdEdt=findViewById(R.id.psd_edt);
        mLoadBtn=findViewById(R.id.load_btn);
        mRegisterTxt=findViewById(R.id.register);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestory();
    }
}