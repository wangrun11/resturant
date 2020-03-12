package UI.Activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wangrun.restaurant.R;

import java.util.List;

import UI.Activity.ProductDetailActivity;
import UI.Activity.Vo.ProductItem;
import config.Config;
import utils.T;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductItemViewHolder> {
    private Context mContext;
    private List<ProductItem> mProductItems;
    private LayoutInflater mInflater;

    public ProductListAdapter(Context context, List<ProductItem> productItems) {
        mContext=context;
        mProductItems=productItems;
        mInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_product_list, parent, false);
        return new ProductItemViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        ProductItem productItem=mProductItems.get(position);
        Picasso.with(mContext)
                .load(Config.baseUrl+productItem.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        holder.mTvName.setText(productItem.getName());
        holder.mTvPrice.setText(productItem.getPrice()+"元/份");
        holder.mTvLabel.setText(productItem.getLabel());
        holder.mCount.setText(productItem.count+"");
    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }
    //回调接口
    public interface OnProductListener{
        void onProductAdd(ProductItem productItem);
        void onProductSub(ProductItem productItem);
    }
    private OnProductListener mOnProductListener;
    public void setOnProductListener(OnProductListener onProductListener){
        mOnProductListener=onProductListener;
    }

    class ProductItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mIvImage,mSub,mAdd;
        public TextView mTvName,mTvLabel,mTvPrice,mCount;
        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage=itemView.findViewById(R.id.id_iv_image);
            mSub=itemView.findViewById(R.id.id_iv_sub);
            mAdd=itemView.findViewById(R.id.id_iv_add);
            mTvName=itemView.findViewById(R.id.id_tv_name);
            mTvLabel=itemView.findViewById(R.id.id_tv_label);
            mTvPrice=itemView.findViewById(R.id.id_tv_price);
            mCount=itemView.findViewById(R.id.id_tv_count);
            //子项点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    ProductDetailActivity.launch(mContext,mProductItems.get(getLayoutPosition()));
                }
            });

            //增加数量
            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(position);
                    productItem.count+=1;
                    mCount.setText(productItem.count+"");
                    //回调到Activity
                    if(mOnProductListener!=null){
                        mOnProductListener.onProductAdd(productItem);
                    }
                }
            });

            //减少数量
            mSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(position);
                    if(productItem.count<=0){
                        T.showToast("已经为0了");
                        return;
                    }
                    productItem.count-=1;
                    mCount.setText(productItem.count+"");
                    //回调到Activity
                    if(mOnProductListener!=null){
                        mOnProductListener.onProductSub(productItem);
                    }
                }
            });

        }


    }
}
