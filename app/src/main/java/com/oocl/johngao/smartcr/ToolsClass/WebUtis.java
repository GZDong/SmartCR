package com.oocl.johngao.smartcr.ToolsClass;

import android.content.Context;
import android.util.Log;

import com.oocl.johngao.smartcr.Const.Const;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by johngao on 18/1/3.
 */

public class WebUtis {

    public static String TAG = "WebUtis";

    public static String getToken(final Context context){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    Request request = new Request.Builder()
                            .url("http://10.222.225.55:3000/qiniu/getUploadToken")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    if (response.isSuccessful()) {
                        Log.e(TAG, "run: " + responseData);
                        WebLab webLab = WebLab.get(context);
                        webLab.setToken(responseData);
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: Error" + e);

                }
            }
        }).start();

        return  " ";
    }

    public static Configuration getConfig(){
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .recorder(null)           // recorder分片上传时，已上传片记录器。默认null
                .recorder(null, null)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();

        return config;
    }
}
