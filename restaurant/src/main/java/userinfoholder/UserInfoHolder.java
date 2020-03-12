package userinfoholder;

import android.text.TextUtils;

import bean.User;
import utils.SPUtils;

public class  UserInfoHolder {
    //用户信息保存类
    public static UserInfoHolder mInstance=new UserInfoHolder();
    private User mUser;
    private static final String KEY_USERNAME="key_username";
    private static final String KEY_PASSWORD="key_password";
    public static UserInfoHolder getInstance() {
        return mInstance;
    }
    //存账号密码
    public void setUser(User user) {
        mUser = user;
        if(user!=null){
            SPUtils.getInstance().put(KEY_USERNAME,user.getUsername());
            SPUtils.getInstance().put(KEY_PASSWORD,user.getPassword());
        }
    }
    //取账号密码
    public User getUser(){
        User u=mUser;
        if(u==null){
            String username= (String) SPUtils.getInstance().get(KEY_USERNAME,"");
            String password= (String) SPUtils.getInstance().get(KEY_PASSWORD,"");
            if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
                u=new User();
                u.setUsername(username);
                u.setPassword(password);
            }
        }
        //如果u!==null,则mUser=u;
        mUser=u;
        return u;
    }
}
