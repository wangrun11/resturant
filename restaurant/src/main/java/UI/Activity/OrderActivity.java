package UI.Activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wangrun.restaurant.R;

import net.CommonCallBack;

import java.util.ArrayList;
import java.util.List;

import UI.Activity.adapter.Orderadapter;
import UI.Activity.view.CircleTransform;
import UI.Activity.view.refresh.SwipeRefresh;
import UI.Activity.view.refresh.SwipeRefreshLayout;
import bean.Order;
import bean.User;
import biz.OrderBiz;
import okhttp3.Call;
import userinfoholder.UserInfoHolder;
import utils.T;

public class OrderActivity extends BaseActivity {
private Button mBtnOrder;
private ImageView mIv;
private SwipeRefreshLayout mSwipeRefreshLayout;
private TextView mUsername;
private RecyclerView mRecyclerView;
private Orderadapter mAdapter;
private List<Order> mDatas=new ArrayList<>();
private OrderBiz mOrderBiz=new OrderBiz();
private int mcurrentPage=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        updata();
        initView();
        initEvent();
    }



    private void initEvent() {
        //下拉事件，更新列表
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updata();
            }
        });
        //上拉事件，加载更多
        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadmore();
            }
        });
        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderActivity.this, ProductListActivity.class);
                startActivityForResult(intent,1001);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001&&resultCode==RESULT_OK){
            updata();
        }
    }


    private void loadmore() {
        startLoadingProgress();
        mOrderBiz.listByPage(++mcurrentPage, new CommonCallBack<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mcurrentPage--;
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                if(response.size()==0){
                    T.showToast("没有的订单了~");
                    mSwipeRefreshLayout.setPullUpRefreshing(false);//刷新关闭
                    return;
                }else {
                    T.showToast("订单加载完成~");
                    mDatas.addAll(response);
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setPullUpRefreshing(false);//刷新关闭(注意是setPullUpRefreshing)

                }

            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }
        });

    }
    //用户退出时，APP依然在后台运行
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            try{
                Intent home=new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            }catch (Exception e){

            }

        }
            return super.onKeyDown(keyCode, event);
    }

    private void updata() {
        startLoadingProgress();
        mOrderBiz.listByPage(0, new CommonCallBack<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                T.showToast("订单更新成功~");
                mDatas.clear();
                mDatas.addAll(response);
                mAdapter.notifyDataSetChanged();
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }
        });
    }

    private void initView() {
        mBtnOrder=findViewById(R.id.btn_order);
        mIv=findViewById(R.id.user_image);
        mUsername=findViewById(R.id.user_name);
        mRecyclerView=findViewById(R.id.recyclerView);
        mSwipeRefreshLayout=findViewById(R.id.SwipeRefreshLayout);
        User user= UserInfoHolder.getInstance().getUser();
        if(user!=null){
            mUsername.setText(user.getUsername());
        }else {
            toLoginActivity();
            finish();
            return;
        }
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLACK,Color.GREEN,Color.YELLOW);
        mAdapter=new Orderadapter(mDatas,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        Picasso.with(this)
                .load(R.drawable.icon)
                .placeholder(R.drawable.pictures_no)
                .transform(new CircleTransform())
                .into(mIv);
    }

    protected void onDestroy(){
        mOrderBiz.onDestroy();
    }
}