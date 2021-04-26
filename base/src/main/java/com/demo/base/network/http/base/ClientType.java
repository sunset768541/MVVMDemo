package com.demo.base.network.http.base;

/**
 * 创建时间： 2017/8/8.
 * 编写人：wuweimin
 * 功能描述: 不同缓存策略的okhttp cilent分类
 */

public enum ClientType {
    //优先请求网络，没有网络返回缓存
    firstNet,

    //优先返回缓存，用于提升网络响应，默认缓存60s,60s后更新缓存.
    // 可在网络请求接口上用@Headers("Cache-Control: max-age=25")注解修改缓存时间，单位秒
    firstCache,

    //只返回网络请求的数据
    onlyNet
}
