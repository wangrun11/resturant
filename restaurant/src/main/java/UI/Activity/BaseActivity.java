package UI.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wangrun.restaurant.R;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mLoadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog=new ProgressDialog(this);
        mLoadingDialog.setMessage("正在加载中...");
    }
    protected void setUpToolBar() {
        Toolbar toolbar=findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
//开始进度框和结束进度框方法
    protected void stopLoadingProgress() {
        //结束进度是取消进度框
        if(mLoadingDialog!=null&&mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }

    }
    protected void startLoadingProgress() {
        mLoadingDialog.show();//开始进度时显示进度框
    }
//确保销毁时mLoadingDialog也销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoadingProgress();
        mLoadingDialog=null;
    }
    protected void toLoginActivity() {
        startActivity(new Intent(this,LoginActivity.class));
    }
}
