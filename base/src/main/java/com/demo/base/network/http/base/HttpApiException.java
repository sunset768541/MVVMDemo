package com.demo.base.network.http.base;

/**
 * 创建时间： 2017/8/21.
 * 编写人：wuweimin
 * 功能描述：
 */

public class HttpApiException extends RuntimeException {
    private int errorCode;
    private String extension;

    HttpApiException(int code, String message) {
        this(code, message, "");
    }

    HttpApiException(int code, String message, String extension) {
        super(message);
        errorCode = code;
        this.extension = extension;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getExtension() {
        return extension;
    }
}
