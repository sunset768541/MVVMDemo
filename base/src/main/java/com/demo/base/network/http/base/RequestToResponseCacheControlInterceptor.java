package com.demo.base.network.http.base;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 创建时间： 2017/8/8.
 * 编写人：wuweimin
 * 功能描述：okhttp拦截器，修改响应头，使其短暂缓存
 */

public class RequestToResponseCacheControlInterceptor implements Interceptor {
    private static final int maxAge = 60;//60s缓存时间

    static public RequestToResponseCacheControlInterceptor newInstance() {
        return new RequestToResponseCacheControlInterceptor();
    }

    private RequestToResponseCacheControlInterceptor() {
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        //在响应时执行
        String cacheControl = originalResponse.request().cacheControl().toString();
        if (TextUtils.isEmpty(cacheControl)) {
            cacheControl = "max-age=" + maxAge;
        }
        return originalResponse.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .build();
    }
}
