package com.insightsurfface.joke.business.main;

import com.insightsurfface.joke.bean.JokeBean;

import io.reactivex.observers.DisposableObserver;

public interface IJokeModel {
    void getJokes(int page, DisposableObserver<JokeBean> observer);
}
