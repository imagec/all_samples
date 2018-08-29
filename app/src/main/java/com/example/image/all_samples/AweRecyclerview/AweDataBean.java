package com.example.image.all_samples.AweRecyclerview;

public class AweDataBean<T> {
    private int type;
    private T data;

    public AweDataBean(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
