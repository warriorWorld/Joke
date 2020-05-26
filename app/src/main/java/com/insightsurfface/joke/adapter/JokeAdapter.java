package com.insightsurfface.joke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insightsurfface.joke.R;
import com.insightsurfface.joke.bean.JokeBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2017/11/15.
 * 还款页的还款计划
 */
public class JokeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context context;
    private final int TYPE_NORMAL = 0;
    private final int TYPE_END = 1;
    private List<JokeBean.ResultBean.DataBean> list;

    public JokeAdapter(Context context) {
        this.context = context;
    }

    // 创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_END) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_end, viewGroup, false);
            return new ListEndViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_joke, viewGroup, false);
            return new NormalViewHolder(view);
        }
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            JokeBean.ResultBean.DataBean item = list.get(position);
            NormalViewHolder vh = (NormalViewHolder) viewHolder;
            vh.jokeTv.setText(item.getContent());
            vh.dateTv.setText(item.getUpdatetime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (null == list || list.size() == 0) {
            return TYPE_END;
        } else if (position == 400) {
            return TYPE_END;
        } else {
            return TYPE_NORMAL;
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        if (null == list || list.size() == 0) {
            return 1;
        } else if (list.size() == 400) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    public void setList(List<JokeBean.ResultBean.DataBean> list) {
        this.list = list;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        private TextView jokeTv;
        private TextView dateTv;

        public NormalViewHolder(View view) {
            super(view);
            jokeTv = (TextView) view.findViewById(R.id.joke_tv);
            dateTv = (TextView) view.findViewById(R.id.date_tv);
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ListEndViewHolder extends RecyclerView.ViewHolder {
        private TextView endTv;

        public ListEndViewHolder(View view) {
            super(view);
            endTv = view.findViewById(R.id.end_tv);
        }
    }
}
