package com.oocl.johngao.smartcr.ToolsClass;

import android.content.Context;
import android.util.Log;

import com.oocl.johngao.smartcr.WebInterface.GetToken_Interface;
import com.qiniu.android.storage.UploadManager;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by johngao on 18/1/3.
 */

public class WebLab {

    private static String TAG = "Token request";

    private Context mContext;
    private static WebLab sWebLab;
    private String mToken;
    private UploadManager mUploadManager;
    private WebLab(Context context){
        mContext = context.getApplicationContext();
        WebUtis.getToken(mContext);
        mUploadManager = new UploadManager(WebUtis.getConfig());
    }
    public static WebLab get(Context context){
        if (sWebLab == null){
            synchronized (DataLab.class){
                if (sWebLab == null){
                    sWebLab = new WebLab(context);
                }
            }
        }
        return sWebLab;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getToken() {
        return mToken;
    }

    public UploadManager getUploadManager() {
        return mUploadManager;
    }
}
