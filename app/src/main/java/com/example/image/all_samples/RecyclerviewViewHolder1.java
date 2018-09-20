package com.example.image.all_samples;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.awerecyclerview.AweRecyclerview.AbsAweViewHolder;
import com.example.awerecyclerview.AweRecyclerview.AweDataBean;
import com.example.awerecyclerview.AweRecyclerview.AweRecyAdapter;
import com.example.awerecyclerview.AweRecyclerview.AweRecyAdapterHelper;
import com.example.image.all_samples.Bean.MockDataBean;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewViewHolder1 extends AbsAweViewHolder {
    private final String TAG = "ViewHolder1";
    private RecyclerView mRecyclerView;
    private AweRecyAdapterHelper aweRecyAdapterHelper = new AweRecyAdapterHelper(ContextUtil.getInstance());
    private AweRecyAdapter aweRecyAdapter = new AweRecyAdapter(ContextUtil.getInstance());

    public RecyclerviewViewHolder1(View itemView) {
        super(itemView);

        aweRecyAdapterHelper.addViewHolder(1, R.layout.view_cate_item, RecyclerviewCateViewHolder.class);

        aweRecyAdapter.setAweRecyAdapterHelper(aweRecyAdapterHelper);

        mRecyclerView = itemView.findViewById(R.id.recylcer_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(ContextUtil.getInstance(), 5));
        mRecyclerView.setAdapter(aweRecyAdapter);
    }

    @Override
    public void update() {
        Log.d(TAG, "data = " + getData());

        List<MockDataBean> mockDataBeansList = (List<MockDataBean>) getData();
        List<AweDataBean> aweDataBeanList = new ArrayList<>();

        for (MockDataBean item : mockDataBeansList) {
            aweDataBeanList.add(new AweDataBean(1, item));
        }

        aweRecyAdapter.updataView(aweDataBeanList);

    }
}
