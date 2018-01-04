package com.oocl.johngao.smartcr.WebInterface;

import io.reactivex.Observable;
import retrofit2.http.GET;


/**
 * Created by johngao on 18/1/3.
 */

public interface GetToken_Interface {
    @GET("qiniu/getUploadToken")
    Observable<String> getCall();
}
