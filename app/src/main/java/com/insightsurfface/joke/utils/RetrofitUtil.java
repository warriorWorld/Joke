package com.insightsurfface.joke.utils;


import android.content.Context;

import com.insightsurfface.joke.config.Configures;
import com.insightsurfface.joke.okhttp.interceptor.ForceCacheInterceptor;
import com.insightsurfface.joke.okhttp.interceptor.Level;
import com.insightsurfface.joke.okhttp.interceptor.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * Created by liu on 2017/6/22.
 */

public class RetrofitUtil {
    private static Retrofit retrofit = null;

    public static final int CONNECT_TIME_OUT = 60;
    public static final int READ_TIME_OUT = 60;
    public static final int WRITE_TIME_OUT = 60;

    public static Retrofit getInstance(Context context) {

        if (retrofit == null) {
            synchronized (RetrofitUtil.class) {
                if (retrofit == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new LoggingInterceptor.Builder()
                                    .loggable(true)//true拦截日志
                                    .setLevel(Level.BODY)//打印内容配置 Level.NONE不打印
                                    .log(INFO)//log级别
                                    .request("HttpLog_Request")
                                    .response("HttpLog_Response")
                                    .build())
                            .addInterceptor(new ForceCacheInterceptor())
                            .cache(new Cache(context.getApplicationContext().getCacheDir(), 1024 * 1024))
                            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .build();
                    // http://10.118.8.13:8080
                    // http://10.118.8.28
                    // http://10.118.8.34
                    // http://10.118.8.20:8080
                    // http://10.118.8.66:8090
                    // http://10.118.18.22:8080
                    // test
                    // http://store-uat.sfbest.com
                    // production
                    // https://store.sfbest.com
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Configures.JOKE_BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }


    /**
     * 登录后重新创建带有token的retrofit
     */
    public static void setRetrofitToNull() {
        RetrofitUtil.retrofit = null;
    }


    //连本地Ip用
    private static Retrofit testInstance = null;

//    public static Retrofit getTestInstance() {
//
//        if (testInstance == null) {
//            synchronized (RetrofitUtil.class) {
//                if (testInstance == null) {
//                    OkHttpClient client = new OkHttpClient.Builder()
//                            .addInterceptor(new Interceptor() {
//                                @Override
//                                public Response intercept(Chain chain) throws IOException {
//                                    Request request = chain.request();
//                                    Request.Builder builder = request.newBuilder();
//                                    request = builder.addHeader("ssi-token", UserManager.getToken()).build();
//                                    return chain.proceed(request);
//                                }
//                            })
//                            .addInterceptor(new LoggingInterceptor.Builder()
//                                    .loggable(true)//true拦截日志
//                                    .setLevel(Level.BODY)//打印内容配置 Level.NONE不打印
//                                    .log(INFO)//log级别
//                                    .request("HttpLog_Request")
//                                    .response("HttpLog_Response")
//                                    .build())
//                            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS).
//                                    readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).
//                                    writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS).build();
//                    testInstance = new Retrofit.Builder().baseUrl("http://10.118.18.46:8085/").client(client).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
//                }
//            }
//        }
//        return testInstance;
//    }
}
