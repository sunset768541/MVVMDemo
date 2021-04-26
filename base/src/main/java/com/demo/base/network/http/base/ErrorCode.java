package com.demo.base.network.http.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建时间： 2017/8/22.
 * 编写人：wuweimin
 * 功能描述：指明该接口有统一错误码
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorCode {
}
