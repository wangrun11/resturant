package UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.wangrun.restaurant.R;

import java.util.List;

import bean.Order;
import bean.Product;
import config.Config;
import utils.T;

public class OrderDetailActivity extends BaseActivity {
    private Order mOrder;
    private ImageView mIvImage;
    private TextView mTvTitle,mTvDesc,mTvPrice;
    private static final String KEY_ORDER="key_order";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setUpToolBar();
        setTitle("订单详情");
        Intent intent = getIntent();//获取Intent
        if(intent!=null){
            mOrder= (Order) intent.getSerializableExtra(KEY_ORDER);
        }
        if(mOrder==null){
            T.showToast("参数错误!");
            finish();
            return;
        }
        initView();
    }

    public static void launch(Context context, Order order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(KEY_ORDER, order);
        context.startActivity(intent);
    }
    private void initView() {
        mIvImage=findViewById(R.id.id_iv_image);
        mTvTitle=findViewById(R.id.id_tv_title);
        mTvDesc=findViewById(R.id.id_tv_desc);
        mTvPrice=findViewById(R.id.id_tv_price);
        //加载图片
        Picasso.with(this)
                .load(Config.baseUrl+mOrder.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvImage);
        mTvTitle.setText("香满居餐厅");
        List<Order.ProductVo> ps = mOrder.getPs();
        StringBuilder sb=new StringBuilder();
        for(Order.ProductVo vo:ps){
            sb.append(vo.product.getName())
                    .append("*")
                    .append(vo.count)
                    .append("\n");
        }
        mTvDesc.setText(sb.toString());
        mTvPrice.setText("共消费"+mOrder.getPrice()+"元");
    }
}
