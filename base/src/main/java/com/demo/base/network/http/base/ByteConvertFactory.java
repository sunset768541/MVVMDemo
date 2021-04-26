package com.demo.base.network.http.base;


import androidx.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * ************************************************ <br>
 * 创建人员: peixiaoyong <br>
 * 文件描述: 分发解析byte[]的Converter，<br>
 * 修改时间: 2017/8/25 10:36 <br>
 * 修改历史: 2017/8/25 1.00 初始版本
 * ************************************************
 */
public class ByteConvertFactory extends Converter.Factory {

    public static ByteConvertFactory create() {
        return new ByteConvertFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ByteConvert();
    }

    private static class ByteConvert implements Converter<ResponseBody, byte[]> {
        @Override
        public byte[] convert(@NonNull ResponseBody value) throws IOException {
            return value.bytes();
        }
    }
}