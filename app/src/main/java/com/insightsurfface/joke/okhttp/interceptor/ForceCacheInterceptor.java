package com.insightsurfface.joke.okhttp.interceptor;

import android.content.Context;

import com.insightsurfface.joke.cache.CacheCaretaker;
import com.insightsurfface.joke.config.Configures;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ForceCacheInterceptor implements Interceptor {
    private Context mContext;

    public ForceCacheInterceptor(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (request.url().toString().startsWith(Configures.JOKE_BASE_URL)) {
            HashMap<String, String> cacheMap = CacheCaretaker.getContent(mContext);
            if (null == cacheMap) {
                cacheMap = new HashMap<>();
            }
            if (cacheMap.containsKey(request.url().toString())) {
                return new Response.Builder()
                        .request(request)
                        .protocol(Protocol.get(Protocol.HTTP_1_0.toString()))
                        .message("success")
                        .code(200)
                        .body(ResponseBody.create(MediaType.get("application/json"), cacheMap.get(request.url().toString())))
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
}
