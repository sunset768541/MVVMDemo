package com.demo.base.network.http.base;
import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建时间： 2017/8/8.
 * 编写人：wuweimin
 * 功能描述：okhttp3的拦截器，将请求头改成只获取网络数据
 */

public class OnlyNetInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        return chain.proceed(request);
    }
}
