package com.example.image.all_samples;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.awerecyclerview.AweRecyclerview.AbsAweViewHolder;
import com.example.image.all_samples.Bean.MockDataBean;

public class RecyclerviewCateViewHolder extends AbsAweViewHolder {
    private final String TAG = "CateViewHolder";
    private ImageView imageView;
    private TextView textView;

    public RecyclerviewCateViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_view);
        textView = itemView.findViewById(R.id.title);
    }

    @Override
    public void update() {
        Log.d(TAG, "data = " + getData());
        MockDataBean mockDataBean = (MockDataBean) getData();
        textView.setText(mockDataBean.getName());
        Glide.with(imageView.getContext()).load(mockDataBean.getIcon()).into(imageView);

        if (imageView.getContext() instanceof Activity) {
        }
    }
}
