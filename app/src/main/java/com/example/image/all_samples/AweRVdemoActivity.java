package com.example.image.all_samples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.awerecyclerview.AweRecyclerview.AweDataBean;
import com.example.awerecyclerview.AweRecyclerview.AweRecyAdapter;
import com.example.awerecyclerview.AweRecyclerview.AweRecyAdapterHelper;
import com.example.image.all_samples.Bean.MockDataBean;

import java.util.ArrayList;
import java.util.List;

public class AweRVdemoActivity extends Activity {

    private RecyclerView mRecyclerView;
    private AweRecyAdapterHelper aweRecyAdapterHelper = new AweRecyAdapterHelper(this);
    private AweRecyAdapter aweRecyAdapter = new AweRecyAdapter(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awerv_demo);

        aweRecyAdapterHelper.addViewHolder(1, R.layout.view_recyclerview_container, RecyclerviewViewHolder1.class);
        aweRecyAdapter.setAweRecyAdapterHelper(aweRecyAdapterHelper);
        mRecyclerView = findViewById(R.id.recylcer_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(aweRecyAdapter);

        List<AweDataBean> aweDataBeanList = new ArrayList<>();
        String jsStr = "[{\"id\":10,\"cateId\":9,\"name\":\"娱乐\",\"icon\":\"https://gw.alicdn.com/tfs/TB1Y5bQt9tYBeNjSspaXXaOOFXa-60-60.png\",\"actionType\":\"\",\"actionUrl\":\"\"},{\"id\":11,\"cateId\":7,\"name\":\"生活\",\"icon\":\"https://gw.alicdn.com/tfs/TB1PibQt9tYBeNjSspaXXaOOFXa-60-60.png\",\"actionType\":\"\",\"actionUrl\":\"\"},{\"id\":12,\"cateId\":15,\"name\":\"教育\",\"icon\":\"https://gw.alicdn.com/tfs/TB1x9N8t1SSBuNjy0FlXXbBpVXa-60-64.png\",\"actionType\":\"\",\"actionUrl\":\"\"},{\"id\":13,\"cateId\":5,\"name\":\"游戏\",\"icon\":\"https://gw.alicdn.com/tfs/TB1jKq3t21TBuNjy0FjXXajyXXa-60-60.png\",\"actionType\":\"\",\"actionUrl\":\"\"},{\"id\":15,\"cateId\":13,\"name\":\"工具\",\"icon\":\"https://gw.alicdn.com/tfs/TB1VPxWt4SYBuNjSsphXXbGvVXa-60-60.png\",\"actionType\":\"\",\"actionUrl\":\"\"}]";

        List<MockDataBean> mockDataBeansList = JSON.parseArray(jsStr, MockDataBean.class);
        mockDataBeansList.add(mockDataBeansList.get(0));
        mockDataBeansList.add(mockDataBeansList.get(1));
        mockDataBeansList.add(mockDataBeansList.get(2));
        mockDataBeansList.add(mockDataBeansList.get(3));

        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));
        aweDataBeanList.add(new AweDataBean(1, mockDataBeansList));

        aweRecyAdapter.updataView(aweDataBeanList);
    }
}
