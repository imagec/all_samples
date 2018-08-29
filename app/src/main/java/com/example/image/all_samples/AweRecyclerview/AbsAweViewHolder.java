package com.example.image.all_samples.AweRecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AbsAweViewHolder<T> extends RecyclerView.ViewHolder {
    public AbsAweViewHolder(View itemView) {
        super(itemView);
    }

    private int type = -1;
    private int layoutId = -1;
    private T data = null;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public abstract void update();
}
