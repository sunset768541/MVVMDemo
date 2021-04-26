package com.demo.base.network.http.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建时间： 2017/8/5.
 * 编写人：wuweimin
 * 功能描述：配合Retrofit2，标记请求接口返回的格式是XML
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Xml {
}
