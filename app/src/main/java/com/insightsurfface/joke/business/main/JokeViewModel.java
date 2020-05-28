package com.insightsurfface.joke.business.main;

import android.content.Context;

import com.insightsurfface.joke.base.BaseViewModel;
import com.insightsurfface.joke.bean.JokeBean;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class JokeViewModel extends BaseViewModel {
    private Context mContext;
    private IJokeModel mJokeModel;
    private MutableLiveData<JokeBean> mJoke = new MutableLiveData<>();

    public JokeViewModel(Context context) {
        mContext = context.getApplicationContext();
        mJokeModel = new JokeModel();
    }

    void getJokes(int page) {
        isUpdating.setValue(true);
        DisposableObserver<JokeBean> observer = new DisposableObserver<JokeBean>() {
            @Override
            public void onNext(JokeBean result) {
                mJoke.setValue(result);
            }

            @Override
            public void onError(Throwable e) {
                isUpdating.setValue(false);
                message.setValue(e.getMessage());
            }

            @Override
            public void onComplete() {
                isUpdating.setValue(false);
            }
        };

        mObserver.add(observer);
        mJokeModel.getJokes(mContext, page, observer);
    }

    public LiveData<JokeBean> getJoke() {
        return mJoke;
    }
}
