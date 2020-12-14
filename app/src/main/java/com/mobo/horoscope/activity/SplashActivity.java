package com.mobo.horoscope.activity;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mobo.horoscope.R;
import com.mobo.horoscope.common.Constants;
import com.mobo.horoscope.common.SPUtils;
import com.mobo.horoscope.tracker.FirebaseTracker;
import com.mobo.horoscope.tracker.AppTracker;

import java.io.IOException;

/**
 * @Description: 启动页
 * @Author: jzhou
 * @CreateDate: 19-8-13 上午9:12
 */
public class SplashActivity extends BaseActivity implements MediaPlayer.OnCompletionListener,
        SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {
    private int mPositionWhenPaused = -1;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseTracker.getInstance().track(AppTracker.start_page_show);

        mSurfaceView = findViewById(R.id.video_view);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setKeepScreenOn(true);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mPlayer.setVolume(0,  0);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnPreparedListener(this);

        try {
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.video);
            mPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                    file.getLength());
            mPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            //mPlayer.setLooping(true);
            mPlayer.prepare();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void startNextActivity() {
        boolean firstLogin = SPUtils.getBoolean(this, Constants.FIRST_LOGIN, true);
        if (firstLogin) {
            HoroscopeSelectorActivity.start(this);
        } else {
            MainActivity.start(this);
        }
        finish();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //mSurfaceView.setLayoutParams(lp);
        if (!mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        startNextActivity();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPlayer.setDisplay(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onResume() {
        if (mPositionWhenPaused >= 0 && mPlayer != null) {
            mPlayer.seekTo(mPositionWhenPaused);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPositionWhenPaused = mPlayer.getCurrentPosition();
            mPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer == null) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.release();
    }
}
