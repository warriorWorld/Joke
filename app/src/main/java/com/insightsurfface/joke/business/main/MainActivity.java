package com.insightsurfface.joke.business.main;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.insightsurfface.joke.R;
import com.insightsurfface.joke.adapter.JokeAdapter;
import com.insightsurfface.joke.base.BaseActivity;
import com.insightsurfface.joke.bean.CityBean;
import com.insightsurfface.joke.bean.JokeBean;
import com.insightsurfface.joke.bean.WeatherBean;
import com.insightsurfface.joke.config.Configures;
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

public class MainActivity extends BaseActivity {
    private SwipeRefreshLayout jokeSrl;
    private RecyclerView jokeRcv;
    private JokeAdapter mAdapter;
    private List<JokeBean.ResultBean.DataBean> list = new ArrayList<>();
    private JokeViewModel mJokeViewModel;
    private WeatherViewModel mWeatherViewModel;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVM();
        mJokeViewModel.getJokes(currentPage);
        mWeatherViewModel.getWeather("1");
//        mWeatherViewModel.getSupportCities();
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
                new NormalDialogBuilder(MainActivity.this)
                        .setTitle("天气")
                        .setMessage(bean.getResult().getRealtime().getInfo())
                        .setOkText("确定")
                        .create()
                        .show();
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
        baseTopBar.setTitle(getResources().getString(R.string.app_name));
        baseTopBar.setRightBackground(R.drawable.ic_qrcode);
    }

    @Override
    protected void topBarOnRightClick() {

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
}
