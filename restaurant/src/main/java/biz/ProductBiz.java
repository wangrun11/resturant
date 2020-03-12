package biz;

import com.zhy.http.okhttp.OkHttpUtils;

import net.CommonCallBack;

import java.util.List;

import bean.Product;
import config.Config;

public class ProductBiz {
public void ListByPage(int currentPage, CommonCallBack<List<Product>> commonCallBack){
    OkHttpUtils.post()
               .url(Config.baseUrl+"product_find")
               .tag(this)
               .addParams("currentPage",currentPage+"")
               .build()
               .execute(commonCallBack);
                }

    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
