package com.insightsurfface.joke.okhttp.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.insightsurfface.joke.cache.CacheCaretaker;
import com.insightsurfface.joke.config.Configures;
import com.insightsurfface.joke.utils.Logger;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ForceCacheInterceptor implements Interceptor {
    private Context mContext;
    private final String[] SUPPORT_CACHE_URLS = {
            Configures.JOKE_BASE_URL,
            Configures.GET_WEATHER_TYPE_URL,
            Configures.GET_CITY_LIST_URL,
    };

    public ForceCacheInterceptor(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Logger.d("headers:" + request.headers().toString());
        try {
            Logger.d("request body:" + request.body().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isSupportCache(request)) {
            HashMap<String, String> cacheMap = CacheCaretaker.getContent(mContext);
            if (null == cacheMap) {
                cacheMap = new HashMap<>();
            }
            if (cacheMap.containsKey(getKey(request))) {
                return new Response.Builder()
                        .request(request)
                        .protocol(Protocol.get(Protocol.HTTP_1_0.toString()))
                        .message("success")
                        .code(200)
                        .body(ResponseBody.create(MediaType.get("application/json"), cacheMap.get(getKey(request))))
                        .build();
            } else {
                Response response = chain.proceed(request);
                if (response.isSuccessful()) {
                    //string方法只能调用一次 在调用了response.body().string()方法之后，response中的流会被关闭，我们需要创建出一个新的response给应用层处理。
                    String content = response.body().string();
                    cacheMap.put(request.url().toString(), content);
                    CacheCaretaker.saveContent(mContext, cacheMap);
                    return response.newBuilder()
                            .body(ResponseBody.create(response.body().contentType(), content))
                            .build();
                }
                return response;
            }
        } else {
            return chain.proceed(request);
        }
    }

    private boolean isSupportCache(Request request) {
        String url = request.url().toString();
        for (String item : SUPPORT_CACHE_URLS) {
            if (url.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

    private String getKey(Request request) {
        String url = request.url().toString();
        String bodyS = null;
        RequestBody body = request.body();
        if (null != body) {
            bodyS = body.toString();
        }
        if (!TextUtils.isEmpty(bodyS)) {
            return url + bodyS;
        } else {
            return url;
        }
    }
}
