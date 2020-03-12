package biz;

import com.zhy.http.okhttp.OkHttpUtils;

import net.CommonCallBack;

import bean.User;
import config.Config;

public class UserBiz {
    //登录方法,参数为传入用户名，密码，CommonCallBack
    public void login(String username, String password, CommonCallBack<User> commonCallBack){
        OkHttpUtils
                .post()
                .url(Config.baseUrl+"user_login")
                .tag(this)
                .addParams("username",username)
                .addParams("password",password)
                .build()
                .execute(commonCallBack);
    }
    //注册方法
    public void register(String username, String password, CommonCallBack<User> commonCallBack){
        OkHttpUtils
                .post()
                .url(Config.baseUrl+"user_register")
                .tag(this)
                .addParams("username",username)
                .addParams("password",password)
                .build()
                .execute(commonCallBack);
    }
    public void onDestory(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
