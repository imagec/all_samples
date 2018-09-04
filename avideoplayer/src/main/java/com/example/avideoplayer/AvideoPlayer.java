package com.example.avideoplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sysutils.SysUtils;

import java.io.IOException;

public class AvideoPlayer extends RelativeLayout implements View.OnClickListener {
    final private String TAG = "AvideoPlayer";

    private boolean isFullScreen = false;

    private final int MEDIA_PLAYER_STATE_ERROR = 0;
    private final int MEDIA_PLAYER_IDLE = 1 << 0;
    private final int MEDIA_PLAYER_INITIALIZED = 1 << 1;
    private final int MEDIA_PLAYER_PREPARING = 1 << 2;
    private final int MEDIA_PLAYER_PREPARED = 1 << 3;
    private final int MEDIA_PLAYER_STARTED = 1 << 4;
    private final int MEDIA_PLAYER_PAUSED = 1 << 5;
    private final int MEDIA_PLAYER_STOPPED = 1 << 6;
    private final int MEDIA_PLAYER_PLAYBACK_COMPLETE = 1 << 7;

    private int mPlayerState = MEDIA_PLAYER_STATE_ERROR;

    private RelativeLayout avideoLayout = null;
    private RelativeLayout layout = null;
    private TextureView textureView = null;
    private TextView infoMessage = null;
    private ImageView playPauseButton;
    private ImageView bigPlayPauseButton;
    private TextView currentDuration;
    private TextView maxDuration;
    private SeekBar seekBar;
    private ImageView screenResize;
    private LinearLayout playProBar;
    private TextView bufferPercent;

    private ConnectivityManager connectivityManager;
    private boolean isNetworkMonitor = false;

    private MediaPlayer mediaPlayer = null;

    private int mVideoScale = 1;

    private String mVideoUrl = null;

