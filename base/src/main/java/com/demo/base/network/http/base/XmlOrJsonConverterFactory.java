package com.demo.base.network.http.base;


import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * 创建时间： 2017/8/5.
 * 编写人：wuweimin
 * 功能描述：提供给Retrofit2的Converter，负责分发json和xml的解析处理
 */

public class XmlOrJsonConverterFactory extends Converter.Factory {
    private Converter.Factory xml = SimpleXmlConverterFactory.create();
    //没有统一错误码时，使用GSON直接解析数据
    private Converter.Factory json = GsonConverterFactory.create();
    //包含统一错误码处理时使用自定义的ConverterFactory
    private Converter.Factory errorCodeJson = UnifiedErrorCodeGsonConverterFactory.create();

    public static XmlOrJsonConverterFactory create() {
        return new XmlOrJsonConverterFactory();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return json.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (annotations == null) {
            return null;
        }
        boolean hasJson = false;
        boolean hasXml = false;
        boolean hasErrorCode = false;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Json.class) {
                hasJson = true;
            } else if (annotation.annotationType() == Xml.class) {
                hasXml = true;
            } else if (annotation.annotationType() == ErrorCode.class) {
                hasErrorCode = true;
            }
        }
        if (hasJson) {
            if (hasErrorCode) {
                return errorCodeJson.responseBodyConverter(type, annotations, retrofit);
            } else {
                return json.responseBodyConverter(type, annotations, retrofit);
            }
        } else if (hasXml) {
            return xml.responseBodyConverter(type, annotations, retrofit);
        } else {
            return null;
        }
    }
}
