package utils;

import com.google.gson.Gson;

public class GsonUtils {
    public static Gson gson=new Gson();
    public static Gson getGson() {
        return gson;
    }
}
