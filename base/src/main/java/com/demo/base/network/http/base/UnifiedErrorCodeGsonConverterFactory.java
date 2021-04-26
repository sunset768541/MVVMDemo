package com.demo.base.network.http.base;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 创建时间： 2017/8/20.
 * 编写人：wuweimin
 * 功能描述：
 */

class UnifiedErrorCodeGsonConverterFactory extends Converter.Factory {
    private final Gson gson;

    private UnifiedErrorCodeGsonConverterFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        this.gson = gson;
    }

    public static UnifiedErrorCodeGsonConverterFactory create() {
        return create(new Gson());
    }

    public static UnifiedErrorCodeGsonConverterFactory create(Gson gson) {
        return new UnifiedErrorCodeGsonConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new UnifiedErrorCodeGsonResponseBodyConverter<>(gson, type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new UnifiedErrorCodeGsonRequestBodyConverter<>(gson, adapter);
    }

}
