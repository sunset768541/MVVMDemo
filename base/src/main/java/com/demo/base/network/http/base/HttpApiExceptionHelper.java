package com.demo.base.network.http.base;

/**
 * 创建时间： 2017/8/21.
 * 编写人：wuweimin
 * 功能描述：
 */

public class HttpApiExceptionHelper {

    public interface ApiError {
        void onError(int errorCode, String message, String extension);
    }

    public static void handleHttpApiException(Throwable t, ApiError apiError) {
        if (t instanceof HttpApiException) {
            HttpApiException a = (HttpApiException) t;
            if (apiError != null) {
                apiError.onError(a.getErrorCode(), a.getMessage(), a.getExtension());
            }
        }
    }
}
