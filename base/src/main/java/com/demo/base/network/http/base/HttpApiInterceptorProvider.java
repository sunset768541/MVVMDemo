package com.demo.base.network.http.base;


import okhttp3.Interceptor;

/**
 * 功能描述：Http请求信息的自定义拦截器
 */

public interface HttpApiInterceptorProvider {
    Interceptor getInterceptor();
}
