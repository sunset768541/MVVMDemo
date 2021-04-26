package com.demo.module_news;


import com.demo.base.network.http.base.HttpApiInterceptorProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestHeader implements HttpApiInterceptorProvider {
    public static final String secretId = "AKID77W38gkXndPGbrvesAmMmkeZdOB8fPh92PdO";
    public static final String secretKey = "1n4jf3i5f51znIe3uot0dglgsx7lbn1ik43qmx06";
    @Override
    public Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();


                Calendar cd = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String datetime = sdf.format(cd.getTime());
                String source = "market";
                // 签名
                String auth = calcAuthorization(source, secretKey, datetime);
                // 请求方法
                // 请求头
                builder.addHeader("X-Source", source);
                Calendar cd1 = Calendar.getInstance();
                builder.addHeader("X-Date", sdf.format(cd1.getTime()));
                builder.addHeader("Authorization", auth);
                return chain.proceed(builder.build());
            }
        };
    }
    public static String calcAuthorization(String source,  String secretKey, String datetime)
    {
        try {
            String signStr = "x-date: " + datetime + "\n" + "x-source: " + source;
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec sKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), mac.getAlgorithm());
            mac.init(sKey);
            byte[] hash = mac.doFinal(signStr.getBytes("UTF-8"));
            String sig = Base64.encode(hash);

            String auth = "hmac id=\"" + secretId + "\", algorithm=\"hmac-sha1\", headers=\"x-date x-source\", signature=\"" + sig + "\"";
            return auth;
        }catch (Exception E){
            E.printStackTrace();
        }
        return "";
    }
}
