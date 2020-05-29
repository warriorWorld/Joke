package com.insightsurfface.joke.business.main;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.insightsurfface.joke.R;
import com.insightsurfface.joke.adapter.JokeAdapter;
import com.insightsurfface.joke.base.BaseActivity;
import com.insightsurfface.joke.bean.AnimationDataBean;
import com.insightsurfface.joke.bean.CityBean;
import com.insightsurfface.joke.bean.JokeBean;
import com.insightsurfface.joke.bean.WeatherBean;
import com.insightsurfface.joke.config.Configures;
import com.insightsurfface.joke.utils.DisplayUtil;
import com.insightsurfface.joke.widget.dialog.NormalDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private SwipeRefreshLayout jokeSrl;
    private RecyclerView jokeRcv;
    private ImageView weatherIv;
    private RelativeLayout mainBarRl;
    private JokeAdapter mAdapter;
    private List<JokeBean.ResultBean.DataBean> list = new ArrayList<>();
    private JokeViewModel mJokeViewModel;
    private WeatherViewModel mWeatherViewModel;
    private int currentPage = 1;
    private AnimationDrawable spinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVM();
        mJokeViewModel.getJokes(currentPage);
//        startAnim();
        mWeatherViewModel.getWeather("1");
//        mWeatherViewModel.getSupportCities();
    }

    private void refreshWeatherUI(WeatherBean.ResultBean.RealtimeBean item) {
        if (null == item) {
            return;
        }
        weatherIv.setVisibility(View.VISIBLE);
        int status = Integer.parseInt(item.getWid());
        switch (status) {
            case 0:
            case 1:
            case 29:
            case 30:
            case 31:
            case 53:
                weatherIv.setBackgroundResource(R.drawable.anim_sunshine);
                spinner = (AnimationDrawable) weatherIv.getBackground();
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
                weatherIv.setBackgroundResource(R.drawable.anim_rain);
                spinner = (AnimationDrawable) weatherIv.getBackground();
                break;
        }
        assert spinner != null;
        spinner.start();
        startAnim();
    }

    private void startAnim() {
        DisposableObserver observer = new DisposableObserver<Float[]>() {
            @Override
            public void onNext(Float[] bean) {
                ObjectAnimator xTransportAnimation = ObjectAnimator.ofFloat(weatherIv, "translationX", bean[0]);
                ObjectAnimator yTransportAnimation = ObjectAnimator.ofFloat(weatherIv, "translationY", bean[1]);
                ObjectAnimator shrinkXAnimation = ObjectAnimator.ofFloat(weatherIv, "scaleX", 0.4f);
                ObjectAnimator shrinkYAnimation = ObjectAnimator.ofFloat(weatherIv, "scaleY", 0.4f);
                AnimatorSet set = new AnimatorSet();
                //属性动画监听类
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        spinner.stop();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                set.playTogether(xTransportAnimation, yTransportAnimation, shrinkXAnimation, shrinkYAnimation);
                set.setStartDelay(2000L);
                set.setDuration(1000);
                set.start();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        mObserver.add(observer);
        Observable
                .create(new ObservableOnSubscribe<AnimationDataBean>() {
                    @Override
                    public void subscribe(ObservableEmitter<AnimationDataBean> emitter) throws Exception {
                        weatherIv.post(new Runnable() {
                            @Override
                            public void run() {
                                AnimationDataBean item = new AnimationDataBean();
                                item.setTargetHeight(weatherIv.getHeight());
                                item.setTargetWidth(weatherIv.getWidth());
                                item.setTargetX(weatherIv.getX());
                                item.setTargetY(weatherIv.getY());
                                emitter.onNext(item);
                            }
                        });
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<AnimationDataBean, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(AnimationDataBean bean) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Float[]>() {
                            @Override
                            public void subscribe(ObservableEmitter<Float[]> emitter) throws Exception {
                                emitter.onNext(new Float[]{-(bean.getTargetX() - DisplayUtil.dip2px(MainActivity.this, 10) + bean.getTargetWidth() * 0.3f),
                                        -(bean.getTargetY() - DisplayUtil.dip2px(MainActivity.this, 5) + bean.getTargetHeight() * 0.3f)});
                            }
                        });
                    }
                })
                .subscribe(observer);
    }

    private void initVM() {
//        mJokeViewModel = ViewModelProviders.of(this).get(JokeViewModel.class);
        mJokeViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            @SuppressWarnings("unchecked")
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new JokeViewModel(MainActivity.this);
            }
        }).get(JokeViewModel.class);

        mJokeViewModel.getJoke().observe(this, new Observer<JokeBean>() {
            @Override
            public void onChanged(JokeBean bean) {
                list.addAll(bean.getResult().getData());
                initRec();
            }
        });
        mJokeViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                new NormalDialogBuilder(MainActivity.this)
                        .setTitle("注意了:")
                        .setMessage(s)
                        .setOkText("确定")
                        .create()
                        .show();
            }
        });
        mJokeViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    jokeSrl.setRefreshing(true);
                } else {
                    jokeSrl.setRefreshing(false);
                }
            }
        });
        mWeatherViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            @SuppressWarnings("unchecked")
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new WeatherViewModel(MainActivity.this);
            }
        }).get(WeatherViewModel.class);
        mWeatherViewModel.getWeather().observe(this, new Observer<WeatherBean>() {
            @Override
            public void onChanged(WeatherBean bean) {
                refreshWeatherUI(bean.getResult().getRealtime());
            }
        });
        mWeatherViewModel.getCity().observe(this, new Observer<CityBean>() {
            @Override
            public void onChanged(CityBean bean) {
                new NormalDialogBuilder(MainActivity.this)
                        .setTitle("城市")
                        .setMessage(bean.getResult().toString())
                        .create()
                        .show();
            }
        });
    }

    @Override
    protected void initUI() {
        super.initUI();
        weatherIv = findViewById(R.id.weather_iv);
        mainBarRl = findViewById(R.id.main_bar_rl);
        jokeSrl = (SwipeRefreshLayout) findViewById(R.id.joke_srl);
        jokeSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                list.clear();
                list = new ArrayList<>();
                mJokeViewModel.getJokes(currentPage);
            }
        });
        jokeRcv = (RecyclerView) findViewById(R.id.joke_rcv);
        jokeRcv.setLayoutManager
                (new LinearLayoutManager
                        (this, LinearLayoutManager.VERTICAL, false));
        jokeRcv.setFocusableInTouchMode(false);
        jokeRcv.setFocusable(false);
        jokeRcv.setHasFixedSize(true);
        jokeRcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastPos = manager.findLastVisibleItemPosition();
                    if (!jokeSrl.isRefreshing()) {
                        if (manager.getChildCount() > 0 && lastPos >= manager.getItemCount() - 1 && manager.getItemCount() > manager.getChildCount()) {
                            currentPage++;
                            if (currentPage > Configures.JOKE_PAGE_LIMIT) {
                                currentPage = Configures.JOKE_PAGE_LIMIT;
                                return;
                            }
                            mJokeViewModel.getJokes(currentPage);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        LayoutAnimationController controller = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.recycler_load));
        jokeRcv.setLayoutAnimation(controller);
        hideBack();
        weatherIv.setOnClickListener(this);
        hideBaseTopBar();
    }

    private void initRec() {
        try {
            if (null == mAdapter) {
                mAdapter = new JokeAdapter(this);
                mAdapter.setList(list);
                jokeRcv.setAdapter(mAdapter);
            } else {
                mAdapter.setList(list);
                mAdapter.notifyDataSetChanged();
            }
            mAdapter.setCurrentPage(currentPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather_iv:
                break;
        }
    }
}
