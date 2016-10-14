package com.liuzhy520.myvideoplayer.entity;

import android.media.MediaPlayer;

import com.liuzhy520.myvideoplayer.core.IPlayer;

/**
 * Created by Wayne on 2016/10/5.
 * using {@link android.media.MediaPlayer} to play videos in here
 * it can preload the next video in the list
 */

public class MediaPlayerEntity implements IPlayer,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void release() {

    }

    @Override
    public void seekTo(int position) {

    }

    @Override
    public int getCurrentVideoPosition() {
        return 0;
    }

    @Override
    public int getCurrentVideoDuration() {
        return 0;
    }

    @Override
    public boolean isVideoPlaying() {
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
