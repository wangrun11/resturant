package UI.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wangrun.restaurant.R;

import net.CommonCallBack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import UI.Activity.BaseActivity;
import UI.Activity.Vo.ProductItem;
import UI.Activity.adapter.ProductListAdapter;
import UI.Activity.view.refresh.SwipeRefresh;
import UI.Activity.view.refresh.SwipeRefreshLayout;
import bean.Order;
import bean.Product;
import biz.OrderBiz;
import biz.ProductBiz;
import okhttp3.Call;
import utils.T;

public class ProductListActivity extends BaseActivity {
        private SwipeRefreshLayout mSwipeRefreshLayout;
        private RecyclerView mRecyclerView;
        private TextView mTvCount;
        private Button mBtnPay;
        private ProductBiz mProductBiz=new ProductBiz();
        private ProductListAdapter mAdapter;
        private List<ProductItem> mDatas=new ArrayList<>();
        private int mCurrentPage;
        private float mTotalPrice;
        private int mTotalCount;
        private OrderBiz mOrderBiz=new OrderBiz();
        private Order mOrder=new Order();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setUpToolBar();
        initView();
        initEvent();
        updata();
    }
//下拉
    private void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updata();
            }
        });
//上拉
        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadmore();
            }
        });
        //点餐的价格和数量，以及立即支付
        mAdapter.setOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalCount++;
                mTvCount.setText("数量:"+mTotalCount);
                mTotalPrice+=productItem.getPrice();
                mBtnPay.setText("共计"+mTotalPrice+"立即支付");
                mOrder.addProduct(productItem);
            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalCount--;
                mTvCount.setText("数量:"+mTotalCount);
                mTotalPrice-=productItem.getPrice();
                if(mTotalCount==0){
                    mTotalPrice=0;
                }
                mBtnPay.setText("共计"+mTotalPrice+"\n"+"立即支付");
                mOrder.removeProduct(productItem);
            }
        });
        //点击支付
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTotalCount<=0){
                    T.showToast("你还没有选择菜品~");
                    return;
                }
                //如果>0,则给Order各项赋值
                mOrder.setCount(mTotalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDatas.get(0).getRestaurant());
                startLoadingProgress();
                mOrderBiz.add(mOrder, new CommonCallBack<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        stopLoadingProgress();
                        T.showToast("订单支付成功!");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                });
            }
        });
    }




    private void loadmore() {
        //TODO
        startLoadingProgress();
        mProductBiz.ListByPage(++mCurrentPage, new CommonCallBack<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mCurrentPage--;
                mSwipeRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setPullUpRefreshing(false);
                if(response.size()==0){
                    T.showToast("没有了~");
                    return;
                }
                T.showToast("又找到"+response.size()+"道菜~");
                for(Product p:response){
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }
        });
    }

    private void updata() {
        startLoadingProgress();
        mProductBiz.ListByPage(0, new CommonCallBack<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setRefreshing(false);
                mCurrentPage=0;
                mDatas.clear();
                for(Product p:response){
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
                //清空选择的数量，价格，以及立即支付
                mTotalPrice=0;
                mTotalCount=0;

                mTvCount.setText("数量:"+mTotalCount);
                mBtnPay.setText("共计"+mTotalPrice+"立即支付");
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }
        });
        //TODO
    }


    private void initView() {
        mSwipeRefreshLayout=findViewById(R.id.SwipeRefreshLayout);
        mRecyclerView=findViewById(R.id.recyclerView);
        mTvCount=findViewById(R.id.id_tv_count);
        mBtnPay=findViewById(R.id.id_btn_pay);
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLACK,Color.GREEN,Color.YELLOW);
        mAdapter=new ProductListAdapter(this,mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }
    protected void onDestroy() {
        super.onDestroy();
        mProductBiz.onDestroy();
    }
}