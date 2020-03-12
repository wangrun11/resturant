package UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.wangrun.restaurant.R;

public class SplashActivity extends AppCompatActivity  {
private Button btn_skip;
private Handler handler=new Handler(){
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        //接收消息,更改UI
        switch (msg.what){
            case 1:
                int value=msg.arg1;
                btn_skip.setText(String.valueOf(value--)+"跳过");
                if(value>=0){
                    Message message=Message.obtain();
                    message.what=1;
                    message.arg1=value;
                    handler.sendMessageDelayed(message,1000);
                }
                break;
        }
    }
};
private Runnable mRunnableToLogin=new Runnable() {
    @Override
    public void run() {
        toLogin();
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initEvent();
        postMessage();
        handler.postDelayed(mRunnableToLogin,7000);
    }

    private void postMessage() {
        //发送消息
        Message message=Message.obtain();
        message.what=1;
        message.arg1=5;
        handler.sendMessageDelayed(message,1000);
    }

    private void initEvent() {
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(mRunnableToLogin);//解决点击跳过按钮后还要再启动一次Activity
                toLogin();
            }
        });
    }

    private void initView() {
        btn_skip=findViewById(R.id.btn_skip);
    }

    //跳转注册页面的方法
    private void toLogin(){
        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(mRunnableToLogin);//避免内存泄漏
    }
}