package com.demo.base.network.http.api;


import com.demo.base.BaseApplication;
import com.demo.base.network.http.base.ByteConvertFactory;
import com.demo.base.network.http.base.ClearResponseCacheControlInterceptor;
import com.demo.base.network.http.base.ClientType;
import com.demo.base.network.http.base.HeaderInterceptor;
import com.demo.base.network.http.base.HttpApiHeadersProvider;
import com.demo.base.network.http.base.HttpApiInitializeListener;
import com.demo.base.network.http.base.HttpApiInterceptorProvider;
import com.demo.base.network.http.base.OfflineRequestCacheInterceptor;
import com.demo.base.network.http.base.OnlyNetInterceptor;
import com.demo.base.network.http.base.RequestToResponseCacheControlInterceptor;
import com.demo.base.network.http.base.XmlOrJsonConverterFactory;

import java.io.File;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 创建时间： 2017/8/5.
 * 编写人：wuweimin
 * 功能描述：网络请求工具
 */

public class HttpApi {
    private boolean mOpenHttpLog = false;//默认false，输出http请求日志，在application里初始时可改
    private boolean mVerifyHttps;
    private boolean hadInit = false;
    private Config config;

    private String mBaseUrl;
    private File mCacheDir;
    private Map<String, String> mHttpHeaderMap;
    private Map<ClientType, Retrofit> mRetrofitMap;
    private Map<ClientType, OkHttpClient> mOkHttpClientMap;
    private Interceptor mInterceptor;

    /**
     * 通过一个网络请求接口来创建一个具体执行该网络请求的服务
     * <p/>
     * For example:
     * <pre>
     * public interface CategoryService {
     *   &#64;POST("category/{cat}/")
     *   Call&lt;List&lt;Item&gt;&gt; categoryList(@Path("cat") String a, @Query("page") int b);
     * }
     * </pre>
     */
    public <T> T createRequestApi(Class<T> clazz) {
        return createRequestApi(clazz, ClientType.onlyNet);
    }

    /**
     * @param clazz      请求接口
     * @param clientType 按照缓存策略分类的client的类型
     */
    public synchronized <T> T createRequestApi(Class<T> clazz, ClientType clientType) {
        if (!hadInit) {
            if (config == null) {
                throw new RuntimeException("Uninitialized！ Config is Null!");
            }
            init(config.openLog, config.verifyHttps, config.baseUrl, config.cacheDir,
                    config.httpHeadersProvider == null ? null : config.httpHeadersProvider.getHttpHeaders(),
                    config.httpInterceptorProvider == null ? null : config.httpInterceptorProvider.getInterceptor());
            if (config.initializeListener != null) {
                config.initializeListener.onInitialize();
            }
            hadInit = true;
        }

        T retrofit = null;
        if (mRetrofitMap.get(clientType) != null) {
            retrofit = mRetrofitMap.get(clientType).create(clazz);
        } else {
            retrofit = createRetrofit(clientType).create(clazz);
        }
        return retrofit;
    }

    /**
     * @param openLog                 打开网络日志
     * @param verifyHttps             https请求校验证书
     * @param baseUrl                 普通http请求的baseUrl
     * @param cacheDir                网络请求缓存的存放目录
     * @param httpProvider            提供全局Http请求的自定义的头部信息
     * @param httpInterceptorProvider 提供全局Http请求的自定义拦截器
     * @param listener                网络请求模块初始化监听器
     */
    public void initConfig(boolean openLog, boolean verifyHttps, String baseUrl, File cacheDir, HttpApiHeadersProvider httpProvider,
                           HttpApiInterceptorProvider httpInterceptorProvider, HttpApiInitializeListener listener) {
        config = new Config().build(openLog, verifyHttps, baseUrl, cacheDir, httpProvider, httpInterceptorProvider, listener);
    }

    /**
     * 网络框架重新初始化
     */
    public void resetInit() {
        hadInit = false;
    }

    /**
     * @param openHttpLog   打开网络日志
     * @param verifyHttps   https请求校验证书
     * @param baseUrl       普通http请求的baseUrl
     * @param cacheDir      网络请求缓存的存放目录
     * @param httpHeaderMap 全局的http请求头部字段
     * @param interceptor   全局的http请求自定义拦截器
     */
    private void init(boolean openHttpLog, boolean verifyHttps, String baseUrl, File cacheDir, Map<String, String> httpHeaderMap, Interceptor interceptor) {
        mOpenHttpLog = openHttpLog;
        mVerifyHttps = verifyHttps;
        mBaseUrl = handleBaseUrl(baseUrl);
        mCacheDir = cacheDir;
        mHttpHeaderMap = httpHeaderMap;
        mInterceptor = interceptor;
        initRetrofit();
    }

    //Retrofit2 里baseUrl不是/结尾会报错
    private static String handleBaseUrl(String url) {
        if (url.endsWith("/")) {
            return url;
        } else {
            return url + "/";
        }
    }

    private void initRetrofit() {
        mRetrofitMap = new EnumMap<>(ClientType.class);
        mOkHttpClientMap = new EnumMap<>(ClientType.class);
        mRetrofitMap.put(ClientType.firstNet, createRetrofit(ClientType.firstNet));
        mRetrofitMap.put(ClientType.firstCache, createRetrofit(ClientType.firstCache));
        mRetrofitMap.put(ClientType.onlyNet, createRetrofit(ClientType.onlyNet));
    }

