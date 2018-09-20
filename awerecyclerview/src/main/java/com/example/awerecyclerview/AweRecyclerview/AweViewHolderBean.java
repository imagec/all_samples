package com.example.awerecyclerview.AweRecyclerview;

public class AweViewHolderBean {
    private int mLayoutId = -1;
    private int type = -1;
    private Class clz;

    public AweViewHolderBean(int type, int mLayoutId, Class clz) {
        this.mLayoutId = mLayoutId;
        this.type = type;
        this.clz = clz;
    }

    public int getmLayoutId() {
        return mLayoutId;
    }

    public void setmLayoutId(int mLayoutId) {
        this.mLayoutId = mLayoutId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }
}
