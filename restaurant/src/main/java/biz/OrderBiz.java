package biz;

import com.zhy.http.okhttp.OkHttpUtils;

import net.CommonCallBack;

import java.util.List;
import java.util.Map;

import bean.Order;
import bean.Product;
import config.Config;

public class OrderBiz {
    //分页方法
    public  void listByPage(int currentPage,CommonCallBack<List<Order>> callBack){
        OkHttpUtils.post()
                   .url(Config.baseUrl+"order_find")
                   .tag(this)
                   .addParams("currentPage",currentPage+"")
                   .build()
                   .execute(callBack);
    }

    public void add(Order order,CommonCallBack<String> callBack){
        Map<Product, Integer> productMap = order.productMap;
        StringBuilder sb=new StringBuilder();
        //遍历
        for(Product p:productMap.keySet()){
            sb.append(p.getId()+"_"+productMap.get(p));
            sb.append("|");
        }
        sb=sb.deleteCharAt(sb.length()-1);
        OkHttpUtils.post()
                    .url(Config.baseUrl+"order_add")
                    .addParams("res_id",order.getRestaurant().getId()+"")
                    .addParams("product_str",sb.toString())
                    .addParams("count",order.getCount()+"")
                    .addParams("price",order.getPrice()+"")
                    .tag(this)
                    .build()
                    .execute(callBack);
    }
    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
