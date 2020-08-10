package me.zhouzhuo810.zznote.api;

import me.zhouzhuo810.zznote.Controller;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;



/**
 * Api调用
 */
public class Api {

    public static String SERVER_IP0 = null;
    private static Api0 api0;

    public static Api0 getApi0() {
        if (api0 == null) {
            synchronized (Api.class) {
                if (api0 == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .cache(null)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .build();

                    CookieHandler.setDefault(getCookieManager());
                    Retrofit retrofit = new Retrofit.Builder()
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())//添加 json 转换器
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加 RxJava 适配器
                            .baseUrl(SERVER_IP0)
                            .build();
                    api0 = retrofit.create(Api0.class);
                }
            }
        }
        return api0;
    }


    public static void updateApi(String ip) {
        SERVER_IP0 = "http://" + ip + ":8080/desktop/";
        Preferences prefs = Preferences.userNodeForPackage(Controller.class);
        prefs.put("lastIp", ip);
        api0 = null;
        getApi0();
    }

    private static CookieManager getCookieManager() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return cookieManager;
    }
}