package utils;

import android.content.Context;
import android.widget.Toast;

public class T {

    private static Toast mToast;
    //showToast方法
    public static void showToast(String content){
        mToast.setText(content);
        mToast.show();
    }
    public static void init(Context context){
        mToast= Toast.makeText(context,"",Toast.LENGTH_SHORT);
    }
}
