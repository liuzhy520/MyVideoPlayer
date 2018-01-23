package com.liuzhy520.myvideoplayer.core;

/**
 * Created by wayne on 1/23/18.
 * Modified by Wayne
 * here will give the video status callback
 */

public interface onVideoStatusCallback {

    /**
     * will be called when video is started
     */
    void onStart();

    /**
     * will be called when video is paused
     */
    void onPaused();

    /**
     * will be called when video is buffering
     * @param buffer will be the value of buffer
     * @param extra extra info value
     */
    void onBuffering(int buffer, Object extra);

    /**
     * will be called when video is stopped
     */
    void onStop();

    /**
     * will be called when video is released
     */
    void onReleased();
}
