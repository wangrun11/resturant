package UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wangrun.restaurant.R;

import java.io.Serializable;

import bean.Product;
import config.Config;
import utils.T;

public class ProductDetailActivity extends BaseActivity {
        private Product mProduct;
        private ImageView mIvImage;
        private TextView mTvTitle,mTvDesc,mTvPrice;
        private static final String KEY_PRODUCT="key_product";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setUpToolBar();
        setTitle("详情");
        Intent intent = getIntent();//获取Intent
        if(intent!=null){
            mProduct= (Product) intent.getSerializableExtra(KEY_PRODUCT);
        }
        if(mProduct==null){
            T.showToast("参数传递错误!");
            return;
        }
        initView();
    }

    public static void launch(Context context,Product product){
        Intent intent=new Intent(context,ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT,product);
        context.startActivity(intent);

    }
    private void initView() {
        mIvImage=findViewById(R.id.id_iv_image);
        mTvTitle=findViewById(R.id.id_tv_title);
        mTvDesc=findViewById(R.id.id_tv_desc);
        mTvPrice=findViewById(R.id.id_tv_price);
        //加载图片
        Picasso.with(this)
                .load(Config.baseUrl+mProduct.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvImage);
        mTvTitle.setText(mProduct.getName());
        mTvDesc.setText(mProduct.getLabel());
        mTvPrice.setText(mProduct.getPrice()+"元/份");
    }
}