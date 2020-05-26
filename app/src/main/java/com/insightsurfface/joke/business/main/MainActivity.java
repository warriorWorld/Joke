package com.insightsurfface.joke.business.main;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.insightsurfface.joke.R;
import com.insightsurfface.joke.adapter.JokeAdapter;
import com.insightsurfface.joke.base.BaseActivity;
import com.insightsurfface.joke.bean.JokeBean;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends BaseActivity {
    private SwipeRefreshLayout jokeSrl;
    private RecyclerView jokeRcv;
    private JokeAdapter mAdapter;
    private List<JokeBean.ResultBean.DataBean> list = new ArrayList<>();
    private JokeViewModel mJokeViewModel;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVM();
    }

    private void initVM() {
        mJokeViewModel = ViewModelProviders.of(this).get(JokeViewModel.class);
        mJokeViewModel.init();
        mJokeViewModel.getJoke().observe(this, new Observer<JokeBean>() {
            @Override
            public void onChanged(JokeBean bean) {
                list.add(bean.getResult().getData().get(0));
                initRec();
            }
        });
        mJokeViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                baseToast.showToast(s);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
