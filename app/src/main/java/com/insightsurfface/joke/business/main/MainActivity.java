package com.insightsurfface.joke.business.main;

import android.os.Bundle;

import com.insightsurfface.joke.R;
import com.insightsurfface.joke.base.BaseActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends BaseActivity {
    private SwipeRefreshLayout jokeSrl;
    private RecyclerView jokeRcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUI() {
        super.initUI();
        jokeSrl = (SwipeRefreshLayout) findViewById(R.id.joke_srl);
        jokeRcv = (RecyclerView) findViewById(R.id.joke_rcv);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
