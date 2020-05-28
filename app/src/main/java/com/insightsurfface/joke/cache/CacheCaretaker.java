package com.insightsurfface.joke.cache;

import android.content.Context;

import com.insightsurfface.joke.utils.ShareObjUtil;

import java.util.HashMap;

public class CacheCaretaker {
    private final static String KEY = "cache";

    public static void saveContent(Context context, HashMap<String, String> content) {
        ShareObjUtil.saveObject(context, content, KEY);
    }

    public static HashMap<String, String> getContent(Context context) {
        return (HashMap<String, String>) ShareObjUtil.getObject(context, KEY);
    }

    public static void clean(Context context) {
        ShareObjUtil.deleteFile(context, KEY);
    }
}
