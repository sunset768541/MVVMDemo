package com.demo.base.network.http.base;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 创建时间： 2017/8/21.
 * 编写人：wuweimin
 * 功能描述：
 */

class UnifiedErrorCodeGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson mGson;
    private final Type mType;

    UnifiedErrorCodeGsonResponseBodyConverter(Gson gson, Type type) {
        mGson = gson;
        this.mType = type;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        String response = value.string();
        Type type = new ParameterizedTypeImpl(UnifiedErrorCodeResult.class, new Type[]{mType});
        UnifiedErrorCodeResult<T> re = mGson.fromJson(response, type);
        if (re.hasError()) {
            throw new HttpApiException(re.getErrorCode(), re.getErrorMessage());
        }
        value.close();
        return re.getData();
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
