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

import UI.Activity.OrderDetailActivity;
import bean.Order;
import config.Config;

public class Orderadapter extends RecyclerView.Adapter<Orderadapter.OrderItemViewHolder> {

private List<Order> mDatas;
private Context mContext;
private LayoutInflater mInflater;

    public Orderadapter(List<Order> Datas, Context context) {
        this.mDatas = Datas;
        this.mContext = context;
        mInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_order_list, parent, false);
        return new OrderItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Order order=mDatas.get(position);
        Picasso.with(mContext)
                .load(Config.baseUrl+order.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        if(order.getPs().size()>0){
            holder.mTvLabel.setText(order.getPs().get(0).product.getName()+"等"+order.getCount()+"件商品");
        }else {
            holder.mTvLabel.setText("未消费，快去吃点什么吧~~");
        }
        holder.mTvName.setText("香满居餐厅");
        holder.mTvPrice.setText("共消费"+order.getPrice()+"元");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mIvImage;
        public TextView mTvName,mTvLabel,mTvPrice;
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    OrderDetailActivity.launch(mContext,mDatas.get(getLayoutPosition()));
                }
            });
            mIvImage=itemView.findViewById(R.id.id_iv_image);
            mTvName=itemView.findViewById(R.id.id_tv_name);
            mTvLabel=itemView.findViewById(R.id.id_tv_label);
            mTvPrice=itemView.findViewById(R.id.id_tv_price);
        }
    }
}
