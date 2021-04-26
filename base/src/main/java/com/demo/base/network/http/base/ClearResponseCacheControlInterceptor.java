package com.demo.base.network.http.base;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 创建时间： 2017/8/8.
 * 编写人：wuweimin
 * 功能描述：okhttp拦截器，修改响应头字段，使其可被缓存
 */

public class ClearResponseCacheControlInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        //在响应是执行
        return originalResponse.newBuilder()
                .removeHeader("Cache-Control")
                .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .build();
    }
}
