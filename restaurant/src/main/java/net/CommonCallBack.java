package net;

import android.telecom.Call;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import utils.GsonUtils;

public abstract class CommonCallBack <T>extends StringCallback {
    Type mType;

    public CommonCallBack() {
        Class<? extends CommonCallBack> clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();
        if(genericSuperclass instanceof Class){
            throw new RuntimeException("Miss Type Params");
        }
        ParameterizedType parameterizedType= (ParameterizedType) genericSuperclass;
        mType=parameterizedType.getActualTypeArguments()[0];//获取实际类型参数
    }


    public void onError(Call call, Exception e, int id) {
        onError(e);
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            JSONObject resp=new JSONObject(response);
            int resultCode=resp.getInt("resultCode");//从服务器获取resultCode
            if(resultCode==1){//成功
                String data=resp.getString("data");
                onSuccess((T) GsonUtils.getGson().fromJson(data,mType));
            }else {//失败
                onError(new RuntimeException(resp.getString("resultMessage")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public abstract void onError(Exception e);
    public abstract void onSuccess(T response);
}
