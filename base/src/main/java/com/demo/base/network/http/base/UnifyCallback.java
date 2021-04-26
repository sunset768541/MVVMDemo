package com.demo.base.network.http.base;


import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 创建时间： 2017/9/5.
 * 编写人：wuweimin
 * 功能描述：retrofit响应处理工具，统一处理响应头
 */

public abstract class UnifyCallback<T> implements Callback<T> {

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (504 == response.code()) {// 缓存无效
            onFail(call, new HttpApiException(response.code(), "cache over time"));
        } else if (!response.isSuccessful()) {// response code isn't in the range [200..300).
            onFail(call, new HttpApiException(response.code(), "response isn't Successful !"));
        } else {
            onSuccess(call, response);
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        HttpApiException e;
        if (t instanceof HttpApiException) {
            e = (HttpApiException) t;
        } else {
            e = new HttpApiException(800, t.getMessage());
        }
        onFail(call, e);
    }

    public abstract void onFail(Call<T> call, HttpApiException e);

    public abstract void onSuccess(Call<T> call, Response<T> response);
}
