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

        if (request.url().toString().contains(Configures.JOKE_BASE_URL)) {
            String cache = request.header("Cache-Time");
            Logger.d(cache);
        }
        return null;
    }
}
