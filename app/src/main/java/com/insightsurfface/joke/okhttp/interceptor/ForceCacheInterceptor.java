package com.insightsurfface.joke.okhttp.interceptor;

import com.insightsurfface.joke.config.Configures;
import com.insightsurfface.joke.utils.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ForceCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (request.url().toString().contains(Configures.JOKE_BASE_URL)) {
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    //cache for cache seconds
                    .header("Cache-Control", "max-age=" + 3600 * 2)
                    .build();
        }
        return response;
    }
}