    public void updateHeader(String headerName, String value) {//动态更新请求头方法
        if (mOkHttpClientMap != null) {
            for (Map.Entry<ClientType, OkHttpClient> okHttpClient : mOkHttpClientMap.entrySet()) {
                if (okHttpClient != null) {
                    if (okHttpClient.getValue() != null) {
                        List<Interceptor> headerInterceptor = okHttpClient.getValue().interceptors();
                        for (int i = 0; i < headerInterceptor.size(); i++) {
                            if (headerInterceptor.get(i) instanceof HeaderInterceptor) {
                                if (((HeaderInterceptor) headerInterceptor.get(i)).getHeaders().containsKey(headerName)) {
                                    ((HeaderInterceptor) headerInterceptor.get(i)).getHeaders().put(headerName, value);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Retrofit createRetrofit(ClientType clientType) {
        OkHttpClient okHttpClient = createOkHttpClient(mOpenHttpLog, mCacheDir, mHttpHeaderMap, clientType);
        mOkHttpClientMap.put(clientType, okHttpClient);
        return new Retrofit
                .Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(XmlOrJsonConverterFactory.create())
                .addConverterFactory(ByteConvertFactory.create())
                .build();
    }

    private OkHttpClient createOkHttpClient(boolean showLog, File cacheDir,
                                            Map<String, String> httpHeaderMap,
                                            ClientType clientType) {
        OkHttpClient.Builder clientBuild = new OkHttpClient.Builder();
        clientBuild.cache(new Cache(new File(cacheDir, "cache"), 1024 * 1024 * 50))  //添加缓存,50Mb
                .addInterceptor(HeaderInterceptor.newInstance(httpHeaderMap))//添加公共头部
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS);

        //https请求校验证书
        if (mVerifyHttps) {
            addVerifyHttpsHandler(clientBuild);
        } else {
            addNonVerifyHttpsHandler(clientBuild);
        }

        //添加自定义拦截器
        if (mInterceptor != null) {
            clientBuild.addInterceptor(mInterceptor);
        }

        if (showLog) {
            //添加一个log拦截器,打印所有的log.设置请求过滤的水平,body,basic,headers
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuild.addInterceptor(httpLoggingInterceptor);
        }
        switch (clientType) {
            case firstNet:
                clientBuild
                        .addInterceptor(OfflineRequestCacheInterceptor.newInstance(BaseApplication.INSTANCE))
                        .addNetworkInterceptor(new ClearResponseCacheControlInterceptor());
                break;
            case firstCache:
                clientBuild
                        .addNetworkInterceptor(RequestToResponseCacheControlInterceptor.newInstance());
                break;
            case onlyNet:
                clientBuild
                        .addInterceptor(new OnlyNetInterceptor());
                break;

            default:
                break;
        }
        return clientBuild.build();
    }

    private class Config {
        boolean openLog;
        boolean verifyHttps;
        String baseUrl;
        File cacheDir;
        HttpApiHeadersProvider httpHeadersProvider;
        HttpApiInterceptorProvider httpInterceptorProvider;
        HttpApiInitializeListener initializeListener;

        Config build(boolean openLog, boolean verifyHttps, String baseUrl, File cacheDir, HttpApiHeadersProvider httpHeadersProvider,
                     HttpApiInterceptorProvider httpInterceptorProvider, HttpApiInitializeListener initializeListener) {
            Config config = new Config();
            config.openLog = openLog;
            config.verifyHttps = verifyHttps;
            config.baseUrl = baseUrl;
            config.cacheDir = cacheDir;
            config.httpHeadersProvider = httpHeadersProvider;
            config.httpInterceptorProvider = httpInterceptorProvider;
            config.initializeListener = initializeListener;
            return config;
        }
    }

    private void addVerifyHttpsHandler(OkHttpClient.Builder clientBuild) {
        SSLContext sslContext = null;
        X509TrustManager x509TrustManager = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            KeyStore keyStore = KeyStore.getInstance("AndroidCAStore");
            keyStore.load(null);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                }
            }
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
            sslContext = null;
        }

        if (sslContext != null) {
            if (x509TrustManager != null) {
                try {
                    clientBuild.sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String hostname, SSLSession session) {
                                    return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    clientBuild.sslSocketFactory(sslContext.getSocketFactory())
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String hostname, SSLSession session) {
                                    return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();

                    x509TrustManager = new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            for (X509Certificate cert : chain) {
                                cert.checkValidity();
                            }
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    };

                    try {
                        clientBuild.sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                                .hostnameVerifier(new HostnameVerifier() {
                                    @Override
                                    public boolean verify(String hostname, SSLSession session) {
                                        return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
                                    }
                                });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private void addNonVerifyHttpsHandler(OkHttpClient.Builder clientBuild) {
        SSLContext sslContext = null;
        X509TrustManager x509TrustManager = null;
        try {
            x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
            sslContext = null;
        }

        if (sslContext != null) {
            if (x509TrustManager != null) {
                try {
                    clientBuild.sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String hostname, SSLSession session) {
                                    return true;
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
