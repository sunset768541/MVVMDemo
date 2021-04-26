package com.demo.base.network.http.base;

import java.util.Map;

/**
 * 创建时间： 2017/11/28.
 * 编写人：wuweimin
 * 功能描述：Http请求头信息的提供者
 */

public interface HttpApiHeadersProvider {
    Map<String, String> getHttpHeaders();
}
