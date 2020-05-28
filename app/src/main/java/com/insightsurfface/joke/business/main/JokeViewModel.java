package com.insightsurfface.joke.business.main;

import android.content.Context;

import com.insightsurfface.joke.bean.JokeBean;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class JokeViewModel extends ViewModel {
    private Context mContext;
    private IJokeModel mJokeModel;
    private CompositeDisposable mObserver = new CompositeDisposable();
    private MutableLiveData<JokeBean> mJoke = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private MutableLiveData<String> message = new MutableLiveData<>();

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

    @Override
    protected void onCleared() {
        super.onCleared();
        mObserver.dispose();
    }

    public LiveData<JokeBean> getJoke() {
        return mJoke;
    }

    public LiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
