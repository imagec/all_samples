package com.example.image.all_samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.avideoplayer.AvideoPlayer;

public class CplayerActivity extends Activity {
    private AvideoPlayer avideoPlayer;
    private String url = "https://g.alicdn.com/forest/ai-labs-portal-web/0.5.22/video/main.mp4";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_avideo_player);

        avideoPlayer = findViewById(R.id.avideo_player);
        avideoPlayer.avideoPlayerInit();
        avideoPlayer.setVideoUrl(url);
    }

    @Override
    public void onPause() {
        super.onPause();
        avideoPlayer.pauseVideo();
    }

    @Override
    public void onResume() {
        super.onResume();
        avideoPlayer.startVideo();
    }
}
