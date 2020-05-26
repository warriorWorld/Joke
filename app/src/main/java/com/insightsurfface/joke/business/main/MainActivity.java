package com.insightsurfface.joke.business.main;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.insightsurfface.joke.R;
import com.insightsurfface.joke.adapter.JokeAdapter;
import com.insightsurfface.joke.base.BaseActivity;
import com.insightsurfface.joke.bean.JokeBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
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
        mJokeViewModel.getJokes(currentPage);
    }

    private void initVM() {
        mJokeViewModel = ViewModelProviders.of(this).get(JokeViewModel.class);
        mJokeViewModel.init();
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
        jokeRcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    baseToast.showToast("到底了");
                    currentPage++;
                    if (currentPage > 20) {
                        return;
                    }
                    mJokeViewModel.getJokes(currentPage);
                }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
