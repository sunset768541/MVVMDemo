package com.demo.base.network.http.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建时间： 2017/8/5.
 * 编写人：wuweimin
 * 功能描述：okhttp的拦截器，用于添加全局的header
 */

public class HeaderInterceptor implements Interceptor {
    private Map<String, String> headerMap;

    static public HeaderInterceptor newInstance(@Nullable Map<String, String> headerMap) {
        HeaderInterceptor interceptor = new HeaderInterceptor();
        interceptor.headerMap = headerMap;
        return interceptor;
    }

    private HeaderInterceptor() {
    }

    public Map<String, String> getHeaders() {  //获取请求头
        return headerMap;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                if (originalRequest.header(entry.getKey()) == null) {
                    requestBuilder.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
        requestBuilder.method(originalRequest.method(), originalRequest.body());
        return chain.proceed(requestBuilder.build());
    }
}
