package com.demo.base.network.http.base;

/**
 * 创建时间： 2017/11/28.
 * 编写人：wuweimin
 * 功能描述：网络请求模块初始化时调用，比如可用于启动部分特殊的service
 * 备注：不能耗时长
 */

public interface HttpApiInitializeListener {
    void onInitialize();
}
