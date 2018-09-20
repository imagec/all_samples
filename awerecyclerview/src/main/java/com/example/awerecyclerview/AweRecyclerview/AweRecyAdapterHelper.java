package com.example.awerecyclerview.AweRecyclerview;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AweRecyAdapterHelper {
    private Context mContext;
    private List<AbsAweViewHolder> absAweViewHolderList = new ArrayList<>();
    HashMap<Integer, AweViewHolderBean> aweViewHolderBeanList = new HashMap<>();

    public AweRecyAdapterHelper(Context mContext) {
        this.mContext = mContext;
    }

    public HashMap<Integer, AweViewHolderBean> getAweViewHolderBeanList() {
        return aweViewHolderBeanList;
    }

    public void addViewHolder(int type, int layoutId, Class cls) {
        aweViewHolderBeanList.put(type, new AweViewHolderBean(type, layoutId, cls));
    }
}
