package com.demo.base.network.http.base;

import android.content.Context;

import androidx.annotation.NonNull;


import com.demo.base.tool.data.android.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建时间： 2017/8/8.
 * 编写人：wuweimin
 * 功能描述：okhttp拦截器，指定有网络时请求网络，无网络情况下请求缓存
 */

public class OfflineRequestCacheInterceptor implements Interceptor {
    private Context mContext;

    static public OfflineRequestCacheInterceptor newInstance(Context context) {
        return new OfflineRequestCacheInterceptor(context);
    }

    private OfflineRequestCacheInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        //在请求时执行,在应用拦截器里执行才有效
        if (!NetworkUtil.isConnected(mContext)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        return chain.proceed(request);
    }
}
