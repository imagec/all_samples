package com.example.awerecyclerview.AweRecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class AweRecyAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<AweDataBean> dataList = new ArrayList<>();
    private AweRecyAdapterHelper aweRecyAdapterHelper;

    public AweRecyAdapter(Context context) {
        this.mContext = context;
    }

    public AweRecyAdapterHelper getAweRecyAdapterHelper() {
        return aweRecyAdapterHelper;
    }

    public void setAweRecyAdapterHelper(AweRecyAdapterHelper aweRecyAdapterHelper) {
        this.aweRecyAdapterHelper = aweRecyAdapterHelper;
    }

    public void updataView(List<AweDataBean> itemList) {
        dataList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AweViewHolderBean aweViewHolderBean = aweRecyAdapterHelper.getAweViewHolderBeanList().get(viewType);

        try {
            View view = LayoutInflater.from(mContext).inflate(aweViewHolderBean.getmLayoutId(), parent, false);
            Constructor c = aweViewHolderBean.getClz().getConstructor(View.class);
            return (RecyclerView.ViewHolder) c.newInstance(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AbsAweViewHolder) {
            for (int i = 0; i < dataList.size(); i++) {
                if (i == position) {
                    ((AbsAweViewHolder) holder).setData(dataList.get(i).getData());
                    ((AbsAweViewHolder) holder).update();
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }
}
