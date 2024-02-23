package com.example.myfoodchoice.BackgroundVideo;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.myfoodchoice.R;

import java.io.IOException;

public class BackgroundVideoView extends SurfaceView implements SurfaceHolder.Callback {
    private MediaPlayer mediaPlayer;

    private Boolean isStarted = false;

    public BackgroundVideoView(Context context)
    {
        super(context);
        init();
    }

    public BackgroundVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();

    }

    public BackgroundVideoView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();

    }

    public BackgroundVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init()
    {
        mediaPlayer = new MediaPlayer();
        getHolder().addCallback(this);

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder)
    {
        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.bg_video);
        try
        {
            if (!isStarted)
            {
                isStarted = true;
                mediaPlayer.setDataSource
                        (afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            }

            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = getWidth();
            layoutParams.height = getHeight();
            setLayoutParams(layoutParams);

            // to run the video as background.
            mediaPlayer.prepare();
            mediaPlayer.setDisplay(getHolder());
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder)
    {
        mediaPlayer.stop();
    }
}
