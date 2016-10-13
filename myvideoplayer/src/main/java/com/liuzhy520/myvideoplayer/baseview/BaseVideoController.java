package com.liuzhy520.myvideoplayer.baseview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.liuzhy520.myvideoplayer.core.IPlayer;

/**
 * Created by Wayne on 2016/10/13.
 * video controller
 */

public abstract class BaseVideoController extends RelativeLayout implements IPlayer{

    protected IPlayer iPlayer;

    protected Context mContext;

    protected View mContentView;

    public BaseVideoController(Context context){
        super(context);
        initViews();
    }

    public BaseVideoController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    protected abstract void initViews();

    protected abstract void setViews();

    protected abstract void updateController(int position, int duration, boolean isPlaying);

    protected abstract void landscapeMode();

    protected abstract void portraitMode();

    protected void setContentView(int id){
        mContentView = LayoutInflater.from(mContext).inflate(id, this);
    }

    public void setVideoPlayer(IPlayer iPlayer){
        this.iPlayer = iPlayer;
        if(iPlayer != null){
            setViews();
        }
    }

    @Override
    public void start() {
        iPlayer.start();
    }

    @Override
    public void pause() {
        iPlayer.pause();
    }

    @Override
    public void stop() {
        iPlayer.stop();
    }

    @Override
    public void release() {
        iPlayer.release();
    }

    @Override
    public void seekTo(int position) {
        iPlayer.seekTo(position);
    }

    @Override
    public int getCurrentVideoPosition() {
        return iPlayer.getCurrentVideoPosition();
    }

    @Override
    public int getCurrentVideoDuration() {
        return iPlayer.getCurrentVideoDuration();
    }

    @Override
    public boolean isVideoPlaying() {
        return iPlayer.isVideoPlaying();
    }
}