    public AvideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.avideo_player_layout, this, true);
        avideoLayout = findViewById(R.id.video_layout);
        layout = findViewById(R.id.layout);
        textureView = findViewById(R.id.video);
        infoMessage = findViewById(R.id.info_message);
        playPauseButton = findViewById(R.id.play_pause);
        bigPlayPauseButton = findViewById(R.id.big_play_pause);
        currentDuration = findViewById(R.id.current_duration);
        maxDuration = findViewById(R.id.max_duration);
        seekBar = findViewById(R.id.seek_bar);
        screenResize = findViewById(R.id.screen_resize);
        playProBar = findViewById(R.id.play_progress_bar);
        bufferPercent = findViewById(R.id.buffer_percent);
    }

    private int initNetworkInfoMonitor() {
        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        isNetworkMonitor = true;
        return 0;
    }

    private boolean checkMediaPlayerState(int currentState, int nextState) {
        if (nextState == MEDIA_PLAYER_IDLE) {
            return true;
        } else if (nextState == MEDIA_PLAYER_INITIALIZED) {
            if (currentState == MEDIA_PLAYER_IDLE) {
                return true;
            }
        } else if (nextState == MEDIA_PLAYER_PREPARED) {
            if (currentState == MEDIA_PLAYER_INITIALIZED ||
                    currentState == MEDIA_PLAYER_PREPARING ||
                    currentState == MEDIA_PLAYER_STOPPED) {
                return true;
            }
        } else if (nextState == MEDIA_PLAYER_STARTED) {
            if (currentState == MEDIA_PLAYER_PREPARED ||
                    currentState == MEDIA_PLAYER_PAUSED ||
                    currentState == MEDIA_PLAYER_PLAYBACK_COMPLETE) {
                return true;
            }
        } else if (nextState == MEDIA_PLAYER_PLAYBACK_COMPLETE) {
            if (currentState == MEDIA_PLAYER_STARTED) {
                return true;
            }
        } else if (nextState == MEDIA_PLAYER_STOPPED) {
            return (currentState == MEDIA_PLAYER_PREPARED ||
                    currentState == MEDIA_PLAYER_STARTED ||
                    currentState == MEDIA_PLAYER_PAUSED ||
                    currentState == MEDIA_PLAYER_PLAYBACK_COMPLETE);
        } else if (nextState == MEDIA_PLAYER_PREPARING) {
            if (currentState == MEDIA_PLAYER_STOPPED ||
                    currentState == MEDIA_PLAYER_INITIALIZED) {
                return true;
            }
        } else if (nextState == MEDIA_PLAYER_PAUSED) {
            if (currentState == MEDIA_PLAYER_STARTED) {
                return true;
            }
        }

        return false;
    }

    private void updatePlayerController(int state) {
        if (state == MEDIA_PLAYER_IDLE) {
            infoMessage.setVisibility(View.GONE);
            bigPlayPauseButton.setVisibility(View.VISIBLE);
            playPauseButton.setImageResource(R.drawable.play_button);

            return;
        }

        if (state == MEDIA_PLAYER_STARTED || state == MEDIA_PLAYER_INITIALIZED ||
                mPlayerState == MEDIA_PLAYER_PREPARING) {
            infoMessage.setText("loading");
            infoMessage.setVisibility(View.VISIBLE);
            bigPlayPauseButton.setVisibility(View.GONE);
            playPauseButton.setImageResource(R.drawable.pause_button);
        } else {
            bigPlayPauseButton.setVisibility(View.VISIBLE);
            playPauseButton.setImageResource(R.drawable.play_button);
        }

        if (state == MEDIA_PLAYER_PREPARING) {
            infoMessage.setVisibility(View.VISIBLE);
        }

        if (state == MEDIA_PLAYER_PREPARED) {
            seekBar.setMax(mediaPlayer.getDuration());
            maxDuration.setText(String.valueOf(mediaPlayer.getDuration() / 1000));
        }
    }

    public void resetPlayer() {
        if (!setMediaPlayerState(MEDIA_PLAYER_IDLE)) {
            Log.d(TAG, "resetPlayer failed");
            return;
        }
    }

    public int startPlay() {
        if (mVideoUrl == null) {
            return -1;
        }
        resetPlayer();

        if (!setMediaPlayerState(MEDIA_PLAYER_INITIALIZED)) {
            Log.d(TAG, "startPlay failed");
            return -1;
        }


        if (!setMediaPlayerState(MEDIA_PLAYER_PREPARING)) {
            Log.d(TAG, "startPlay failed");
            return -1;
        }

        return 0;
    }


    public void startVideo() {
        if (!setMediaPlayerState(MEDIA_PLAYER_STARTED)) {
            Log.d(TAG, "startVideo failed");
            startPlay();
            return;
        }
    }

    public void pauseVideo() {
        if (!setMediaPlayerState(MEDIA_PLAYER_PAUSED)) {
            Log.d(TAG, "pauseVideo failed");
            startPlay();
            return;
        }
    }

    private boolean setMediaPlayerState(int newState) {
        if (checkMediaPlayerState(mPlayerState, newState)) {
            Log.d(TAG, "change currentState : " + mPlayerState + " to nextState : " + newState);
            mPlayerState = newState;

            updatePlayerController(newState);
            switch (newState) {
                case MEDIA_PLAYER_STOPPED:
                    mediaPlayer.stop();
                    break;
                case MEDIA_PLAYER_STARTED:
                    mediaPlayer.start();
                    break;
                case MEDIA_PLAYER_PAUSED:
                    mediaPlayer.pause();
                    break;
                case MEDIA_PLAYER_IDLE:
                    mediaPlayer.reset();
                    break;
                case MEDIA_PLAYER_PREPARED:
                    startVideo();
                    break;
                case MEDIA_PLAYER_INITIALIZED:
                    try {
                        mediaPlayer.setDataSource(mVideoUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case MEDIA_PLAYER_PREPARING:
                    mediaPlayer.prepareAsync();
                    break;
            }
            return true;
        }

        Log.d(TAG, "can't change currentState:" + mPlayerState + " to nextState:" + newState);

        return false;
    }

    private void setTextureViewScalef(double scale) {
        LayoutParams videoParams = (LayoutParams) layout.getLayoutParams();
        videoParams.width = SysUtils.getScreenHeight(getContext());
        double h = videoParams.width * (double) 9f / (double) 16f;
        videoParams.height = (int) h;
        layout.setLayoutParams(videoParams);
    }

    private void setTextureViewScale(double scale) {
        int width = this.getRight();
        int height = this.getBottom();
        double parentScale = (double) width / (double) height;
        Log.d(TAG, "setTextureViewScale width : " + width + " height :" + height);
        if (parentScale < scale) {
            LayoutParams videoParams = (LayoutParams) layout.getLayoutParams();
            videoParams.width = width;
            double h = (double) width * (double) 9f / (double) 16f;
            videoParams.height = (int) h;
            layout.setLayoutParams(videoParams);
        }

        if (parentScale > scale) {
            LayoutParams videoParams = (LayoutParams) layout.getLayoutParams();
            videoParams.height = height;
            double w = (double) height * (double) 16f / (double) 9f;
            videoParams.width = (int) w;
            layout.setLayoutParams(videoParams);
        }
    }

    private int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    private void mediaPlayerInit() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                Log.d(TAG, "what = " + what + " extra = " + extra);
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START");
                        infoMessage.setVisibility(View.GONE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        Log.d(TAG, "MEDIA_INFO_BUFFERING_START");
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        Log.d(TAG, "MEDIA_INFO_BUFFERING_END");
                        break;
                }
                return true;
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (!setMediaPlayerState(MEDIA_PLAYER_PREPARED)) {
                    Log.d(TAG, "onPrepared failed");
                    return;
                }
                Log.d(TAG, "onPrepared");
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (!setMediaPlayerState(MEDIA_PLAYER_PLAYBACK_COMPLETE)) {
                    Log.d(TAG, "onCompletion failed");
                    return;
                }
                Log.d(TAG, "onCompletion");
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Log.d(TAG, "setOnErrorListener i = " + i + "i1 = " + i1);
                if (i == -38) {
                    resetPlayer();
                    return true;
                }

                Log.d(TAG, Log.getStackTraceString(new Throwable()));
                resetPlayer();
                return true;
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                Log.d(TAG, "onBufferingUpdate Percent = " + i);
                if (i == 100) {
                    bufferPercent.setVisibility(GONE);
                } else {
                    bufferPercent.setVisibility(VISIBLE);
                    bufferPercent.setText(String.valueOf(i));
                }
            }
        });

        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                Log.d(TAG, "onVideoSizeChanged width :" + i + " height :" + i1);
                double scale = (double) i / (double) i1;
                Log.d(TAG, "onVideoSizeChanged scale :" + scale);
                if (scale < 1.8 && scale > 1.7) {
                    mVideoScale = 16;
                }

                if (scale < 1.4 && scale > 1.3) {
                    mVideoScale = 4;
                }

                if (!isFullScreen) {
                    setTextureViewScale(scale);
                } else {
                    setTextureViewScalef(scale);
                }

            }
        });

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private boolean isSurfaceTextureAvailable = false;
    private Surface surface = null;

    private void setMediaPlayerSurface(Surface surface) {
        if (mediaPlayer == null) {
            Log.d(TAG, "setMediaPlayerSurface, mediaPlayer == null");
            return;
        }

        mediaPlayer.setSurface(surface);
    }

    private void handlePlayPause() {
        if (mPlayerState == MEDIA_PLAYER_IDLE) {
            if (mVideoUrl == null) {
                Log.d(TAG, "Panic not set video url");
                return;
            }

            startPlay();
            return;
        }

        if (mPlayerState == MEDIA_PLAYER_PREPARING) {
            setMediaPlayerState(MEDIA_PLAYER_IDLE);
            return;
        }

        if (mPlayerState == MEDIA_PLAYER_STARTED) {
            pauseVideo();
        } else {
            startVideo();
        }
    }


    public void avideoPlayerInit() {
        mediaPlayerInit();
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                isSurfaceTextureAvailable = true;
                surface = new Surface(surfaceTexture);
                setMediaPlayerSurface(surface);
                Log.d(TAG, "onSurfaceTextureAvailable");
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
                Log.d(TAG, "onSurfaceTextureSizeChanged");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                Log.d(TAG, "onSurfaceTextureDestroyed");
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });

        textureView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePlayPause();
            }
        });

        playPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePlayPause();
            }
        });

        screenResize.setOnClickListener(this);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (!b) {
                    return;
                }

                if (seekBar.getId() == R.id.seek_bar) {
                    if (checkIfCanSeek()) {
                        mediaPlayer.seekTo(i);
                    } else {
                        Log.d(TAG, "mediaPlayer can not seek");
                        resetPlayer();
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mPlayerLoopHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (checkIfCanSeek()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
                return false;
            }
        });

        startSeekBarUpdateThread();
        resetPlayer();
        initNetworkInfoMonitor();
    }

    @Override
    public void onClick(View view) {
        if (isFullScreen) {
            SysUtils.exitFullScreen((Activity) getContext(), this, avideoLayout);
            isFullScreen = false;
            setTextureViewScale(1.77777);
        } else {
            SysUtils.enterFullScreen((Activity) getContext(), this, avideoLayout);
            isFullScreen = true;
            setTextureViewScalef(1.77777);
        }
    }

    private boolean checkIfCanSeek() {
        if (mPlayerState == MEDIA_PLAYER_STARTED ||
                mPlayerState == MEDIA_PLAYER_PREPARED ||
                mPlayerState == MEDIA_PLAYER_PAUSED ||
                mPlayerState == MEDIA_PLAYER_PLAYBACK_COMPLETE) {
            return true;
        }

        return false;
    }

    private boolean isThreadLoop = false;
    private int loopInterval = 100;
    private Handler mPlayerLoopHandler = null;

    private void startSeekBarUpdateThread() {
        if (isThreadLoop) {
            return;
        } else {
            isThreadLoop = true;
        }

        new Thread() {
            @Override
            public void run() {
                while (isThreadLoop) {
                    try {
                        sleep(loopInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mPlayerLoopHandler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    public void setVideoUrl(String url) {
        mVideoUrl = url;
    }

}
