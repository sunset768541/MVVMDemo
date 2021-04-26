package com.demo.module_news

import android.content.Context
import com.demo.base.network.http.api.HttpApi

fun getApi(context: Context):HttpApi{
    var api = HttpApi()
    api.initConfig(
        true,
        false,
        "http://service-o5ikp40z-1255468759.ap-shanghai.apigateway.myqcloud.com/",
        context.getCacheDir(),
        null,
        RequestHeader(),

        null
    )
    return api
}