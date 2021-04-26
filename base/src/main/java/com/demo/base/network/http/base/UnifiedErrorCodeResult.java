package com.demo.base.network.http.base;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 创建时间： 2017/8/21.
 * 编写人：wuweimin
 * 功能描述：
 */

class UnifiedErrorCodeResult<T> {
    @SerializedName("errorCode")
    private Integer errorCode; //错误码
    @SerializedName("errorMessage")
    private String errorMessage; //错误描述

    @SerializedName("results")
    private T data;

    /**
     * 判断是否有错误，仅当errorCode！=0和errorMessage不为空时才算有错误
     */
    boolean hasError() {
        return errorCode != null && errorCode != 0 && !TextUtils.isEmpty(errorMessage);
    }

    int getErrorCode() {
        return errorCode == null ? 0 : errorCode;
    }

    String getErrorMessage() {
        return errorMessage == null ? "" : errorMessage;
    }

    T getData() {
        return data;
    }

    /**
     * 检查TOKEN是否过期
     */
    public boolean checkTokenIfExpired() {
        return errorCode != null && errorCode >= 104 && errorCode <= 108;
    }
}
